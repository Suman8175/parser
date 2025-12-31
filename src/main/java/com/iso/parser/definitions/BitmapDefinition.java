package com.iso.parser.definitions;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BitmapDefinition {

    @XmlAttribute
    private String type; // PRIMARY, SECONDARY
}
