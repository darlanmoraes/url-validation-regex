package br.com.darlan.urlvalidation.service.impl;

import br.com.darlan.urlvalidation.model.Whitelist;
import br.com.darlan.urlvalidation.repository.WhitelistRepository;
import br.com.darlan.urlvalidation.service.WhitelistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhitelistServiceImpl implements WhitelistService {

    private final WhitelistRepository whitelistRepository;

    @Autowired
    public WhitelistServiceImpl(final WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    @Override
    public void saveWhitelist(final String client, final String regex) {
        final Whitelist whitelist = Whitelist.builder()
                .client(client)
                .regex(regex)
            .build();
        this.whitelistRepository.save(whitelist);
        log.info("Saved whitelist = {}", whitelist);
    }
}