package com.iso.parser.decoder;

import com.iso.parser.definitions.IsoFieldDefinition;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class LlvarFieldDecoder implements FieldDecoder {
    @Override
    public String decode(ByteBuffer buffer, IsoFieldDefinition def) {
        // Read 2 bytes for the length (e.g., '0', '6')
        byte[] lenBytes = new byte[2];
        buffer.get(lenBytes);

        int len = Integer.parseInt(new String(lenBytes, StandardCharsets.US_ASCII));

        // Read the actual data
        byte[] data = new byte[len];
        buffer.get(data);

        return new String(data, StandardCharsets.US_ASCII);
    }
}

