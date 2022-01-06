package com.sofka.biblioteca.services;

import com.sofka.biblioteca.models.Recurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.sofka.biblioteca.repositories.RecursosRepository;


@Service
public class BibliotecaService {

    @Autowired
    RecursosRepository repository;

    public Mono<Recurso> findOne(String id){
        return repository.findById(id);
    }

    public String consultarDisponibilidad(String id) {
        Mono<Recurso> answer = repository.findById(id);
        return validarDisponibilidad(answer);
    }

    private String validarDisponibilidad(Mono<Recurso> recurso) {
        return recurso.flatMap(libro -> {
            var disponibilidad = libro.isPrestado() ? "No disponible desde " + libro.getFechaSalida() : "Disponible";
            return Mono.just(disponibilidad);
        }).block();
    }

    public Mono prestarRecurso(String id) {
        return repository.findById(id).flatMap(recurso -> recurso.isPrestado() ?
             Mono.just("El rercurso esta prestado") : repository.save(recurso.setPrestado(true))
            );
    }

    public Object eliminarRecurso(String id) {
        return repository.deleteById(id);
    }

    public Mono<Recurso> agregarRecurso(Recurso Recurso) {
        var respuesta = repository.save(Recurso);
        if(respuesta == null){
            return null;
        }
        return respuesta;
    }

    public Flux<Recurso> recomendarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public Mono devolverRecurso(String id) {
        return repository.findById(id).flatMap(recurso -> recurso.isPrestado() ?
                repository.save(recurso.setPrestado(false)) : Mono.just("El recurso no se encuentra prestado")
        );
    }
}
