package com.iso.parser.decoder;

import com.iso.parser.definitions.IsoFieldDefinition;
import java.nio.ByteBuffer;

public interface FieldDecoder {
    // Crucial: Change return type to Object
    Object decode(ByteBuffer buffer, IsoFieldDefinition def);
}