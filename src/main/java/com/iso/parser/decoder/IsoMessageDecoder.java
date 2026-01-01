package com.iso.parser.decoder;

import com.iso.parser.config.IsoPackagerLoader;
import com.iso.parser.definitions.IsoFieldDefinition;
import com.iso.parser.definitions.IsoMessageDefinition;
import com.iso.parser.util.BitmapUtil;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class IsoMessageDecoder {

    private final IsoPackagerLoader loader;
    private final Map<String, FieldDecoder> decoders = Map.of(
            "FIXED", new FixedFieldDecoder(),
            "LLVAR", new LlvarFieldDecoder(),
            "COMPOSITE", new CompositeFieldDecoder()
    );

    public IsoMessageDecoder(IsoPackagerLoader loader) {
        this.loader = loader;
    }

    public Map<Integer, Object> decode(byte[] message) throws DecoderException {
        IsoMessageDefinition def = loader.getDefinition();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        Map<Integer, Object> result = new LinkedHashMap<>();

        // 1. MTI
        byte[] mtiBytes = new byte[def.getMti().getLength()];
        buffer.get(mtiBytes);
        result.put(0, new String(mtiBytes));

        // 2. Primary Bitmap
        byte[] bitmapBytes = new byte[8];
        buffer.get(bitmapBytes);
        Set<Integer> fields = BitmapUtil.parseBinaryBitmap(bitmapBytes);

        // 3. Secondary Bitmap (if field 1 is present)
        if (fields.contains(1)) {
            byte[] secondaryBytes = new byte[8];
            buffer.get(secondaryBytes);
            fields.addAll(BitmapUtil.parseSecondaryBitmap(secondaryBytes));
            fields.remove(1);
        }

        // 4. Fields Parsing
        for (Integer fieldId : fields) {
            IsoFieldDefinition fd = def.getFields().stream()
                    .filter(f -> f.getId() == fieldId)
                    .findFirst()
                    .orElse(null);

            if (fd != null) {
                FieldDecoder decoder = decoders.get(fd.getType());
                // This puts either a String or the Map from CompositeFieldDecoder
                result.put(fieldId, decoder.decode(buffer, fd));
            }
            else {
                // Optional: Handle or log undefined fields to avoid stopping the parser
                System.out.println("Warning: Field " + fieldId + " present in bitmap but not in XML");
            }
        }
        return result;
    }
}