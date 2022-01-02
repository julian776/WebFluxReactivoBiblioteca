package com.sofka.biblioteca.repositories;

import com.sofka.biblioteca.models.Recurso;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RecursosRepository extends ReactiveMongoRepository<Recurso, String> {
    public Flux<Recurso> findByTipo(String tipo);
}
