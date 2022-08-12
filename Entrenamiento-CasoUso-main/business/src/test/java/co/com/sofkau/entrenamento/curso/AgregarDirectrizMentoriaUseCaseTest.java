package co.com.sofkau.entrenamento.curso;


import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.Mentoria;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.CursoId;
import co.com.sofkau.entrenamiento.curso.values.Descripcion;
import co.com.sofkau.entrenamiento.curso.values.Directiz;
import co.com.sofkau.entrenamiento.curso.values.Fecha;
import co.com.sofkau.entrenamiento.curso.values.MentoriaId;
import co.com.sofkau.entrenamiento.curso.values.Nombre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AgregarDirectrizMentoriaUseCaseTest {

    @InjectMocks
    private AgregarDirectrizMentoriaUseCase useCase;
    @Mock
    private DomainEventRepository repository;

    @Test
    public void agregarDirectrizAMentoria() {
       //arrange
        CursoId cursoId = CursoId.of("dsasd");
        MentoriaId mentoriaId = MentoriaId.of("dsasdd");
        Directiz directriz = new Directiz("Terminar el Test");
        var command = new AgregarDirectrizMentoria(cursoId,mentoriaId,directriz);

        when(repository.getEventsBy("dsasd")).thenReturn(history());
        useCase.addRepository(repository);
        //act
        var events = UseCaseHandler.getInstance()
                .setIdentifyExecutor(command.getCursoId().value())
                .syncExecutor(useCase, new RequestCommand<>(command))
                .orElseThrow()
                .getDomainEvents();
        //assert
        var event = (DirectrizAgregadaAMentoria)events.get(0);
        Assertions.assertEquals("Terminar el Test", event.getDirectiz().value());
    }

    public List<DomainEvent> history() {
        Nombre nombre = new Nombre("DDD");
        Descripcion descripcion = new Descripcion("training");
        var event = new CursoCreado(
                nombre,
                descripcion
        );

        MentoriaId mentoriaId = MentoriaId.of("dsasdd");
        Nombre nombreMentoria = new Nombre("Casos de uso DDD");
        Fecha fechaMentoria = new Fecha(LocalDateTime.now(), LocalDate.now());
        var eventMentoria = new MentoriaCreada(
                mentoriaId,
                nombreMentoria,
                fechaMentoria
        );
        event.setAggregateRootId("xxxxx");
        return List.of(event,eventMentoria);
    }
}

