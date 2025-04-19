package org.ecommercecv.dto.request;

import lombok.Data;

@Data
public class EmailConfirmRequest {

    private String email;
    private String confirmationCode;
}
