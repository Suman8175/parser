package com.iso.parser.util;

import java.util.Set;
import java.util.TreeSet;

public class BitmapUtil {
    public static Set<Integer> parseBinaryBitmap(byte[] bitmap) {
        Set<Integer> fields = new TreeSet<>();
        for (int i = 0; i < bitmap.length; i++) {
            for (int bit = 0; bit < 8; bit++) {
                if ((bitmap[i] & (1 << (7 - bit))) != 0) {
                    fields.add(i * 8 + bit + 1);
                }
            }
        }
        return fields;
    }

    public static Set<Integer> parseSecondaryBitmap(byte[] bitmap) {
        Set<Integer> fields = new TreeSet<>();
        for (int i = 0; i < bitmap.length; i++) {
            for (int bit = 0; bit < 8; bit++) {
                if ((bitmap[i] & (1 << (7 - bit))) != 0) {
                    fields.add((i * 8 + bit + 1) + 64);
                }
            }
        }
        return fields;
    }
}