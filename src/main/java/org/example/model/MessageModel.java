package org.example.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MessageModel {

    @NotBlank(message = "Message can't be not blank")
    @NotNull(message = "Message can't be not null")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
