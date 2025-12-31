package com.iso.parser.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Set;
import java.util.TreeSet;

public class BitmapUtil {

    // Use this for the first 8 bytes after the MTI
    public static Set<Integer> parseBinaryBitmap(byte[] bitmap) {
        Set<Integer> fields = new TreeSet<>();
        for (int i = 0; i < bitmap.length; i++) {
            for (int bit = 0; bit < 8; bit++) {
                if ((bitmap[i] & (1 << (7 - bit))) != 0) {
                    fields.add(i * 8 + bit + 1); // Fields 1 to 64
                }
            }
        }
        return fields;
    }

    // Existing method for secondary bitmaps (if field 1 is present)
    public static Set<Integer> parseSecondaryBitmap(byte[] bitmap) {
        Set<Integer> fields = new TreeSet<>();
        for (int i = 0; i < bitmap.length; i++) {
            for (int bit = 0; bit < 8; bit++) {
                if ((bitmap[i] & (1 << (7 - bit))) != 0) {
                    fields.add((i * 8 + bit + 1) + 64); // Fields 65 to 128
                }
            }
        }
        return fields;
    }
}
