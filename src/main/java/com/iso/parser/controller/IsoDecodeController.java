package com.iso.parser.controller;

import com.iso.parser.decoder.IsoMessageDecoder;
import com.iso.parser.dto.IsoDecodeRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/iso")
@Slf4j
public class IsoDecodeController {

    private final IsoMessageDecoder decoder;

    public IsoDecodeController(IsoMessageDecoder decoder) {
        this.decoder = decoder;
    }

    @PostMapping("/decode")
    public Map<Integer, String> decode(@RequestBody IsoDecodeRequest request) throws DecoderException {

        String isoHex = request.getIsoMessage().trim();

        // Convert ASCII-HEX (e.g., "30313030") to actual bytes (e.g., [0x30, 0x31, 0x30, 0x30])
        // This is necessary because IsoMessageDecoder uses a ByteBuffer
        byte[] isoBytes = Hex.decodeHex(isoHex);
        long startTimeMs = System.currentTimeMillis();
        // Call your decoder which processes the MTI, Bitmap, and Fields
        Map<Integer, String> decode = decoder.decode(isoBytes);
        long endTimeMs = System.currentTimeMillis();
        long durationMs = endTimeMs - startTimeMs;

        log.info("ISO decode completed in {} ms", durationMs);
        return decode;
    }



}

