package com.iso.parser.config;

import com.iso.parser.definitions.IsoMessageDefinition;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class IsoPackagerLoader {

    private IsoMessageDefinition definition;

    @PostConstruct
    public void load() throws Exception {

        JAXBContext context =
                JAXBContext.newInstance(IsoMessageDefinition.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        this.definition = (IsoMessageDefinition)
                unmarshaller.unmarshal(
                        new ClassPathResource("iso/iso8583.xml").getInputStream()
                );
    }

    public IsoMessageDefinition getDefinition() {
        return definition;
    }
}

