package com.miguel.webflux.controller;

import com.miguel.webflux.model.Menu;
import com.miguel.webflux.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final IMenuService menuService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Menu>>> getMenus() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(roles -> {
                    String rolesString = roles.stream().map(Objects::toString).collect(Collectors.joining(","));
                    String[] stringsArray = rolesString.split(",");
                    return menuService.getMenus(stringsArray);
                })
                .flatMap(fx -> Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx)));
    }
}
