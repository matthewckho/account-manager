package com.acmebank.accountManager.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String status;
    private String message;
}
