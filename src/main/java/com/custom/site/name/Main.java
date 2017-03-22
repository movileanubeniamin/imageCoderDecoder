package com.custom.site.name;

import java.util.List;
import java.util.Map;

import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;

/**
 * @author bmovileanu
 *
 */
public class Main {
   
   /**
    * @param args
    */
   public static void main(String[] args) {
      //Reading file
      String fileName = "src/main/resources/image.ppm";
      List<Integer> image = FileUtils.readPpm(fileName);

      //Getting image info (width, height, RGB list)
      Integer width = image.get(0);
      Integer height = image.get(1);
      FirstLevelEncoderDecoder firstLevelEncoderDecoder = new FirstLevelEncoderDecoder();
      List<RGB> rgbImage = firstLevelEncoderDecoder.convertListToRgb(image);

      //RGB to YUV conversion
      List<YUV> yuvImage = firstLevelEncoderDecoder.convertRGBImageToYUV(rgbImage);
//      System.out.println(yuvImage.size());

      //YUV list to YUV matrix conversion
      Map<String, long[][]> imageMatrix = firstLevelEncoderDecoder.convertYuvToMatrix(yuvImage, width, height);

      //YUV matrix to 3 separate lists of 8x8 matrices conversion
      List<long[][]> yBlocks = firstLevelEncoderDecoder.matrixToBlocks(imageMatrix.get("y"), 8, width, height);
      List<long[][]> uBlocks = firstLevelEncoderDecoder.matrixToBlocks(imageMatrix.get("u"), 8, width, height);
      List<long[][]> vBlocks = firstLevelEncoderDecoder.matrixToBlocks(imageMatrix.get("v"), 8, width, height);
//      System.out.println(yBlocks.size());
//      System.out.println(uBlocks.size());
//      System.out.println(vBlocks.size());


   }
   
}
