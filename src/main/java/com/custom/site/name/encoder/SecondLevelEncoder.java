package com.custom.site.name.encoder;

import java.util.ArrayList;
import java.util.List;


public class SecondLevelEncoder {
    private static final int N = 8;
    private static double[] c = {1 / Math.sqrt(2.0), 1, 1, 1, 1, 1, 1, 1};
    private static long[][] quantizationMatrix = {
            {6,  4,  4,  6,  10, 16, 20, 24},
            {5,  5,  6,  8,  10, 23, 24, 22},
            {6,  5,  6,  10, 16, 23, 28, 22},
            {6,  7,  9,  12, 20, 35, 32, 25},
            {7,  9,  15, 22, 27, 44, 41, 31},
            {10, 14, 22, 26, 32, 42, 45, 37},
            {20, 26, 31, 35, 41, 48, 48, 40},
            {29, 37, 38, 39, 45, 40, 41, 40}};


    private static long[][] blocksToDCT(long[][] f) {
        long[][] F = new long[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) *
                                Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * f[i][j];
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = (long) sum;
            }
        }
        return F;
    }


    private static long[][] subThe128(long[][] originalMatrix, int nr){
        long[][] addedMatrix = new long[8][8];
        long[][] DCTedMatrix;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                addedMatrix[i][j] = originalMatrix[i][j] - nr;
            }
        }
        DCTedMatrix = blocksToDCT(addedMatrix);
        return DCTedMatrix;
    }


    public static List<long[][]> listToDCT(List<long[][]> originalBlocks){
        List <long[][]> DCTedBlocks = new ArrayList<long[][]>();
        for (int i = 0; i < originalBlocks.size(); i++){
            DCTedBlocks.add(subThe128(originalBlocks.get(i), 0));
        }
        return DCTedBlocks;
    }


    private static long[][] blocksQuanti(long[][] originalMatrix){
        long[][] QuantiedMatrix = new long[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                QuantiedMatrix[i][j] = originalMatrix[i][j] / quantizationMatrix[i][j];
            }
        }
        return QuantiedMatrix;
    }


    public static List<long[][]> listToQuanti(List<long[][]> originalBlocks){
        List <long[][]> QuantiedBlocks = new ArrayList<long[][]>();
        for (int i = 0; i < originalBlocks.size(); i++){
            QuantiedBlocks.add(blocksQuanti(originalBlocks.get(i)));
        }
        return QuantiedBlocks;
    }
}
