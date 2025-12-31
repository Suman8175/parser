package com.iso.parser.decoder;

import com.iso.parser.definitions.IsoFieldDefinition;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FixedFieldDecoder implements FieldDecoder {

    @Override
    public String decode(ByteBuffer buffer, IsoFieldDefinition def) {
        byte[] data = new byte[def.getLength()];
        buffer.get(data);
        return new String(data, StandardCharsets.US_ASCII);
    }
}

