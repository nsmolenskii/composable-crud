package de.audibene.common.composablecrud.service;

import com.querydsl.core.types.Predicate;
import de.audibene.common.composablecrud.datatransferobject.TestDTO;
import de.audibene.common.composablecrud.domainobject.TestDO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class QueryableTraitTest {

    @Mock
    private TestRepository repository;

    @Spy
    @InjectMocks
    private TestTrait trait;

    @Mock
    private TestDO object;
    @Mock
    private TestDTO dto;
    @Mock
    private Function<TestDO, TestDTO> mapper;
    @Mock
    private Predicate predicate;
    @Mock
    private Predicate actualPredicate;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<TestDO> objectPage;
    @Mock
    private Page<TestDTO> dtoPage;

    @Test
    public void shouldDelegateFindOneToRepository() {
        doReturn(actualPredicate).when(trait).wrap(any(Predicate.class));
        doReturn(object).when(repository).findOne(any(Predicate.class));

        final Optional<TestDO> actual = trait.findOne(predicate);

        assertThat(actual).containsSame(object);
        inOrder(trait, repository, predicate, actualPredicate);
        verify(trait).wrap(predicate);
        verify(repository).findOne(actualPredicate);
        verifyNoMoreInteractions(repository, predicate, actualPredicate);
    }

    @Test
    public void shouldDelegateFindOneToRepositoryAndAcceptNull() {
        doReturn(actualPredicate).when(trait).wrap(any(Predicate.class));
        doReturn(null).when(repository).findOne(any(Predicate.class));

        final Optional<TestDO> actual = trait.findOne(predicate);

        assertThat(actual).isEmpty();
        inOrder(trait, repository, predicate, actualPredicate);
        verify(trait).wrap(predicate);
        verify(repository).findOne(actualPredicate);
        verifyNoMoreInteractions(repository, predicate, actualPredicate);
    }

    @Test
    public void shouldDelegateFindOnwWithMapperToTrait() {
        doReturn(Optional.of(object)).when(trait).findOne(any(Predicate.class));
        doReturn(dto).when(mapper).apply(any(TestDO.class));

        final Optional<TestDTO> actual = trait.findOne(predicate, mapper);

        assertThat(actual).containsSame(dto);
        inOrder(trait, repository, predicate);
        verify(trait).findOne(predicate);
        verify(mapper).apply(object);
        verifyNoMoreInteractions(repository, predicate, mapper);
    }

    @Test
    public void shouldDelegateFindOnwWithMapperToTraitAndAcceptEmpty() {
        doReturn(Optional.empty()).when(trait).findOne(any(Predicate.class));

        final Optional<TestDTO> actual = trait.findOne(predicate, mapper);

        assertThat(actual).isEmpty();
        inOrder(trait, repository, predicate);
        verify(trait).findOne(predicate);
        verifyNoMoreInteractions(repository, predicate, mapper);
    }

    @Test
    public void shouldDelegateFindAllToRepository() {
        doReturn(actualPredicate).when(trait).wrap(any(Predicate.class));
        doReturn(objectPage).when(repository).findAll(any(Predicate.class), any(Pageable.class));

        final Page<TestDO> actual = trait.findAll(predicate, pageable);

        assertThat(actual).isSameAs(objectPage);
        inOrder(trait, repository, predicate, actualPredicate);
        verify(trait).wrap(predicate);
        verify(repository).findAll(actualPredicate, pageable);
        verifyNoMoreInteractions(repository, predicate, actualPredicate);
    }

    @Test
    public void shouldDelegateFindAllToTraitWithMapper() {
        doReturn(objectPage).when(trait).findAll(any(Predicate.class), any(Pageable.class));
        doReturn(dtoPage).when(objectPage).map(any());

        final Page<TestDTO> actual = trait.findAll(predicate, pageable, mapper);

        assertThat(actual).isSameAs(dtoPage);
        inOrder(trait, repository, predicate);
        verify(trait).findAll(predicate, pageable);
        verifyNoMoreInteractions(repository, predicate, mapper);
    }

    @AllArgsConstructor
    static class TestTrait implements QueryableTrait<TestDO, String, TestRepository> {

        @Getter
        private final TestRepository repository;

    }

    interface TestRepository extends QueryDslPredicateExecutor<TestDO> {
    }
}