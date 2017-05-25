package com.custom.site.name;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.custom.site.name.decoder.FirstLevelDecoder;
import com.custom.site.name.decoder.SecondLevelDecoder;
import com.custom.site.name.decoder.ThirdLevelDecoder;
import com.custom.site.name.encoder.FirstLevelEncoder;
import com.custom.site.name.encoder.SecondLevelEncoder;
import com.custom.site.name.encoder.ThirdLevelEncoder;
import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;
import com.custom.site.name.tests.TestImageEncoderDecoder;
import com.custom.site.name.utils.FileUtils;
import com.custom.site.name.utils.ImageUtils;


public class Main {


    public static void main(String[] args) {


        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the file name(no need for extension): ");
        String imageName = reader.next();
        System.out.println("Enter the compressed file name(no need for extension): ");
        String newImageName = reader.next();
        start(imageName, newImageName);


        // Tests
//        TestImageEncoderDecoder.testSubsample();
//        TestImageEncoderDecoder.testOrderSub(7500);
//        TestImageEncoderDecoder.testOrderSub2(7500);
//        TestImageEncoderDecoder.testareMatBlo(800, 600, 8);
//        TestImageEncoderDecoder.testareMatBlo(8, 8, 4);
//        TestImageEncoderDecoder.testareMatBlo(4, 4, 2);
//        TestImageEncoderDecoder.compareFiles();
    }


    private static void start(String imageName, String newImageName) {
        // First Level encoder
        // Reading file
        System.out.println("Reading ppm file...");
        String fileName = "src/main/resources/"+imageName+".ppm";
        List<Integer> image = FileUtils.readPpm(fileName);

        // Getting image info (width, height, RGB list)
        System.out.println("Getting image info (width, height, max pixel value, RGB list)...");
        Integer width = image.get(0);
        Integer height = image.get(1);
        Integer maxpix = image.get(2);
        List<RGB> rgbImage = FirstLevelEncoder.convertListToRgb(image);

        // RGB to YUV conversion
        System.out.println("Converting RGB List to YUV List...");
        List<YUV> yuvImage = FirstLevelEncoder.convertRGBImageToYUV(rgbImage);

        // YUV list to YUV matrix conversion
        System.out.println("Converting YUV list to YUV matrix...");
        Map<String, long[][]> imageMatrix = FirstLevelEncoder.convertListToMatrix(yuvImage, width, height);

        // YUV matrix to 3 separate lists of 8x8 matrices conversion
        System.out.println("Converting YUV matrix to 3 separate lists of 8x8 matrices...");
        List<long[][]> yBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("y"), 8);
        List<long[][]> uBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("u"), 8);
        List<long[][]> vBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("v"), 8);

        // Downsample the U and V matrix
        System.out.println("Subsampling the U and V 8x8 matrices with 4:2:0 ratio...");
        List<long[][]> uDownBlocks = FirstLevelEncoder.subSampleBlocks(uBlocks);
        List<long[][]> vDownBlocks = FirstLevelEncoder.subSampleBlocks(vBlocks);


        // ========================================================================================================
        // First Level decoder
        // Resize the subsampled U and V matrix
        System.out.println("Resizing the subsampled U and V matrices to 8x8...");
        List<long[][]> resUSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(uDownBlocks);
        List<long[][]> resVSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(vDownBlocks);


        // ========================================================================================================
        // Second Level encoder
        // Add 128 to every pixel and do the Forward DCT
        System.out.println("Adding 128 to every pixel and doing the Forward DCT...");
        List<long[][]> yBlocksDCT = SecondLevelEncoder.listToDCT(yBlocks);
        List<long[][]> uBlocksDCT = SecondLevelEncoder.listToDCT(resUSubBlocks);
        List<long[][]> vBlocksDCT = SecondLevelEncoder.listToDCT(resVSubBlocks);

        // Doing the Quantization phase
        System.out.println("Doing the Quantization phase...");
        List<long[][]> yBlocksQunati = SecondLevelEncoder.listToQuanti(yBlocksDCT);
        List<long[][]> uBlocksQunati = SecondLevelEncoder.listToQuanti(uBlocksDCT);
        List<long[][]> vBlocksQunati = SecondLevelEncoder.listToQuanti(vBlocksDCT);


        // ========================================================================================================
        // Third Level encoder
        System.out.println("Creating the ZigZag vector...");
        List<Long> zigZagY = ThirdLevelEncoder.listToZigZag(yBlocksQunati);
        List<Long> zigZagU = ThirdLevelEncoder.listToZigZag(uBlocksQunati);
        List<Long> zigZagV = ThirdLevelEncoder.listToZigZag(vBlocksQunati);


        // ========================================================================================================
        // Third Level decoder
        System.out.println("Creating back to blocks from the ZigZag vector...");
        List<long[][]> unZigZagY = ThirdLevelDecoder.zigZagToList(zigZagY);
        List<long[][]> unZigZagU = ThirdLevelDecoder.zigZagToList(zigZagU);
        List<long[][]> unZigZagV = ThirdLevelDecoder.zigZagToList(zigZagV);


        // ========================================================================================================
        // Second Level decoder
        // Doing the Dequantization phase
        System.out.println("Doing the Dequantization phase...");
        List<long[][]> yBlocksDequnati = SecondLevelDecoder.listToDequanti(unZigZagY);
        List<long[][]> uBlocksDequnati = SecondLevelDecoder.listToDequanti(unZigZagU);
        List<long[][]> vBlocksDequnati = SecondLevelDecoder.listToDequanti(unZigZagV);

        // Doing the Inverse DCT and substracting 128 from every pixel
        System.out.println("Doing the Inverse DCT and substracting 128 from every pixel...");
        List<long[][]> yBlocksIDCT = SecondLevelDecoder.listToIDCT(yBlocksDequnati);
        List<long[][]> uBlocksIDCT = SecondLevelDecoder.listToIDCT(uBlocksDequnati);
        List<long[][]> vBlocksIDCT = SecondLevelDecoder.listToIDCT(vBlocksDequnati);

        // ========================================================================================================
        // First Level decoder
        // Reconstruct the matrix from block list
        System.out.println("Reconstructing the matrices from block lists...");
        long[][] recreatedMatrixY = FirstLevelDecoder.blocksToMatrix(yBlocksIDCT, width, height);
        long[][] recreatedMatrixU = FirstLevelDecoder.blocksToMatrix(uBlocksIDCT, width, height);
        long[][] recreatedMatrixV = FirstLevelDecoder.blocksToMatrix(vBlocksIDCT, width, height);

        // Create YUV List from Matrix
        System.out.println("Converting to YUV List from Matrix...");
        List<YUV> yuvNewImage = FirstLevelDecoder.convertMatrixToYUVList(recreatedMatrixY, recreatedMatrixU,
                recreatedMatrixV, width, height);

        // Convert YUV List to RGB List
        System.out.println("Converting to RGB List from YUV List...");
        List<RGB> rgbNewImage = FirstLevelDecoder.convertRGBImageToYUV(yuvNewImage);

        // Convert RGB List into Image List and write it to file
        System.out.println("Converting the RGB List into Image List and writing it to file...");
        List<Integer> newImage = FirstLevelDecoder.convertRgbToList(rgbNewImage);
        FileUtils.writePpm("src/main/resources/"+newImageName+".ppm", newImage, width, height, maxpix);


        System.out.println("Done!");
    }

}
