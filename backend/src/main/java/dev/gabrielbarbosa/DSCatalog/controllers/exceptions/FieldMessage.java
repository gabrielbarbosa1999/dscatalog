package dev.gabrielbarbosa.DSCatalog.controllers.exceptions;

public class FieldMessage {

    private String fieldName;

    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public FieldMessage(org.springframework.validation.FieldError fieldError) {
        this.fieldName = fieldError.getField();
        this.message = fieldError.getDefaultMessage();;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

}
