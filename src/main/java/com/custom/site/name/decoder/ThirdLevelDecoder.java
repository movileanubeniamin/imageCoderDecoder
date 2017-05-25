package com.custom.site.name.decoder;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 1; i < 64; i++) {
            if (vector.size() <= 6)
                break;

            i += vector.get(3 * temp - 1);
            block[posx.get(i)][posy.get(i)] = vector.get(3 * temp + 1);

            temp += 1;
            if ((temp * 3 + 1) > vector.size() - 1)
                break;
        }
        return block;
    }

    public static List<long[][]> zigZagToList(List<Long> zigZagList) {
        List<long[][]> unzigZagList = new ArrayList<long[][]>();
        List<Long>  zigZagVector = new ArrayList<Long>();
        zigZagVector.add(zigZagList.get(0));
        for (int i = 1; i < zigZagList.size(); i++) {
            zigZagVector.add(zigZagList.get(i));
            if (((zigZagList.get(i-1) == 0) && (zigZagList.get(i) == 0))){
                if (zigZagVector.size() > 2) {
                    unzigZagList.add(decodeVector(zigZagVector));
                    zigZagVector.clear();
                }
            }
        }
        return unzigZagList;
    }

}
