package br.com.darlan.urlvalidation.service.impl;

import br.com.darlan.urlvalidation.model.Whitelist;
import br.com.darlan.urlvalidation.model.UrlValidation;
import br.com.darlan.urlvalidation.repository.WhitelistRepository;
import br.com.darlan.urlvalidation.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.darlan.urlvalidation.model.Whitelist.Type;

@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {

    private final WhitelistRepository whitelistRepository;

    @Autowired
    public ValidationServiceImpl(final WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    @Override
    public UrlValidation validateUrl(final String client, final String url, final Integer correlationId) {
        final List<Whitelist> candidates = this.findCandidates(client);
        final Optional<Whitelist> whitelist = this.match(url, candidates);
        log.info("The url={} for client={} has matched the whitelist={}", url, client, whitelist);
        return UrlValidation.builder()
                .regex(whitelist.map(Whitelist::getRegex).orElse(null))
                .correlationId(correlationId)
            .build();
    }

    private Optional<Whitelist> match(final String url, final List<Whitelist> candidates) {
        return candidates.stream()
            .filter(whitelist -> url.matches(whitelist.getRegex()))
            .findFirst();
    }

    private List<Whitelist> findCandidates(final String client) {
        final List<Whitelist> privates = this.whitelistRepository.findByClientAndType(client, Type.PRIVATE);
        final List<Whitelist> globals = this.whitelistRepository.findByType(Type.GLOBAL);
        log.info("Loaded ({})PRIVATE and ({})GLOBAL whitelist. client={}", privates.size(), globals.size(), client);
        final List<Whitelist> candidates = new ArrayList<>(privates);
        candidates.addAll(globals);
        return candidates;
    }

}
