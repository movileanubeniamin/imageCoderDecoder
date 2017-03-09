package com.custom.site.name;

import com.custom.site.name.model.RGB;
import com.custom.site.name.model.YUV;

/**
 * @author bmovileanu
 *
 */
public class FirstLevelEncoderDecoder {
   private static final double Y_R = 0.299;
   private static final double Y_G = 0.587;
   private static final double Y_B = 0.114;
   
   private static final double U_R = 0.1687;
   private static final double U_G = 0.3312;
   private static final double U_B = 0.5;
   
   private static final double V_R = 0.5;
   private static final double V_G = 0.4186;
   private static final double V_B = 0.0813;
   
   public YUV convertRgbToYuv(RGB rgb) {
      YUV yuv = new YUV();
      yuv.setY(rgb.getR() * Y_R + rgb.getG() * Y_G + rgb.getB() * Y_B);
      yuv.setU(rgb.getR() * U_R + rgb.getG() * U_G + rgb.getB() * U_B);
      yuv.setV(rgb.getR() * V_R + rgb.getG() * V_G + rgb.getB() * V_B);
      return yuv;
   }
   
}
