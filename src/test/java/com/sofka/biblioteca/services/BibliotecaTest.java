package com.sofka.biblioteca.services;

import com.sofka.biblioteca.models.Recurso;
import com.sofka.biblioteca.repositories.RecursosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.when;

@SpringBootTest
class BibliotecaTest {

    @Autowired
    BibliotecaService service;

    @MockBean
    private RecursosRepository repository;

    @Test
    @DisplayName("Consulta a un disponible")
    void consultarDisponibilidad() {
        //arrange
        var id = "124";
        Recurso recursoTest = new Recurso("00/00/00", "xx", "yy");
        recursoTest.setId("124");
        //act
        Mockito.when(repository.findById("124")).thenReturn(Mono.just(recursoTest));
        var isDisponible = service.consultarDisponibilidad(id);
        //assert
        Assertions.assertEquals("Disponible", isDisponible);
    }

    @Test
    @DisplayName("Consulta a un no disponible")
    void consultarNoDisponible() {
        //arrange
        var id = "450";
        Recurso recursoTest = new Recurso("00/00/00", "xx", "yy");
        recursoTest.setId(id);
        recursoTest.setPrestado(true);
        //act
        Mockito.when(repository.findById(id)).thenReturn(Mono.just(recursoTest));
        var isDisponible = service.consultarDisponibilidad(id);
        //assert
        Assertions.assertEquals("No disponible desde 00/00/00", isDisponible);
    }

    @Test
    @DisplayName("Recomendaciones por tipo")
    void recomendarPorTipo() {
        //arrange
        var tipo = "articulo";
        Recurso recursoTest1 = new Recurso("00/00/00", "xx", "articulo");
        Recurso recursoTest2 = new Recurso("00/00/00", "xx", "articulo");
        //act
        Mockito.when(repository.findByTipo(tipo)).thenReturn(Flux.just(recursoTest1, recursoTest2));
        var respuesta = service.recomendarPorTipo(tipo);
        //Assert
        var listado = respuesta.collectList().block();
        Assertions.assertEquals("articulo", listado.get(0).getTipo());
        Assertions.assertEquals("articulo", listado.get(1).getTipo());
        Assertions.assertEquals(2, listado.stream().count());
    }

    @Test
    @DisplayName("Prestamo de recurso correcto")
    void prestarRecurso(){
        //arrange
        String id = "0000";
        Recurso recursoTest = new Recurso("00/00/00", "xx", "yy");
        recursoTest.setId(id);
        //act
        Mockito.when(repository.findById(id)).thenReturn(Mono.just(recursoTest));
        var respuesta = service.prestarRecurso(id);
        //assert
        Assertions.assertEquals(true, recursoTest.isPrestado());
        Assertions.assertEquals("0000", recursoTest.getId());
        Assertions.assertNotEquals(null, respuesta);
    }

    @Test
    @DisplayName("Devolucion de recurso correcta")
    void devolverRecurso(){
        //arrange
        String id = "0000";
        Recurso recursoTest = new Recurso("00/00/00", "Recurso Test", "Test");
        recursoTest.setId(id);
        //act
        Mockito.when(repository.findById(id)).thenReturn(Mono.just(recursoTest));
        var respuesta = service.devolverRecurso(id);
        //assert
        Assertions.assertEquals(false, recursoTest.isPrestado());
        Assertions.assertEquals("0000", recursoTest.getId());
        Assertions.assertEquals("Test", recursoTest.getTipo());
        Assertions.assertEquals("Recurso Test", recursoTest.getTitulo());
        Assertions.assertNotEquals(null, respuesta);
    }
}