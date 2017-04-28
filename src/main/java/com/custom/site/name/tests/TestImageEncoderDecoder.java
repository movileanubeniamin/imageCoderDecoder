package com.custom.site.name.tests;

import com.custom.site.name.decoder.FirstLevelDecoder;
import com.custom.site.name.encoder.FirstLevelEncoder;
import com.custom.site.name.utils.FileUtils;
import com.custom.site.name.utils.OtherUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestImageEncoderDecoder {


    public static void testareMatBlo(int width, int height, int blockdim) {
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


    public static void testSubsample() {
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


    public static void testOrderSub(int nr) {
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


    public static void testOrderSub2(int nr) {
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


    public static void compareFiles(){
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
