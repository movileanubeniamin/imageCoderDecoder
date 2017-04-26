package com.custom.site.name.utils;

/**
 * Created by horea on 26/04/2017.
 */
public class OtherUtils {

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
