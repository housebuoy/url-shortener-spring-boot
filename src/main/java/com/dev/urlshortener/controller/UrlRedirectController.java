package com.dev.urlshortener.controller;

import com.dev.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class UrlRedirectController {
    private final UrlService service;

    public UrlRedirectController(UrlService service) {
        this.service = service;
    }

    @GetMapping("/r/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        service.getByShortCode(code).ifPresentOrElse(url -> {
            try {
                service.incrementClick(url);
                response.sendRedirect(url.getOriginalUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, () -> {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
