package br.com.darlan.urlvalidation.service;

public interface WhitelistService {
    void saveWhitelist(String client, String regex);
}
