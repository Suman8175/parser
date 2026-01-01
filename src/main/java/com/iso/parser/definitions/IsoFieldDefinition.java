package com.iso.parser.definitions;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class IsoFieldDefinition {
    @XmlAttribute private int id;
    @XmlAttribute private String type;
    @XmlAttribute private Integer length;
    @XmlAttribute private Integer maxLength;
    @XmlAttribute private String dataType;

    @XmlAttribute private String transformer;
    // This allows <field id="10"> <field id="1" ... /> </field>
    @XmlElement(name = "field")
    private List<IsoFieldDefinition> subfields;
}