package de.audibene.common.composablecrud.service;


import de.audibene.common.composablecrud.datatransferobject.TestDTO;
import de.audibene.common.composablecrud.domainobject.TestDO;
import de.audibene.common.composablecrud.problem.EntityNotFoundProblem;
import de.audibene.common.composablecrud.repository.IdentifiableRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class IdentifiableTraitTest {

    @Mock
    private TestRepository repository;

    @Spy
    @InjectMocks
    private TestTrait trait;

    private final String id = UUID.randomUUID().toString();

    @Mock
    private TestDO object;
    @Mock
    private TestDTO dto;
    @Mock
    private Function<TestDO, TestDTO> mapper;


    @Test
    public void shouldDelegateFindByIdToRepositoryAndReturnSome() {
        doReturn(Optional.of(object)).when(repository).findById(anyString());

        final Optional<TestDO> actual = trait.findById(id);

        assertThat(actual).containsSame(object);
        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldDelegateFindByIdToRepositoryAndReturnNone() {
        doReturn(Optional.empty()).when(repository).findById(anyString());

        final Optional<TestDO> actual = trait.findById(id);

        assertThat(actual).isEmpty();
        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldDelegateFindByIdToTraitWithMapperAndReturnSome() {
        doReturn(Optional.of(object)).when(trait).findById(anyString());
        doReturn(dto).when(mapper).apply(any(TestDO.class));

        final Optional<TestDTO> actual = trait.findById(id, mapper);

        assertThat(actual).containsSame(dto);
        verify(trait).findById(id);
        verify(mapper).apply(object);
        verifyNoMoreInteractions(repository, object, mapper, dto);
    }

    @Test
    public void shouldDelegateFindByIdToTraitWithMapperAndReturnNone() {
        doReturn(Optional.empty()).when(trait).findById(anyString());

        final Optional<TestDTO> actual = trait.findById(id, mapper);

        assertThat(actual).isEmpty();
        verify(trait).findById(id);
        verifyZeroInteractions(repository, mapper);
    }

    @Test
    public void shouldDelegateGetByIdToTraitAndReturn() {
        doReturn(Optional.of(object)).when(trait).findById(anyString());

        final TestDO actual = trait.getById(id);

        assertThat(actual).isSameAs(object);
        verify(trait).findById(id);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldDelegateGetByIdToTraitAndThrow() {
        doReturn(Optional.empty()).when(trait).findById(anyString());

        assertThatThrownBy(() -> trait.getById(id))
                .isInstanceOf(EntityNotFoundProblem.class);

        verify(trait).findById(id);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldDelegateGetByIdToTraitWithMapperAndReturn() {
        doReturn(Optional.of(dto)).when(trait).findById(anyString(), any());

        final TestDTO actual = trait.getById(id, mapper);

        assertThat(actual).isSameAs(dto);
        verify(trait).findById(id, mapper);
        verifyNoMoreInteractions(repository, object, mapper, dto);
    }

    @Test
    public void shouldDelegateGetByIdToTraitWithMapperAndThrow() {
        doReturn(Optional.empty()).when(trait).findById(anyString(), any());

        assertThatThrownBy(() -> trait.getById(id, mapper))
                .isInstanceOf(EntityNotFoundProblem.class);

        verify(trait).findById(id, mapper);
        verifyNoMoreInteractions(repository, mapper);
    }

    @AllArgsConstructor
    static class TestTrait implements IdentifiableTrait<TestDO, String, TestRepository> {

        @Getter
        private final TestRepository repository;

        @Override
        public Supplier<? extends EntityNotFoundProblem> notFoundById(final String id) {
            return EntityNotFoundProblem.withId("test", id);
        }
    }

    interface TestRepository extends IdentifiableRepository<TestDO, String> {
    }
}