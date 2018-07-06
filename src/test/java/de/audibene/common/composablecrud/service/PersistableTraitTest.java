package de.audibene.common.composablecrud.service;

import de.audibene.common.composablecrud.datatransferobject.TestDTO;
import de.audibene.common.composablecrud.domainobject.TestDO;
import de.audibene.common.composablecrud.problem.EntityNotFoundProblem;
import de.audibene.common.composablecrud.repository.IdentifiableRepository;
import de.audibene.common.composablecrud.repository.PersistableRepository;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class PersistableTraitTest {

    @Mock
    private TestRepository repository;

    @Spy
    @InjectMocks
    private TestTrait trait;

    private final String id = UUID.randomUUID().toString();

    @Mock
    private TestDO object;
    @Mock
    private TestDO persisted;
    @Mock
    private TestDTO dto;
    @Mock
    private Supplier<TestDO> factory;
    @Mock
    private Consumer<TestDO> updater;
    @Mock
    private Function<TestDO, TestDTO> mapper;

    @Test
    public void shouldCreateNewEntityAndDelegateToRepository() {
        doReturn(persisted).when(repository).saveAndFlush(any(TestDO.class));

        final TestDO actual = trait.create(() -> object);

        assertThat(actual).isSameAs(persisted);
        inOrder(repository, object, persisted);
        verify(repository).saveAndFlush(object);
        verifyNoMoreInteractions(repository, object, persisted);
    }


    @Test
    public void shouldCreateNewEntityAndDelegateToTraitWithMapper() {
        doReturn(persisted).when(trait).create(any());
        doReturn(dto).when(mapper).apply(any(TestDO.class));

        final TestDTO actual = trait.create(factory, mapper);

        assertThat(actual).isSameAs(dto);
        inOrder(repository, object, persisted, mapper);
        verify(trait).create(factory);
        verify(mapper).apply(persisted);
        verifyNoMoreInteractions(repository, object, persisted, mapper);
    }

    @Test
    public void shouldDelegateUpdateExistingObjectToRepository() {
        doReturn(Optional.of(object)).when(repository).findById(anyString());
        doReturn(persisted).when(repository).saveAndFlush(any(TestDO.class));

        final TestDO actual = trait.updateById(id, updater);

        assertThat(actual).isSameAs(persisted);
        inOrder(repository, object, updater);
        verify(repository).findById(id);
        verify(updater).accept(object);
        verify(repository).saveAndFlush(object);
        verifyNoMoreInteractions(repository, object, persisted, updater);
    }

    @Test
    public void shouldThrowNotFoundOnUpdateById() {
        doReturn(Optional.empty()).when(repository).findById(anyString());

        assertThatThrownBy(() -> trait.updateById(id, updater))
                .isInstanceOf(EntityNotFoundProblem.class);

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, updater);
    }

    @Test
    public void shouldDelegateUpdateExistingObjectToTraitWithMapper() {
        doReturn(persisted).when(trait).updateById(anyString(), any());
        doReturn(dto).when(mapper).apply(any(TestDO.class));

        final TestDTO actual = trait.updateById(id, updater, mapper);

        assertThat(actual).isSameAs(dto);
        inOrder(trait, object, updater);
        verify(trait).updateById(id, updater);
        verify(mapper).apply(persisted);
        verifyNoMoreInteractions(repository, persisted, updater, mapper);
    }

    @Test
    public void shouldThrowNotFoundOnUpdateByIdWithMapper() {
        EntityNotFoundProblem problem = EntityNotFoundProblem.withId("test", id).get();
        doThrow(problem).when(trait).updateById(anyString(), any());

        assertThatThrownBy(() -> trait.updateById(id, updater, mapper))
                .isInstanceOf(EntityNotFoundProblem.class);

        verify(trait).updateById(id, updater);
        verifyNoMoreInteractions(repository, updater, mapper);
    }

    @AllArgsConstructor
    static class TestTrait implements PersistableTrait<TestDO, String, TestRepository> {

        @Getter
        private final TestRepository repository;

        @Override
        public Supplier<? extends EntityNotFoundProblem> notFoundById(final String id) {
            return EntityNotFoundProblem.withId("test", id);
        }
    }

    interface TestRepository extends PersistableRepository<TestDO, String>, IdentifiableRepository<TestDO, String> {
    }
}