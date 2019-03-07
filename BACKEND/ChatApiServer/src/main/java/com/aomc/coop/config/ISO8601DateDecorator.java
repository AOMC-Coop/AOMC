package com.aomc.coop.config;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import net.logstash.logback.decorate.JsonFactoryDecorator;

public class ISO8601DateDecorator implements JsonFactoryDecorator {
    @Override
    public MappingJsonFactory decorate(MappingJsonFactory mappingJsonFactory) {
        ObjectMapper codec = mappingJsonFactory.getCodec();
        codec.setDateFormat(new ISO8601DateFormat());
        return mappingJsonFactory;
    }
}
