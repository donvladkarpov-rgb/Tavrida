package ru.vtb.msa.detr.tavrida.core.exception;

public class EntityNotFoundException extends RuntimeException {
    private String fieldName;
    private Object fieldValue;
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, String fieldName, Object fieldValue) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}