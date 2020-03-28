package br.com.darlan.urlvalidation.service;

import br.com.darlan.urlvalidation.model.UrlValidation;

public interface ValidationService {
    UrlValidation validateUrl(String client, String url, Integer correlationId);
}
