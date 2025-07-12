package com.dev.urlshortener.service;

import com.dev.urlshortener.model.UrlMapping;
import com.dev.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlMappingRepository repo;

    public UrlService(UrlMappingRepository repo) {
        this.repo = repo;
    }

    public UrlMapping createShortUrl(String originalUrl) {
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(UUID.randomUUID().toString().substring(0, 6));
        return repo.save(mapping);
    }

    public Optional<UrlMapping> getByShortCode(String code) {
        return repo.findByShortCode(code);
    }

    public Optional<UrlMapping> updateUrl(String code, String newUrl) {
        Optional<UrlMapping> existing = repo.findByShortCode(code);
        existing.ifPresent(url -> {
            url.setOriginalUrl(newUrl);
            repo.save(url);
        });
        return existing;
    }

    public boolean deleteByShortCode(String code) {
        Optional<UrlMapping> existing = repo.findByShortCode(code);
        existing.ifPresent(repo::delete);
        return existing.isPresent();
    }

    public void incrementClick(UrlMapping url) {
        url.setClickCount(url.getClickCount() + 1);
        repo.save(url);
    }
}
