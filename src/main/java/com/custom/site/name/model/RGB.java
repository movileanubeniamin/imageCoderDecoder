package com.custom.site.name.model;

/**
 * @author bmovileanu
 *
 */
public class RGB {
   private long r;
   private long g;
   private long b;
   
   public RGB() {
      super();
   }
   
   /**
    * @param r
    * @param g
    * @param b
    */
   public RGB(long r, long g, long b) {
      super();
      this.r = r;
      this.g = g;
      this.b = b;
   }
   
   /**
    * @return the r
    */
   public long getR() {
      return r;
   }
   
   /**
    * @param r
    *           the r to set
    */
   public void setR(long r) {
      this.r = r;
   }
   
   /**
    * @return the g
    */
   public long getG() {
      return g;
   }
   
   /**
    * @param g
    *           the g to set
    */
   public void setG(long g) {
      this.g = g;
   }
   
   /**
    * @return the b
    */
   public long getB() {
      return b;
   }
   
   /**
    * @param b
    *           the b to set
    */
   public void setB(long b) {
      this.b = b;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "RGB [" + r + ", " + g + ", " + b + "]";
   }
   
}
