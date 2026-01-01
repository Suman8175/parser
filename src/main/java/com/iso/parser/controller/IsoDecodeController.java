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
    public Map<Integer, Object> decode(@RequestBody IsoDecodeRequest request) throws DecoderException {
        String isoHex = request.getIsoMessage().trim();
        long startTimeNs = System.nanoTime();
        byte[] isoBytes = Hex.decodeHex(isoHex);

        // Returns the structured map
        Map<Integer, Object> decode = decoder.decode(isoBytes);
        long durationMs = (System.nanoTime() - startTimeNs) / 1_000_000;

        log.info("ISO decode completed in {} ms", durationMs);

        return decode;
    }
}

