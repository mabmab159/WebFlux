package com.miguel.webflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // Para que al compilar el generico no se instancie en Object y espere la ejecución
public interface IGenericRepo<T, ID> extends ReactiveMongoRepository<T, ID> {
}
