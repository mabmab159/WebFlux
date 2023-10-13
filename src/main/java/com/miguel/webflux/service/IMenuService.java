package com.miguel.webflux.service;

import com.miguel.webflux.model.Menu;
import reactor.core.publisher.Flux;

public interface IMenuService extends ICRUD<Menu, String> {
    Flux<Menu> getMenus(String[] roles);
}
