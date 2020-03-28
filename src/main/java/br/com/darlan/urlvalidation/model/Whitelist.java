package br.com.darlan.urlvalidation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "whitelist")
public class Whitelist {

    public enum Type {
        GLOBAL,
        PRIVATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String client;
    @NonNull
    private String regex;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Type type = Type.GLOBAL;

    @SuppressWarnings("unused")
    public static class WhitelistBuilder {
        public WhitelistBuilder client(final String client) {
            this.client = client;
            this.type(isEmpty(client) ? Type.GLOBAL : Type.PRIVATE);
            return this;
        }
    }

}
