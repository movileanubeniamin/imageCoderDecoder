package com.custom.site.name.decoder;

import java.util.ArrayList;
import java.util.List;


public class SecondLevelDecoder {
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


    private static long[][] blocksToIDCT(long[][] F) {
        long[][] f = new long[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double sum = 0.0;
                for (int u = 0; u < N; u++) {
                    for (int v = 0; v < N; v++) {
                        sum += (c[u] * c[v]) / 4.0 * Math.cos(((2 * i + 1)/(2.0 * N)) * u * Math.PI) *
                                Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * F[u][v];
                    }
                }
                f[i][j]=Math.round(sum);
            }
        }
        return f;
    }


    private static long[][] addThe128(long[][] originalMatrix, int nr){
        long[][] subbedMatrix = new long[8][8];
        long[][] IDCTedMatrix = blocksToIDCT(originalMatrix);
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                subbedMatrix[i][j] = IDCTedMatrix[i][j] + nr;
            }
        }
        return subbedMatrix;
    }


    public static List<long[][]> listToIDCT(List<long[][]> originalBlocks){
        List <long[][]> IDCTedBlocks = new ArrayList<long[][]>();
        for (int i = 0; i < originalBlocks.size(); i++){
            IDCTedBlocks.add(addThe128(originalBlocks.get(i), 128));
        }
        return IDCTedBlocks;
    }


    private static long[][] blocksDequanti(long[][] originalMatrix){
        long[][] DequantiedMatrix = new long[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                DequantiedMatrix[i][j] = originalMatrix[i][j] * quantizationMatrix[i][j];
            }
        }
        return DequantiedMatrix;
    }


    public static List<long[][]> listToDequanti(List<long[][]> originalBlocks){
        List <long[][]> DequantiedBlocks = new ArrayList<long[][]>();
        for (int i = 0; i < originalBlocks.size(); i++){
            DequantiedBlocks.add(blocksDequanti(originalBlocks.get(i)));
        }
        return DequantiedBlocks;
    }
}
