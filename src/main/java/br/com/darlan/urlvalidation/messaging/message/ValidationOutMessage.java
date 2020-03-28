package br.com.darlan.urlvalidation.messaging.message;

import br.com.darlan.urlvalidation.model.UrlValidation;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationOutMessage implements Serializable {
    @NonNull
    private Boolean match;
    @Nullable
    private String regex;
    @NonNull
    private Integer correlationId;

    public static ValidationOutMessage from(final UrlValidation result) {
        return ValidationOutMessage.builder()
                .match(result.getMatch())
                .regex(result.getRegex())
                .correlationId(result.getCorrelationId())
            .build();
    }
}
