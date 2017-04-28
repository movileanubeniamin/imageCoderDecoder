package com.custom.site.name;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.custom.site.name.decoder.FirstLevelDecoder;
import com.custom.site.name.decoder.SecondLevelDecoder;
import com.custom.site.name.encoder.FirstLevelEncoder;
import com.custom.site.name.encoder.SecondLevelEncoder;
import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;
import com.custom.site.name.utils.FileUtils;
import com.custom.site.name.utils.OtherUtils;

public class Main {

    public static void main(String[] args) {
         start();

        // Tests
//        testSubsample();
//        testOrderSub(7500);
//        testOrderSub2(7500);
//        testareMatBlo(800, 600, 8);
//        testareMatBlo(8, 8, 4);
//        testareMatBlo(4, 4, 2);
//        compareFiles();
    }

    private static void start() {
        // First Level encoder
        // Reading file
        System.out.println("Reading file\n");
        String fileName = "src/main/resources/image.ppm";
        List<Integer> image = FileUtils.readPpm(fileName);

        // Getting image info (width, height, RGB list)
        System.out.println("Getting image info (width, height, RGB list)\n");
        Integer width = image.get(0);
        Integer height = image.get(1);
        Integer maxpix = image.get(2);
        List<RGB> rgbImage = FirstLevelEncoder.convertListToRgb(image);

        // RGB to YUV conversion
        System.out.println("RGB to YUV conversion\n");
        List<YUV> yuvImage = FirstLevelEncoder.convertRGBImageToYUV(rgbImage);

        // YUV list to YUV matrix conversion
        System.out.println("YUV list to YUV matrix conversion\n");
        Map<String, long[][]> imageMatrix = FirstLevelEncoder.convertListToMatrix(yuvImage, width, height);

        // YUV matrix to 3 separate lists of 8x8 matrices conversion
        System.out.println("YUV matrix to 3 separate lists of 8x8 matrices conversion\n");
        List<long[][]> yBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("y"), 8);
        List<long[][]> uBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("u"), 8);
        List<long[][]> vBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("v"), 8);

        // Downsample the U and V matrix
        System.out.println("Downsample the U and V matrix\n");
        List<long[][]> uDownBlocks = FirstLevelEncoder.subSampleBlocks(uBlocks);
        List<long[][]> vDownBlocks = FirstLevelEncoder.subSampleBlocks(vBlocks);


        // ========================================================================================================
        // First Level decoder
        // Resize the subsampled U and V matrix
        System.out.println("Resize the subsampled U and V matrix\n");
        List<long[][]> resUSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(uDownBlocks);
        List<long[][]> resVSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(vDownBlocks);


        // ========================================================================================================
        // Second Level encoder
        // Add 128 to every pixel and do the Forward DCT
        System.out.println("Add 128 to every pixel and do the Forward DCT\n");
        List<long[][]> yBlocksDCT = SecondLevelEncoder.listToDCT(yBlocks);
        List<long[][]> uBlocksDCT = SecondLevelEncoder.listToDCT(resUSubBlocks);
        List<long[][]> vBlocksDCT = SecondLevelEncoder.listToDCT(resVSubBlocks);

        // Doing the Quantization phase
        System.out.println("Doing the Quantization phase\n");
        List<long[][]> yBlocksQunati = SecondLevelEncoder.listToQuanti(yBlocksDCT);
        List<long[][]> uBlocksQunati = SecondLevelEncoder.listToQuanti(uBlocksDCT);
        List<long[][]> vBlocksQunati = SecondLevelEncoder.listToQuanti(vBlocksDCT);


        // ========================================================================================================
        // Second Level decoder
        // Doing the Dequantization phase
        System.out.println("Doing the Dequantization phase\n");
        List<long[][]> yBlocksDequnati = SecondLevelDecoder.listToDequanti(yBlocksQunati);
        List<long[][]> uBlocksDequnati = SecondLevelDecoder.listToDequanti(uBlocksQunati);
        List<long[][]> vBlocksDequnati = SecondLevelDecoder.listToDequanti(vBlocksQunati);

        // Doing the Inverse DCT and substracting 128 from every pixel
        System.out.println("Doing the Inverse DCT and substracting 128 from every pixel\n");
        List<long[][]> yBlocksIDCT = SecondLevelDecoder.listToIDCT(yBlocksDequnati);
        List<long[][]> uBlocksIDCT = SecondLevelDecoder.listToIDCT(uBlocksDequnati);
        List<long[][]> vBlocksIDCT = SecondLevelDecoder.listToIDCT(vBlocksDequnati);

        // ========================================================================================================
        // First Level decoder
        // Reconstruct the matrix from block list
        System.out.println("Reconstruct the matrix from block list\n");
        long[][] recreatedMatrixY = FirstLevelDecoder.blocksToMatrix(yBlocksIDCT, width, height);
        long[][] recreatedMatrixU = FirstLevelDecoder.blocksToMatrix(uBlocksIDCT, width, height);
        long[][] recreatedMatrixV = FirstLevelDecoder.blocksToMatrix(vBlocksIDCT, width, height);

        // Create YUV List from Matrix
        System.out.println("Create YUV List from Matrix\n");
        List<YUV> yuvNewImage = FirstLevelDecoder.convertMatrixToYUVList(recreatedMatrixY, recreatedMatrixU,
                recreatedMatrixV, width, height);

        // Convert YUV List to RGB List
        System.out.println("Convert YUV List to RGB List\n");
        List<RGB> rgbNewImage = FirstLevelDecoder.convertRGBImageToYUV(yuvNewImage);

        // Convert RGB List into Image List and write it to file
        System.out.println("Convert RGB List into Image List and write it to file\n");
        List<Integer> newImage = FirstLevelDecoder.convertRgbToList(rgbNewImage);
        FileUtils.writePpm("src/main/resources/newImage.ppm", newImage, width, height, maxpix);

        System.out.println("Done!\n");
    }


    private static void testareMatBlo(int width, int height, int blockdim) {
        long[][] textMatrix = new long[width][height];
        int val = 1;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                textMatrix[i][j] = val;
                val++;
            }
        }
        List<long[][]> blocks = FirstLevelEncoder.matrixToBlocks(textMatrix, blockdim);
        long[][] recreatedMatrix = FirstLevelDecoder.blocksToMatrix(blocks, width, height);
        if (Arrays.deepEquals(textMatrix, recreatedMatrix)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }


    private static void testSubsample() {
        long[][] matrix = {
                {1, 1, 2, 2, 3, 3, 4, 4},
                {1, 1, 2, 2, 3, 3, 4, 4},
                {5, 5, 6, 6, 7, 7, 8, 8},
                {5, 5, 6, 6, 7, 7, 8, 8},
                {9, 9, 10, 10, 11, 11, 12, 12},
                {9, 9, 10, 10, 11, 11, 12, 12},
                {13, 13, 14, 14, 15, 15, 16, 16},
                {13, 13, 14, 14, 15, 15, 16, 16}};
        long[][] subTest = FirstLevelEncoder.subSample420(matrix);
        long[][] resTest = FirstLevelDecoder.resizeSubsampledBlocks(subTest);

        System.out.println(
                java.util.Arrays.deepEquals(matrix, resTest));
    }


    private static void testOrderSub(int nr) {
        List<long[][]> testList = new ArrayList<long[][]>();
        for (int i = 0; i < nr; i++) {
            long[][] matrix = {
                    {i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3},
                    {i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3},
                    {i + 4, i + 4, i + 5, i + 5, i + 6, i + 6, i + 7, i + 7},
                    {i + 4, i + 4, i + 5, i + 5, i + 6, i + 6, i + 7, i + 7},
                    {i + 8, i + 8, i + 9, i + 9, i + 10, i + 10, i + 11, i + 11},
                    {i + 8, i + 8, i + 9, i + 9, i + 10, i + 10, i + 11, i + 11},
                    {i + 12, i + 12, i + 13, i + 13, i + 14, i + 14, i + 15, i + 15},
                    {i + 12, i + 12, i + 13, i + 13, i + 14, i + 14, i + 15, i + 15}};
            testList.add(matrix);
        }
        List<long[][]> subTest = FirstLevelEncoder.subSampleBlocks(testList);
        List<long[][]> sizeTest = FirstLevelDecoder.reziseSubsampledBlockList(subTest);
        int counter = 0;
        List<List<String[]>> diferente = new ArrayList<List<String[]>>();
        for (int i = 0; i < nr; i++) {
            if (Arrays.deepEquals(sizeTest.get(i), testList.get(i))) {
                System.out.println("true");
                counter++;
            } else {
                System.out.println("false");
                diferente.add(OtherUtils.checkForDifferences(testList.get(i), sizeTest.get(i)));
            }
        }
        if (counter == nr)
            System.out.println("Lists are equal");
        else
            System.out.println("Lists are not equal");
    }


    private static void testOrderSub2(int nr) {
        List<long[][]> testList = new ArrayList<long[][]>();
        for (int i = 0; i < nr; i++) {
            long[][] matrix = {
                    {i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3},
                    {i, i, i + 1, i + 1, i + 2, i + 2, i + 3, i + 3},
                    {i + 4, i + 4, i + 5, i + 5, i + 6, i + 6, i + 7, i + 7},
                    {i + 4, i + 4, i + 5, i + 5, i + 6, i + 6, i + 7, i + 7},
                    {i + 8, i + 8, i + 9, i + 9, i + 10, i + 10, i + 11, i + 11},
                    {i + 8, i + 8, i + 9, i + 9, i + 10, i + 10, i + 11, i + 11},
                    {i + 12, i + 12, i + 13, i + 13, i + 14, i + 14, i + 15, i + 15},
                    {i + 12, i + 12, i + 13, i + 13, i + 14, i + 14, i + 15, i + 15}};
            testList.add(matrix);
        }
        long[][] testBlocks = FirstLevelDecoder.blocksToMatrix(testList, 800, 600);
        List<long[][]> testtomat = FirstLevelEncoder.matrixToBlocks(testBlocks, 8);
        List<long[][]> subTest = FirstLevelEncoder.subSampleBlocks(testtomat);
        List<long[][]> sizeTest = FirstLevelDecoder.reziseSubsampledBlockList(subTest);
        int counter = 0;
        List<List<String[]>> diferente = new ArrayList<List<String[]>>();
        for (int i = 0; i < nr; i++) {
            if (Arrays.deepEquals(sizeTest.get(i), testList.get(i))) {
                System.out.println("true");
                counter++;
            } else {
                System.out.println("false");
                diferente.add(OtherUtils.checkForDifferences(testList.get(i), sizeTest.get(i)));
            }
        }
        if (counter == nr)
            System.out.println("Lists are equal");
        else
            System.out.println("Lists are not equal");
    }


    private static void compareFiles(){
        System.out.println("Comparing file\n");
        String fileName = "src/main/resources/4deeptest.ppm";
        List<Integer> image = FileUtils.readPpm(fileName);

        String fileName2 = "src/main/resources/1conversie.ppm";
        List<Integer> image2 = FileUtils.readPpm(fileName2);

        System.out.println(image2.equals(image));
    }


    private static long[][] deepTest2(long[][] originalMatrix){
        long[][] testmatrix = new long[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                testmatrix[i][j] = originalMatrix[i][j];
            }
        }
        return testmatrix;
    }


    public static List<long[][]> deepTest1(List<long[][]> originalBlocks){
        List <long[][]> DequantiedBlocks = new ArrayList<long[][]>();
        for (int i = 0; i < originalBlocks.size(); i++){
            DequantiedBlocks.add(deepTest2(originalBlocks.get(i)));
        }
        return DequantiedBlocks;
    }

}
