package com.miguel.webflux.security;

import org.springframework.stereotype.Component;

@Component
public class TestAuthorize {
    public boolean hasAccess() {
        return false;
    }
}
