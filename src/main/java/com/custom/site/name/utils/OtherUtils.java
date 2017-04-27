package com.custom.site.name.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horea on 26/04/2017.
 */
public class OtherUtils {

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static List reverseList(List myList) {
        List invertedList = new ArrayList();
        for (int i = myList.size() - 1; i >= 0; i--) {
            invertedList.add(myList.get(i));
        }
        return invertedList;
    }
}
