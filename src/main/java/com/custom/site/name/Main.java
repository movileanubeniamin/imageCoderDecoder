package com.custom.site.name;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.custom.site.name.decoder.FirstLevelDecoder;
import com.custom.site.name.encoder.FirstLevelEncoder;
import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;
import com.custom.site.name.utils.FileUtils;


public class Main {


    public static void main(String[] args) {
        start();


        //Tests
//        testSubsample();
//        testOrderSub(7500);
//        testareMatBlo(800, 600, 8);
//        testareMatBlo(8, 8, 4);
//        testareMatBlo(4, 4, 2);
    }

    private static void start(){
        //First Level encoder
        //Reading file
        String fileName = "src/main/resources/image.ppm";
        List<Integer> image = FileUtils.readPpm(fileName);

        //Getting image info (width, height, RGB list)
        Integer width = image.get(0);
        Integer height = image.get(1);
        List<RGB> rgbImage = FirstLevelEncoder.convertListToRgb(image);

        //RGB to YUV conversion
        List<YUV> yuvImage = FirstLevelEncoder.convertRGBImageToYUV(rgbImage);
//      System.out.println(yuvImage.size());

        //YUV list to YUV matrix conversion
        Map<String, long[][]> imageMatrix = FirstLevelEncoder.convertListToMatrix(yuvImage, width, height);

        //YUV matrix to 3 separate lists of 8x8 matrices conversion
        List<long[][]> yBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("y"), 8);
        List<long[][]> uBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("u"), 8);
        List<long[][]> vBlocks = FirstLevelEncoder.matrixToBlocks(imageMatrix.get("v"), 8);

        //Downsample the U and V matrix
        List<long[][]> uDownBlocks = FirstLevelEncoder.subSampleBlocks(uBlocks);
        List<long[][]> vDownBlocks = FirstLevelEncoder.subSampleBlocks(vBlocks);


        //========================================================================================================
        //First Level decoder
        //Resize the subsampled U and V matrix
        List<long[][]> resUSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(uDownBlocks);
        List<long[][]> resVSubBlocks = FirstLevelDecoder.reziseSubsampledBlockList(vDownBlocks);

        //Reconstruct the matrix from block list
        long[][] recreatedMatrixY = FirstLevelDecoder.blocksToMatrix(yBlocks, width, height);
        long[][] recreatedMatrixU = FirstLevelDecoder.blocksToMatrix(resUSubBlocks, width, height);
        long[][] recreatedMatrixV = FirstLevelDecoder.blocksToMatrix(resVSubBlocks, width, height);

        //Create YUV List from Matrix
        List<YUV> yuvNewImage = FirstLevelDecoder.convertMatrixToYUVList(recreatedMatrixY, recreatedMatrixU, recreatedMatrixV, width, height);

        //Convert YUV List to RGB List
        List<RGB> rgbNewImage = FirstLevelDecoder.convertRGBImageToYUV(yuvNewImage);

        //Convert RGB List into Image List and write it to file
        List<Integer> newImage = FirstLevelDecoder.convertRgbToList(rgbNewImage);
        FileUtils.writePpm("src/main/resources/newImage.ppm", newImage);
    }

    private static void testareMatBlo(int width, int height, int blockdim){
        long[][] textMatrix = new long[width][height];
        int val = 1;
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                textMatrix[i][j] = val;
                val++;
            }
        }
        List<long[][]> blocks = FirstLevelEncoder.matrixToBlocks(textMatrix, blockdim);
        long[][] recreatedMatrix = FirstLevelDecoder.blocksToMatrix(blocks, width, height);
        if (Arrays.deepEquals(textMatrix, recreatedMatrix)){
            System.out.println("YES");
        }
        else {
            System.out.println("NO");
        }
    }


    private static void testSubsample(){
        long[][] matrix = {
                {1,  1,  2,  2,  3,  3,  4,  4},
                {1,  1,  2,  2,  3,  3,  4,  4},
                {5,  5,  6,  6,  7,  7,  8,  8},
                {5,  5,  6,  6,  7,  7,  8,  8},
                {9,  9,  10, 10, 11, 11, 12, 12},
                {9,  9,  10, 10, 11, 11, 12, 12},
                {13, 13, 14, 14, 15, 15, 16, 16},
                {13, 13, 14, 14, 15, 15, 16, 16}};
        long[][] subTest = FirstLevelEncoder.subSample420(matrix);
        long[][] resTest = FirstLevelDecoder.resizeSubsampledBlocks(subTest);

        System.out.println(
                java.util.Arrays.deepEquals(matrix, resTest)
        );
    }


    private static void testOrderSub(int nr){
        List<long[][]> testList = new ArrayList<long[][]>();
        for (int i = 0; i < nr; i++){
            long[][] matrix = {
                    {i,     i,     i+1,   i+1,   i+2,   i+2,   i+3,   i+3},
                    {i,     i,     i+1,   i+1,   i+2,   i+2,   i+3,   i+3},
                    {i+4,   i+4,   i+5,   i+5,   i+6,   i+6,   i+7,   i+7},
                    {i+4,   i+4,   i+5,   i+5,   i+6,   i+6,   i+7,   i+7},
                    {i+8,   i+8,   i+9,   i+9,   i+10,  i+10,  i+11,  i+11},
                    {i+8,   i+8,   i+9,   i+9,   i+10,  i+10,  i+11,  i+11},
                    {i+12,  i+12,  i+13,  i+13,  i+14,  i+14,  i+15,  i+15},
                    {i+12,  i+12,  i+13,  i+13,  i+14,  i+14,  i+15,  i+15}};
            testList.add(matrix);
        }

        List<long[][]> subTest = FirstLevelEncoder.subSampleBlocks(testList);
        List<long[][]> sizeTest = FirstLevelDecoder.reziseSubsampledBlockList(subTest);
        for (int i = 0; i < nr; i++) {
            if (!(Arrays.equals(testList.get(i), sizeTest.get(i)))){
                System.out.println("false");
            }
        }
    }

}