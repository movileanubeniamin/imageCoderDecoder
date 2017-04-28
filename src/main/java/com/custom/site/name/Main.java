package com.custom.site.name;

import java.util.List;
import java.util.Map;

import com.custom.site.name.decoder.FirstLevelDecoder;
import com.custom.site.name.decoder.SecondLevelDecoder;
import com.custom.site.name.encoder.FirstLevelEncoder;
import com.custom.site.name.encoder.SecondLevelEncoder;
import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;
import com.custom.site.name.tests.TestImageEncoderDecoder;
import com.custom.site.name.utils.FileUtils;


public class Main {


    public static void main(String[] args) {
         start();

        // Tests
//        TestImageEncoderDecoder.testSubsample();
//        TestImageEncoderDecoder.testOrderSub(7500);
//        TestImageEncoderDecoder.testOrderSub2(7500);
//        TestImageEncoderDecoder.testareMatBlo(800, 600, 8);
//        TestImageEncoderDecoder.testareMatBlo(8, 8, 4);
//        TestImageEncoderDecoder.testareMatBlo(4, 4, 2);
//        TestImageEncoderDecoder.compareFiles();
    }


    private static void start() {
        // First Level encoder
        // Reading file
        System.out.println("Reading ppm file...\n");
        String fileName = "src/main/resources/image.ppm";
        List<Integer> image = FileUtils.readPpm(fileName);

        // Getting image info (width, height, RGB list)
        System.out.println("Getting image info (width, height, max pixel value, RGB list)...\n");
        Integer width = image.get(0);
        Integer height = image.get(1);
        Integer maxpix = image.get(2);
        List<RGB> rgbImage = FirstLevelEncoder.convertListToRgb(image);

        // RGB to YUV conversion
        System.out.println("Converting RGB List to YUV List...\n");
        List<YUV> yuvImage = FirstLevelEncoder.convertRGBImageToYUV(rgbImage);

        // YUV list to YUV matrix conversion
        System.out.println("Converting YUV list to YUV matrix...\n");
        Map<String, long[][]> imageMatrix = FirstLevelEncoder.convertListToMatrix(yuvImage, width, height);

        // YUV matrix to 3 separate lists of 8x8 matrices conversion
        System.out.println("Converting YUV matrix to 3 separate lists of 8x8 matrices...\n");
        List<long[][]> yBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("y"), 8);
        List<long[][]> uBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("u"), 8);
        List<long[][]> vBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("v"), 8);

        // Downsample the U and V matrix
        System.out.println("Subsampling the U and V 8x8 matrices with 4:2:0 ratio...\n");
        List<long[][]> uDownBlocks = FirstLevelEncoder.subSampleBlocks(uBlocks);
        List<long[][]> vDownBlocks = FirstLevelEncoder.subSampleBlocks(vBlocks);


        // ========================================================================================================
        // First Level decoder
        // Resize the subsampled U and V matrix
        System.out.println("Resizing the subsampled U and V matrices to 8x8...\n");
        List<long[][]> resUSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(uDownBlocks);
        List<long[][]> resVSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(vDownBlocks);


        // ========================================================================================================
        // Second Level encoder
        // Add 128 to every pixel and do the Forward DCT
        System.out.println("Adding 128 to every pixel and dooing the Forward DCT...\n");
        List<long[][]> yBlocksDCT = SecondLevelEncoder.listToDCT(yBlocks);
        List<long[][]> uBlocksDCT = SecondLevelEncoder.listToDCT(resUSubBlocks);
        List<long[][]> vBlocksDCT = SecondLevelEncoder.listToDCT(resVSubBlocks);

        // Doing the Quantization phase
        System.out.println("Doing the Quantization phase...\n");
        List<long[][]> yBlocksQunati = SecondLevelEncoder.listToQuanti(yBlocksDCT);
        List<long[][]> uBlocksQunati = SecondLevelEncoder.listToQuanti(uBlocksDCT);
        List<long[][]> vBlocksQunati = SecondLevelEncoder.listToQuanti(vBlocksDCT);


        // ========================================================================================================
        // Second Level decoder
        // Doing the Dequantization phase
        System.out.println("Doing the Dequantization phase...\n");
        List<long[][]> yBlocksDequnati = SecondLevelDecoder.listToDequanti(yBlocksQunati);
        List<long[][]> uBlocksDequnati = SecondLevelDecoder.listToDequanti(uBlocksQunati);
        List<long[][]> vBlocksDequnati = SecondLevelDecoder.listToDequanti(vBlocksQunati);

        // Doing the Inverse DCT and substracting 128 from every pixel
        System.out.println("Doing the Inverse DCT and substracting 128 from every pixel...\n");
        List<long[][]> yBlocksIDCT = SecondLevelDecoder.listToIDCT(yBlocksDequnati);
        List<long[][]> uBlocksIDCT = SecondLevelDecoder.listToIDCT(uBlocksDequnati);
        List<long[][]> vBlocksIDCT = SecondLevelDecoder.listToIDCT(vBlocksDequnati);

        // ========================================================================================================
        // First Level decoder
        // Reconstruct the matrix from block list
        System.out.println("Reconstructing the matrices from block lists...\n");
        long[][] recreatedMatrixY = FirstLevelDecoder.blocksToMatrix(yBlocksIDCT, width, height);
        long[][] recreatedMatrixU = FirstLevelDecoder.blocksToMatrix(uBlocksIDCT, width, height);
        long[][] recreatedMatrixV = FirstLevelDecoder.blocksToMatrix(vBlocksIDCT, width, height);

        // Create YUV List from Matrix
        System.out.println("Converting to YUV List from Matrix...\n");
        List<YUV> yuvNewImage = FirstLevelDecoder.convertMatrixToYUVList(recreatedMatrixY, recreatedMatrixU,
                recreatedMatrixV, width, height);

        // Convert YUV List to RGB List
        System.out.println("Converting to RGB List from YUV List...\n");
        List<RGB> rgbNewImage = FirstLevelDecoder.convertRGBImageToYUV(yuvNewImage);

        // Convert RGB List into Image List and write it to file
        System.out.println("Converting the RGB List into Image List and writing it to file...\n");
        List<Integer> newImage = FirstLevelDecoder.convertRgbToList(rgbNewImage);
        FileUtils.writePpm("src/main/resources/newImage.ppm", newImage, width, height, maxpix);

        System.out.println("Done!\n");
    }

}
