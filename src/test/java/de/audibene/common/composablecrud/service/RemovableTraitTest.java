package de.audibene.common.composablecrud.service;


import de.audibene.common.composablecrud.domainobject.TestDO;
import de.audibene.common.composablecrud.repository.RemovableRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class RemovableTraitTest {

    @Mock
    private TestRepository repository;

    @Spy
    @InjectMocks
    private TestTrait trait;

    private final String id = UUID.randomUUID().toString();

    @Mock
    private TestDO object;

    @Test
    public void shouldDelegateToRepositoryOnDelete() {
        doReturn(id).when(object).getId();

        trait.delete(object);

        verify(object).getId();
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository, object);
    }

    @Test
    public void shouldDelegateToRepositoryOnDeleteById() {
        trait.delete(id);

        verify(repository).delete(id);
        verifyNoMoreInteractions(repository, object);
    }

    @AllArgsConstructor
    static class TestTrait implements RemovableTrait<TestDO, String, TestRepository> {

        @Getter
        private final TestRepository repository;

    }

    interface TestRepository extends RemovableRepository<TestDO, String> {
    }
}