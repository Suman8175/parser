package com.iso.parser.definitions;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@XmlRootElement(name = "iso8583")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class IsoMessageDefinition {

    private MtiDefinition mti;
    private BitmapDefinition bitmap;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<IsoFieldDefinition> fields;
}
