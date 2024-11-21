package org.pda.announcement.util.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiCustomResponse {
    private String message;
    private Object data;

    public ApiCustomResponse(String message) {
        this.message = message;
    }
}