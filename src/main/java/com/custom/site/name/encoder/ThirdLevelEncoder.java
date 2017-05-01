/**
 * 
 */
package com.custom.site.name.encoder;

import java.util.ArrayList;
import java.util.List;

import com.custom.site.name.utils.OtherUtils;

public class ThirdLevelEncoder {
   
   private List<Long> zigZagVector(long[][] block) {
      List<Long> vector = new ArrayList<Long>();
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
      
      return vector;
   }
   
   public List<Long> encodeBlock(long[][] block) {
      List<Long> encodedBlock = new ArrayList<Long>();
      List<Long> vector = zigZagVector(block);
      encodedBlock.add(OtherUtils.calcualteSize(vector.get(0)));
      encodedBlock.add(vector.get(0));
      long counter = 0;
      for (int i = 1; i < vector.size(); i++) {
         if (vector.get(i) == 0 && i != vector.size() - 1) {
            counter++;
            continue;
         }
         if (i == vector.size() - 1) {
            encodedBlock.add(0l);
            encodedBlock.add(0l);
            break;
         }
         encodedBlock.add(counter);
         encodedBlock.add(OtherUtils.calcualteSize(vector.get(i)));
         encodedBlock.add(vector.get(i));
         counter = 0;
      }
      return encodedBlock;
   }
   
   public static void main(String[] args) {
      long[][] block = new long[8][8];
      block[0][0] = 150;
      block[0][1] = 80;
      block[0][2] = 20;
      block[0][3] = 4;
      block[0][4] = 1;
      block[0][5] = 0;
      block[0][6] = 0;
      block[0][7] = 0;
      block[1][0] = 92;
      block[1][1] = 75;
      block[1][2] = 18;
      block[1][3] = 3;
      block[1][4] = 1;
      block[1][5] = 0;
      block[1][6] = 0;
      block[1][7] = 0;
      block[2][0] = 26;
      block[2][1] = 19;
      block[2][2] = 13;
      block[2][3] = 2;
      block[2][4] = 1;
      block[2][5] = 0;
      block[2][6] = 0;
      block[2][7] = 0;
      block[3][0] = 3;
      block[3][1] = 2;
      block[3][2] = 2;
      block[3][3] = 1;
      block[3][4] = 0;
      block[3][5] = 0;
      block[3][6] = 0;
      block[3][7] = 0;
      block[4][0] = 1;
      block[4][1] = 0;
      block[4][2] = 0;
      block[4][3] = 0;
      block[4][4] = 0;
      block[4][5] = 0;
      block[4][6] = 0;
      block[4][7] = 0;
      block[5][0] = 0;
      block[5][1] = 0;
      block[5][2] = 0;
      block[5][3] = 0;
      block[5][4] = 0;
      block[5][5] = 0;
      block[5][6] = 0;
      block[5][7] = 0;
      block[6][0] = 0;
      block[6][1] = 0;
      block[6][2] = 0;
      block[6][3] = 0;
      block[6][4] = 0;
      block[6][5] = 0;
      block[6][6] = 0;
      block[6][7] = 0;
      block[7][0] = 0;
      block[7][1] = 0;
      block[7][2] = 0;
      block[7][3] = 0;
      block[7][4] = 0;
      block[7][5] = 0;
      block[7][6] = 0;
      block[7][7] = 0;
      ThirdLevelEncoder thirdLevelEncoder = new ThirdLevelEncoder();
      System.out.println(thirdLevelEncoder.encodeBlock(block));
   }
}
