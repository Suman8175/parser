package com.iso.parser.decoder;

import com.iso.parser.definitions.IsoFieldDefinition;

import java.nio.ByteBuffer;

public interface FieldDecoder {
    String decode(ByteBuffer buffer, IsoFieldDefinition def);
}
