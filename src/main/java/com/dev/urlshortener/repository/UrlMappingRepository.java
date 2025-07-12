package com.dev.urlshortener.repository;

import com.dev.urlshortener.model.UrlMapping;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UrlMappingRepository extends CrudRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortCode);
    void deleteByShortCode(String shortCode);
}
