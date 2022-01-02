package com.sofka.biblioteca.services;

import com.sofka.biblioteca.models.Recurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.sofka.biblioteca.repositories.RecursosRepository;

import java.util.Date;

@Service
public class BibliotecaService {

    @Autowired
    RecursosRepository repository;

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

    public Object prestarRecurso(String id) {
        Recurso recurso = repository.findById(id).block();
        recurso.setPrestado(true);
        repository.save(recurso);
        return "Prestado " + new Date();
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

    public String devolverRecurso(String id) {
        Recurso recurso = repository.findById(id).block();
        recurso.setPrestado(false);
        repository.save(recurso);
        return "Devuelto " + new Date();
    }
}
