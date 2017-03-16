package com.custom.site.name.model;

/**
 * @author bmovileanu
 *
 */
public class YUV {
   private long y;
   private long u;
   private long v;
   
   public YUV() {
      super();
   }
   
   /**
    * @param y
    * @param u
    * @param v
    */
   public YUV(long y, long u, long v) {
      super();
      this.y = y;
      this.u = u;
      this.v = v;
   }
   
   /**
    * @return the y
    */
   public long getY() {
      return y;
   }
   
   /**
    * @param y
    *           the y to set
    */
   public void setY(long y) {
      this.y = y;
   }
   
   /**
    * @return the u
    */
   public long getU() {
      return u;
   }
   
   /**
    * @param u
    *           the u to set
    */
   public void setU(long u) {
      this.u = u;
   }
   
   /**
    * @return the v
    */
   public long getV() {
      return v;
   }
   
   /**
    * @param v
    *           the v to set
    */
   public void setV(long v) {
      this.v = v;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "YUV [" + y + ", " + u + ", " + v + "]";
   }
   
}
