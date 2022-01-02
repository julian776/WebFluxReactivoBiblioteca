package com.sofka.biblioteca.controllers;

import com.sofka.biblioteca.models.Recurso;
import com.sofka.biblioteca.services.BibliotecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class Controller {

    @Autowired
    BibliotecaService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> isDisponile(@PathVariable("id") String id){
        var isDisponible = service.consultarDisponibilidad(id);
        if(isDisponible == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(isDisponible);
    }

    @PostMapping("/agregar")
    public ResponseEntity crearLibroBiblioteca(@RequestBody Recurso Recurso ){
        var recursoAgregado = service.agregarRecurso(Recurso);
        System.out.println(Recurso);
        if(recursoAgregado == null){
            System.out.println("Estoy dentro");
            return ResponseEntity.unprocessableEntity().body(Recurso);
        }
        return ResponseEntity.ok(recursoAgregado.block());
    }

    @PutMapping("/prestar/{id}")
    public ResponseEntity<String> prestarRecurso(@PathVariable("id") String id){
        return new ResponseEntity(service.prestarRecurso(id), HttpStatus.OK);
    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<String> devolverRecurso(@PathVariable("id") String id){
        return new ResponseEntity<>(service.devolverRecurso(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRecurso(@PathVariable("id") String id){
        return new ResponseEntity(service.eliminarRecurso(id), HttpStatus.OK);
    }

    @GetMapping("/recomendaciones/{tipo}")
    public Flux<Recurso> recomendar(@PathVariable("tipo") String tipo){
        return service.recomendarPorTipo(tipo);
    }
}
