package com.custom.site.name.encoder;

import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirstLevelEncoder {

    public static List<RGB> convertListToRgb(List<Integer> image) {
        List<RGB> rgbList = new ArrayList<RGB>();
        for (int index = 3; index < image.size();)
            rgbList.add(new RGB(image.get(index++), image.get(index++), image.get(index++)));
        return rgbList;
    }


    public static List<YUV> convertRGBImageToYUV(List<RGB> rgbImage) {
        List<YUV> yuvImage = new ArrayList<YUV>();
        for (RGB rgb : rgbImage)
            yuvImage.add(convertRgbToYuv(rgb));
        return yuvImage;
    }


    private static YUV convertRgbToYuv(RGB rgb) {
        YUV yuv = new YUV();
//        yuv.setY(Math.round(0.299 * rgb.getR() + 0.587 * rgb.getG() + 0.114 * rgb.getB()));
//        yuv.setU(Math.round(-0.147 * rgb.getR() - 0.289 * rgb.getG() + 0.437 * rgb.getB()));
//        yuv.setV(Math.round(0.615 * rgb.getR() - 0.515 * rgb.getG() - 0.100 * rgb.getB()));
        yuv.setY(Math.round(0.299 * rgb.getR() + 0.587 * rgb.getG() + 0.114 * rgb.getB()));
        yuv.setU(Math.round(128 - (-0.168736 * rgb.getR() - 0.331264 * rgb.getG() + 0.5 * rgb.getB())));
        yuv.setV(Math.round(128 + (0.5 * rgb.getR() - 0.418688 * rgb.getG() - 0.081312 * rgb.getB())));
        return yuv;
    }


    public static Map<String, long[][]> convertListToMatrix(List<YUV> yuvList, int width, int height) {
        Map<String, long[][]> imageMatrix = new HashMap<String, long[][]>();
        long[][] yMatrix = new long[height][width];
        long[][] uMatrix = new long[height][width];
        long[][] vMatrix = new long[height][width];
        int nr = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                YUV yuvPixel = yuvList.get(nr);
                yMatrix[i][j] = yuvPixel.getY();
                uMatrix[i][j] = yuvPixel.getU();
                vMatrix[i][j] = yuvPixel.getV();
                nr++;
            }
        }
        imageMatrix.put("y", yMatrix);
        imageMatrix.put("u", uMatrix);
        imageMatrix.put("v", vMatrix);
        return imageMatrix;
    }


    public static List<long[][]> matrixToBlocks(long[][] yuvMatrix, int blockDimension){
        //Check matrix and block dimension match
        if( yuvMatrix.length == 0 || yuvMatrix.length % blockDimension != 0
                || yuvMatrix[0].length == 0 || yuvMatrix[0].length % blockDimension != 0 ) {
            throw new IllegalArgumentException("Dimensiunile matricilor sunt incorecte");
        }
        List<long[][]> blocks = new ArrayList<long[][]>();
        //Iterate over the blocks in row-major order (down first, then right)
        for( int c = 0; c < yuvMatrix.length; c += blockDimension ) {
            for( int r = 0; r < yuvMatrix[c].length; r += blockDimension ) {
                long[][] subBlock = new long[blockDimension][blockDimension];
                //Iterate over the block in row-major order
                for(int bc = 0; bc < blockDimension; bc++ ) {
                    for(int br = 0; br < blockDimension; br++ ) {
                        subBlock[bc][br]=yuvMatrix[c+bc][r+br];
                    }
                }
                blocks.add(subBlock);
            }
        }
        return blocks;
    }


    public static long[][] subSample420(long[][] originalBlocks){
        long[][] downsampledBlocks = new long[4][4];
        long[] mediePixel = new long[16];
        int nr = 0;
        for (int i = 0; i < 8; i += 2){
            for (int j = 0; j < 8; j += 2){
                mediePixel[nr] = Math.round((double)(originalBlocks[i][j] + originalBlocks[i][j+1] + originalBlocks[i+1][j] + originalBlocks[i+1][j+1]) / 4);
                nr++;
            }
        }
        nr = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                downsampledBlocks[i][j] = mediePixel[nr];
                nr++;
            }
        }
        return downsampledBlocks;
    }


    public static List<long[][]> subSampleBlocks(List<long[][]> originalBlockList){
        List<long[][]> downsampledBlockList = new ArrayList<long[][]>();
        for (long[][] anOriginalBlockList : originalBlockList) {
            downsampledBlockList.add(subSample420(anOriginalBlockList));
        }
        return downsampledBlockList;
    }

}