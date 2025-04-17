package org.ecommercecv.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}
