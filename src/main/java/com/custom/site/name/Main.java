package com.custom.site.name;

import java.util.List;

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
      String fileName = "src/main/resources/image.ppm";
      List<Integer> image = FileUtils.readPpm(fileName);
      FirstLevelEncoderDecoder firstLevelEncoderDecoder = new FirstLevelEncoderDecoder();
      List<RGB> rgbImage = firstLevelEncoderDecoder.convertListToRgb(image);
      List<YUV> yuvImage = firstLevelEncoderDecoder.convertRGBImageToYUV(rgbImage);
      System.out.println(yuvImage.size());
   }
   
}
