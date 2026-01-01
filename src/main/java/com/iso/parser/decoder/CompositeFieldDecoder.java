package com.iso.parser.decoder;

import com.iso.parser.definitions.IsoFieldDefinition;
import com.iso.parser.enums.FieldTransformer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.iso.parser.enums.SubStringStat.SUB_FIELD;

public class CompositeFieldDecoder implements FieldDecoder {

    @Override
    public Object decode(ByteBuffer buffer, IsoFieldDefinition def) {
        // Read raw data block
        byte[] rawData = new byte[def.getLength()];
        buffer.get(rawData);
        String fullValue = new String(rawData, StandardCharsets.US_ASCII);

        // If no subfields, return raw string
        if (def.getSubfields() == null || def.getSubfields().isEmpty()) {
            return fullValue;
        }

        Map<String, Object> subElements = new LinkedHashMap<>();
        int offset = 0;

        // 1. Slice the data based on XML subfield definitions
        for (IsoFieldDefinition sub : def.getSubfields()) {
            int subLen = sub.getLength();
            if (offset + subLen <= fullValue.length()) {
                String val = fullValue.substring(offset, offset + subLen);
                subElements.put(SUB_FIELD+ sub.getId(), val);
                offset += subLen;
            }
        }

        // 2. Apply Transformer from Enum
        FieldTransformer transformer = FieldTransformer.fromString(def.getTransformer());
        transformer.transform(subElements);

        return subElements;
    }
}