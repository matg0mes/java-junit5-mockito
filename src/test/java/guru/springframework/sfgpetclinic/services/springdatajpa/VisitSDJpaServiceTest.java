package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        Set<Visit> visits = Set.of(new Visit(), new Visit());

        given(visitRepository.findAll()).willReturn(visits);

        Set<Visit> visitsFound = service.findAll();

        then(visitRepository).should().findAll();
        assertThat(visitsFound).isNotEmpty();
        assertThat(visitsFound).hasSize(2);
    }

    @Test
    void findById() {
        Visit visit = new Visit();

        given(visitRepository.findById(anyLong())).willReturn(Optional.of(visit));

        Visit visitFound = service.findById(1L);

        then(visitRepository).should().findById(anyLong());
        assertThat(visitFound).isNotNull();
    }

    @Test
    void save() {
        Visit visit = new Visit();

        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        Visit visitSaved = service.save(visit);

        then(visitRepository).should().save(any(Visit.class));
        assertThat(visitSaved).isNotNull();
    }

    @Test
    void delete() {
        Visit visit = new Visit();

        service.delete(visit);

        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        verify(visitRepository).deleteById(anyLong());
        then(visitRepository).should().deleteById(anyLong());
    }
}