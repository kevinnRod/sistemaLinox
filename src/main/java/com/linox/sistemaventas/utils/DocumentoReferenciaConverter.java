package com.linox.sistemaventas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.linox.sistemaventas.models.DocumentoReferencia;

@Converter(autoApply = false)
public class DocumentoReferenciaConverter implements AttributeConverter<DocumentoReferencia, String> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DocumentoReferencia attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializando DocumentoReferencia", e);
        }
    }

    @Override
    public DocumentoReferencia convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, DocumentoReferencia.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializando DocumentoReferencia", e);
        }
    }
}
