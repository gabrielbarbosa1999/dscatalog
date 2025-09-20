package dev.gabrielbarbosa.DSCatalog.controllers.exceptions;

public class FieldMessage {

    private String fildName;

    private String message;

    public FieldMessage(String fildName, String message) {
        this.fildName = fildName;
        this.message = message;
    }

    public FieldMessage(org.springframework.validation.FieldError fieldError) {
        this.fildName = fieldError.getField();
        this.message = fieldError.getDefaultMessage();;
    }

    public String getFildName() {
        return fildName;
    }

    public String getMessage() {
        return message;
    }

}
