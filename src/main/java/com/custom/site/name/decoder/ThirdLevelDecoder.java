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
   
   public long[][] decodeVector(List<Long> vector) {
      long[] blockAsArray = new long[64];
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
      
      // creating the vector
      for (int i = 0; i < posx.size(); i++)
         vector.add(block[posx.get(i)][posy.get(i)]);
      
      for (int i = 0; i < 64; i++)
         blockAsArray[i] = 0;
      
      for (int i = 0; i < block.length; i++)
         for (int j = 0; j < block.length; j++)
            block[i][j] = 0;
         
      block[0][0] = vector.get(1);
      for (int i = 1; i < posx.size(); i++) {
         int tempI = i;
         if ((tempI * 3 + 1) > vector.size())
            break;
         while (vector.get(tempI * 3 - 1) > 0 && (i * 3 + 1) < posx.size()) {
            block[posx.get(i)][posy.get(i)] = 0;
            vector.set(tempI * 3 - 1, vector.get(tempI * 3 - 1) - 1);
            i++;
         }
         block[posx.get(i)][posy.get(i)] = vector.get(3 * i + 1);
      }
      return block;
   }
   
}
