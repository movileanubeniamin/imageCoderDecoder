/**
 * 
 */
package com.custom.site.name.decoder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ThirdLevelDecoder {
   
   public static long[][] decodeVector(List<Long> vector) {
      long[][] block = new long[8][8];
      List<Integer> posx = new ArrayList<Integer>();
      List<Integer> posy = new ArrayList<Integer>();
      
      // build of posx
      for (int i = 0; i < block.length; i += 2) {
         int j;
         for (j = 0; j <= i; j++)
            posx.add(j);
         for (j = i - 1; j >= 0; j--)
            posx.add(j);
      }
      for (int i = 0; i < block.length; i++)
         posx.add(i);
      for (int i = 1; i < block.length; i += 2) {
         int j;
         for (j = block.length - 1; j >= i; j--)
            posx.add(j);
         for (j = i + 1; j < block.length; j++)
            posx.add(j);
      }
      
      // build of posy
      for (int i = 1; i < block.length; i += 2) {
         int j;
         for (j = 0; j <= i; j++)
            posy.add(j);
         for (j = i - 1; j >= 0; j--)
            posy.add(j);
      }
      for (int i = 1; i < block.length; i++)
         posy.add(i);
      for (int i = 2; i < block.length; i += 2) {
         int j;
         for (j = block.length - 1; j >= i; j--)
            posy.add(j);
         for (j = i + 1; j < block.length; j++)
            posy.add(j);
      }
      
      for (int i = 0; i < block.length; i++)
         for (int j = 0; j < block.length; j++)
            block[i][j] = 0;
         
      int temp = 1;
      block[0][0] = vector.get(1);
      for (int i = 1;; i++) {
         if (vector.size() <= 6)
            break;
         // int tempI = i;
         // if ((tempI * 3 + 1) > (vector.size() - 1))
         // break;
         
         i += vector.get(3 * temp - 1);
         // while (vector.get(tempI * 3 - 1) > 0 && (i * 3 + 1) < posx.size()) {
         // block[posx.get(i)][posy.get(i)] = 0;
         // vector.set(tempI * 3 - 1, vector.get(tempI * 3 - 1) - 1);
         // i++;
         // }
         if (i == 23)
//            System.out.println((i * 3 + 1) + "*" + vector.size());
         if (i == 23)
//            System.out.println((i * 3 + 1) + "?" + vector.size());
         try {
            block[posx.get(i)][posy.get(i)] = vector.get(3 * temp + 1);
         } catch (IndexOutOfBoundsException exception) {
            System.out.println("asd");
         }
         temp += 1;
         if ((temp * 3 + 1) > vector.size() - 1)
            break;
      }
      return block;
   }
   
   public static List<long[][]> zigZagToList(List<List<Long>> zigZagList) {
      List<long[][]> unzigZagList = new ArrayList<long[][]>();
      for (int i = 0; i < zigZagList.size(); i++) {
         unzigZagList.add(decodeVector(zigZagList.get(i)));
      }
      return unzigZagList;
   }
   
}
