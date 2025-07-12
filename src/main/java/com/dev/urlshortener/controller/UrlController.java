package com.dev.urlshortener.controller;

import com.dev.urlshortener.model.UrlMapping;
import com.dev.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createShort(@RequestBody Map<String, String> body) {
        String longUrl = body.get("originalUrl");
        UrlMapping mapping = service.createShortUrl(longUrl);
        return ResponseEntity.ok(Map.of(
                "shortCode", mapping.getShortCode(),
                "shortUrl", "http://localhost:8080/r/" + mapping.getShortCode()
        ));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getInfo(@PathVariable String code) {
        return service.getByShortCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable String code, @RequestBody Map<String, String> body) {
        return service.updateUrl(code, body.get("originalUrl"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@PathVariable String code) {
        boolean deleted = service.deleteByShortCode(code);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{code}/stats")
    public ResponseEntity<?> stats(@PathVariable String code) {
        return service.getByShortCode(code)
                .map(url -> ResponseEntity.ok(Map.of(
                        "originalUrl", url.getOriginalUrl(),
                        "clickCount", url.getClickCount(),
                        "createdAt", url.getCreatedAt()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
