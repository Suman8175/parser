package com.iso.parser.definitions;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class IsoFieldDefinition {

    @XmlAttribute
    private int id;

    @XmlAttribute
    private String type; // FIXED, LLVAR

    @XmlAttribute
    private Integer length;

    @XmlAttribute
    private Integer maxLength;

    @XmlAttribute
    private String dataType;
}
