package de.audibene.common.composablecrud.service;


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
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ArchivableTraitTest {

    @Mock
    private TestRepository repository;

    @Spy
    @InjectMocks
    private TestTrait trait;

    private final String id = UUID.randomUUID().toString();

    @Mock
    private TestDO object;

    @Test
    public void shouldDelegateToRepositoryOnArchive() {
        trait.archive(object);

        inOrder(repository, object);
        verify(object).archive();
        verify(repository).saveAndFlush(object);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldDelegateToTraitOnArchiveById() {
        doReturn(Optional.of(object)).when(repository).findById(anyString());
        doNothing().when(trait).archive(any(TestDO.class));

        trait.archive(id);

        verify(repository).findById(id);
        verify(trait).archive(object);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldThrowNotFoundOnArchiveById() {
        doReturn(Optional.empty()).when(repository).findById(anyString());

        assertThatThrownBy(() -> trait.archive(id)).isInstanceOf(EntityNotFoundProblem.class);

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, object);
    }

    @AllArgsConstructor
    static class TestTrait implements ArchivableTrait<TestDO, String, TestRepository> {

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