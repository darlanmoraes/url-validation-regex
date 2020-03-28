package br.com.darlan.urlvalidation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlValidation {
    @NonNull
    @Builder.Default
    private Boolean match = false;
    @Nullable
    private String regex;
    @NonNull
    private Integer correlationId;

    @SuppressWarnings("unused")
    public static class UrlValidationBuilder {
        public UrlValidationBuilder regex(final String regex) {
            this.regex = regex;
            this.match(!isEmpty(regex));
            return this;
        }
    }

}