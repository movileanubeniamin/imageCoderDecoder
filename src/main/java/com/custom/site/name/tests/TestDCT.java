//package com.custom.site.name.tests;
//
//import java.util.Random;
//
//public class TestDCT {
//    private static final int N = 8;
//    private static double[][] f = new double[N][N];
//    private static Random generator = new Random();
//
//    public static void main(String[] args) {
//        // Generate random integers between 0 and 255
//        int value;
//        for (int x=0;x<N;x++) {
//            for (int y=0;y<N;y++) {
//                value = generator.nextInt(255);
//                f[x][y] = value;
//                System.out.println(f[x][y]+" => f["+x+"]["+y+"]");
//            }
//        }
//
//        DCT dctApplied = new DCT();
//        double[][] F = dctApplied.applyDCT(f);
//        System.out.println("From f to F");
//        System.out.println("-----------");
//        for (int x=0;x<N;x++) {
//            for (int y=0;y<N;y++) {
//                try {
//                    System.out.println(F[x][y]+" => F["+x+"]["+y+"]");
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//        }
//
//        double f[][] = dctApplied.applyIDCT(F);
//        System.out.println("Back to f");
//        System.out.println("---------");
//        for (int y=0;y<N;y++) {
//            for (int z=0;z<N;z++) {
//                System.out.println(f[y][z]+" => f["+y+"]["+z+"]");
//            }
//        }
//    }
//}