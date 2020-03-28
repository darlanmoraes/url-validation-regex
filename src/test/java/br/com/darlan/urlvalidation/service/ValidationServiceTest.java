package br.com.darlan.urlvalidation.service;

import br.com.darlan.urlvalidation.model.Whitelist;
import br.com.darlan.urlvalidation.model.UrlValidation;
import br.com.darlan.urlvalidation.repository.WhitelistRepository;
import br.com.darlan.urlvalidation.service.impl.ValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

import static br.com.darlan.urlvalidation.model.Whitelist.Type;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceTest {

    @Mock
    private WhitelistRepository whitelistRepository;
    private ValidationService validationService;

    @Before
    public void setUp() {
        this.validationService = new ValidationServiceImpl(whitelistRepository);
        final Whitelist whitelistG = Whitelist.builder()
                .regex("http://.*")
            .build();

        final Whitelist whitelistP = Whitelist.builder()
                .regex("http://darlan\\.com")
            .build();

        when(whitelistRepository.findByType(Type.GLOBAL))
                .thenReturn(Collections.singletonList(whitelistG));
        when(whitelistRepository.findByClientAndType("client1", Type.PRIVATE))
                .thenReturn(Collections.singletonList(whitelistP));

    }

    @Test
    public void givenNullClientAndValidUrlShouldMatchGlobal() {
        final UrlValidation validation = validationService.validateUrl(null, "http://google.com", 123);
        assertTrue(validation.getMatch());
        assertEquals("http://.*", validation.getRegex());
        assertEquals(new Integer(123), validation.getCorrelationId());
    }

    @Test
    public void givenNullClientAndInvalidUrlShouldNotMatch(){
        final UrlValidation validation = validationService.validateUrl(null, "ayreon", 123);
        assertFalse(validation.getMatch());
        assertNull(validation.getRegex());
        assertEquals(new Integer(123), validation.getCorrelationId());
    }

    @Test
    public void givenAClientAndValidUrlShouldMatchGlobal(){
        final UrlValidation validation = validationService.validateUrl(null, "http://arroz.com", 123);
        assertTrue(validation.getMatch());
        assertEquals("http://.*", validation.getRegex());
        assertEquals(new Integer(123), validation.getCorrelationId());
    }

    @Test
    public void givenAClientAndValidUrlShouldMatchPrivate(){
        final UrlValidation validation = validationService.validateUrl("client1", "http://darlan.com", 123);
        assertTrue(validation.getMatch());
        assertEquals("http://darlan\\.com", validation.getRegex());
        assertEquals(new Integer(123), validation.getCorrelationId());
    }

    @Test
    public void givenAClientAndInvalidUrlShouldNotMatch(){
        final UrlValidation validation = validationService.validateUrl("client1", "dreams", 123);
        assertFalse(validation.getMatch());
        assertNull(validation.getRegex());
        assertEquals(new Integer(123), validation.getCorrelationId());
    }


}