package com.miguel.webflux.repository;

import com.miguel.webflux.model.User;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Mono;

public interface IUserRepo extends IGenericRepo<User, String> {
    Mono<User> findOneByUsername(String username);
}
