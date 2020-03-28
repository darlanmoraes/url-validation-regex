package br.com.darlan.urlvalidation.repository;

import br.com.darlan.urlvalidation.model.Whitelist;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static br.com.darlan.urlvalidation.model.Whitelist.Type;

@Repository
public interface WhitelistRepository extends CrudRepository<Whitelist, Long> {
    @Cacheable("WhitelistRepository_findByType")
    List<Whitelist> findByType(Type type);
    @Cacheable("WhitelistRepository_findByClientAndType")
    List<Whitelist> findByClientAndType(String client, Type type);
}
