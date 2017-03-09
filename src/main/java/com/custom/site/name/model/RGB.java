package com.custom.site.name.model;

/**
 * @author bmovileanu
 *
 */
public class RGB {
   private double r;
   private double g;
   private double b;
   
   public RGB() {
      super();
   }
   
   /**
    * @param r
    * @param g
    * @param b
    */
   public RGB(double r, double g, double b) {
      super();
      this.r = r;
      this.g = g;
      this.b = b;
   }
   
   /**
    * @return the r
    */
   public double getR() {
      return r;
   }
   
   /**
    * @param r
    *           the r to set
    */
   public void setR(double r) {
      this.r = r;
   }
   
   /**
    * @return the g
    */
   public double getG() {
      return g;
   }
   
   /**
    * @param g
    *           the g to set
    */
   public void setG(double g) {
      this.g = g;
   }
   
   /**
    * @return the b
    */
   public double getB() {
      return b;
   }
   
   /**
    * @param b
    *           the b to set
    */
   public void setB(double b) {
      this.b = b;
   }
   
}
