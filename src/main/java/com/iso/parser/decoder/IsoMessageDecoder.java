package com.iso.parser.decoder;

import com.iso.parser.config.IsoPackagerLoader;
import com.iso.parser.definitions.IsoFieldDefinition;
import com.iso.parser.definitions.IsoMessageDefinition;
import com.iso.parser.util.BitmapUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
            "LLVAR", new LlvarFieldDecoder()
    );

    public IsoMessageDecoder(IsoPackagerLoader loader) {
        this.loader = loader;
    }

    public Map<Integer, String> decode(byte[] message) throws DecoderException {
        IsoMessageDefinition def = loader.getDefinition();
        ByteBuffer buffer = ByteBuffer.wrap(message);
        Map<Integer, String> result = new LinkedHashMap<>();

        // 1. Decode MTI
        byte[] mtiBytes = new byte[def.getMti().getLength()];
        buffer.get(mtiBytes);
        result.put(0, new String(mtiBytes));

        // 2. Decode Primary Bitmap
        byte[] primaryBitmap = new byte[8];
        buffer.get(primaryBitmap);
        Set<Integer> fields = BitmapUtil.parseBinaryBitmap(primaryBitmap);

        // 3. Check for Secondary Bitmap (Field 1)
        if (fields.contains(1)) {
            byte[] secondaryBitmap = new byte[8];
            buffer.get(secondaryBitmap);
            fields.addAll(BitmapUtil.parseSecondaryBitmap(secondaryBitmap));
            fields.remove(1); // Remove the flag field itself
        }

        // 4. Decode Fields
        for (Integer fieldId : fields) {
            // Find definition from XML
            IsoFieldDefinition fd = def.getFields().stream()
                    .filter(f -> f.getId() == fieldId)
                    .findFirst()
                    .orElse(null);

            if (fd != null) {
                FieldDecoder decoder = decoders.get(fd.getType());
                result.put(fieldId, decoder.decode(buffer, fd));
            } else {
                // Optional: Handle or log undefined fields to avoid stopping the parser
                System.out.println("Warning: Field " + fieldId + " present in bitmap but not in XML");
            }
        }
        return result;
    }
}
