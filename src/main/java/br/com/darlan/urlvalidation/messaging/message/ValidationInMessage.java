package br.com.darlan.urlvalidation.messaging.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationInMessage {
    @NotNull
    @NotEmpty
    private String client;
    @NotNull
    @NotEmpty
    private String url;
    @NotNull
    private Integer correlationId;
}
