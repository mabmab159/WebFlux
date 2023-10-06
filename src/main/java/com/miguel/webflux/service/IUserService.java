package com.miguel.webflux.service;

import com.miguel.webflux.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String>{

    Mono<User> saveHash(User user);

    Mono<com.miguel.webflux.security.User> seachByUser(String username);
}
