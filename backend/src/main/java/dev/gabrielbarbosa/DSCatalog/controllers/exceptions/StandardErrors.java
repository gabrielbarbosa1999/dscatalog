package dev.gabrielbarbosa.DSCatalog.controllers.exceptions;

import java.util.List;

public class StandardErrors extends StandardError {

    private List<FieldMessage> errors;

    public StandardErrors(String message, Integer status, List<FieldMessage> errors) {
        super(message, status);
        this.errors = errors;
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

}

