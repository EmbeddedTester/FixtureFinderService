package com.abetterway2feel.fixturefinder.rest.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDTO {
    public String code;
    public String message;
}
