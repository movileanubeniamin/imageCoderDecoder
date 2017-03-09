package com.custom.site.name.model;

/**
 * @author bmovileanu
 *
 */
public class YUV {
   private double y;
   private double u;
   private double v;
   
   public YUV() {
      super();
   }
   
   /**
    * @param y
    * @param u
    * @param v
    */
   public YUV(double y, double u, double v) {
      super();
      this.y = y;
      this.u = u;
      this.v = v;
   }
   
   /**
    * @return the y
    */
   public double getY() {
      return y;
   }
   
   /**
    * @param y
    *           the y to set
    */
   public void setY(double y) {
      this.y = y;
   }
   
   /**
    * @return the u
    */
   public double getU() {
      return u;
   }
   
   /**
    * @param u
    *           the u to set
    */
   public void setU(double u) {
      this.u = u;
   }
   
   /**
    * @return the v
    */
   public double getV() {
      return v;
   }
   
   /**
    * @param v
    *           the v to set
    */
   public void setV(double v) {
      this.v = v;
   }
   
}
