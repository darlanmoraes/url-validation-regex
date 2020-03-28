package br.com.darlan.urlvalidation.messaging.message;

import br.com.darlan.urlvalidation.validation.RegexConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertionInMessage {
    @Nullable
    private String client;
    @NotNull
    @NotEmpty
    @RegexConstraint
    private String regex;
}
