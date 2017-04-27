package com.custom.site.name.decoder;

import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;
import com.custom.site.name.utils.OtherUtils;

import java.util.ArrayList;
import java.util.List;


public class FirstLevelDecoder {

    public static long[][] blocksToMatrix(List<long[][]> blocks, int width, int height ) {
        long[][] yuvMatrix = new long[width][height];
        int c = 0;
        int r = 0;
        for( long[][] block : blocks ) {
            int blockWidth = block.length;
            int blockHeight = block[0].length;

            for( int bc = 0; bc < block.length; bc++ ) {
                for( int br = 0; br < block[bc].length; br++ ) {
                    yuvMatrix[c + bc][r + br] = block[bc][br];
                }
            }
            //calculate the next offset into the matrix
            //The blocks where created in row-major order so we need to advance the offset in the same way
            r += blockHeight;
            if( r >= height ) {
                r = 0;
                c += blockWidth;
            }
        }
        return yuvMatrix;
    }


    public static long[][] resizeSubsampledBlocks(long[][] subBlocks){
        long[][] resizedBlocks = new long[8][8];
        int nr = 0;
        long[] pixel = new long[16];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                pixel[nr] = subBlocks[i][j];
                nr++;
            }
        }
        nr = 0;
        for (int i = 0; i < 8; i += 2){
            for (int j = 0; j < 8; j += 2){
                resizedBlocks[i][j] = pixel[nr];
                resizedBlocks[i][j+1] = pixel[nr];
                resizedBlocks[i+1][j] = pixel[nr];
                resizedBlocks[i+1][j+1] = pixel[nr];
                nr++;
            }
        }
        return resizedBlocks;
    }


    public static List<long[][]> reziseSubsampledBlockList(List<long[][]> subBlocks){
        List<long[][]> rezisedBlocks = new ArrayList<long[][]>();
        for (long[][] subBlock : subBlocks) {
            rezisedBlocks.add(resizeSubsampledBlocks(subBlock));
        }
        return rezisedBlocks;
    }


    public static List<YUV> convertMatrixToYUVList(long[][] yMatrix, long[][] uMatrix, long[][] vMatrix, int width, int height) {
        List<YUV> yuvList = new ArrayList<YUV>();
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                yuvList.add(new YUV(yMatrix[i][j], uMatrix[i][j], vMatrix[i][j]));
        return yuvList;
    }


    private static RGB convertYuvToRgb(YUV yuv) {
        RGB rgb = new RGB();
//        rgb.setR(Math.round(OtherUtils.clamp((float) (yuv.getY() + 1.140 * yuv.getV()), 0, 255)));
//        rgb.setG(Math.round(OtherUtils.clamp((float) (yuv.getY() - 0.394 * yuv.getU() - 0.581 * yuv.getV()), 0, 255)));
//        rgb.setB(Math.round(OtherUtils.clamp((float) (yuv.getY() + 2.028 * yuv.getU()), 0, 255)));
        rgb.setR(Math.round(OtherUtils.clamp((float) (yuv.getY() + 1.402 * (yuv.getV() - 128)), 0, 255)));
        rgb.setG(Math.round(OtherUtils.clamp((float) (yuv.getY() - 0.344136 * (- yuv.getU() + 128) - 0.714136 * (yuv.getV() - 128)), 0, 255)));
        rgb.setB(Math.round(OtherUtils.clamp((float) (yuv.getY() + 1.772 * (- yuv.getU() + 128)), 0, 255)));
        return rgb;
    }


    public static List<RGB> convertRGBImageToYUV(List<YUV> yuvImage) {
        List<RGB> rgbImage = new ArrayList<RGB>();
        for (YUV yuv : yuvImage)
            rgbImage.add(convertYuvToRgb(yuv));
        return rgbImage;
    }


    public static List<Integer> convertRgbToList(List<RGB> rgbImage) {
        List<Integer> imageList = new ArrayList<Integer>();
        for (int index = 0; index < rgbImage.size(); index++){
            imageList.add((int)rgbImage.get(index).getR());
            imageList.add((int)rgbImage.get(index).getG());
            imageList.add((int)rgbImage.get(index).getB());
        }
        return imageList;
    }


}