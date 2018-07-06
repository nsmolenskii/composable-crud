package de.audibene.common.composablecrud.service;

import com.querydsl.core.types.Predicate;
import de.audibene.common.composablecrud.accessor.RepositoryAccessor;
import de.audibene.common.composablecrud.domainobject.Identifiable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

public interface QueryableTrait<
        DO extends Identifiable<ID>,
        ID extends Serializable,
        Repository extends QueryDslPredicateExecutor<DO>
        > extends RepositoryAccessor<Repository> {

    @Transactional(readOnly = true)
    default Page<DO> findAll(final Predicate predicate, final Pageable pageable) {
        final Predicate actualPredicate = wrap(predicate);
        return getRepository().findAll(actualPredicate, pageable);
    }

    @Transactional(readOnly = true)
    default <DTO> Page<DTO> findAll(final Predicate predicate, final Pageable pageable, final Function<DO, DTO> mapper) {
        return findAll(predicate, pageable).map(mapper::apply);
    }

    @Transactional(readOnly = true)
    default Optional<DO> findOne(final Predicate predicate) {
        final Predicate actualPredicate = wrap(predicate);
        return Optional.ofNullable(getRepository().findOne(actualPredicate));
    }

    @Transactional(readOnly = true)
    default <DTO> Optional<DTO> findOne(Predicate predicate, Function<DO, DTO> mapper) {
        return findOne(predicate).map(mapper);
    }

    default Predicate wrap(final Predicate predicate) {
        return predicate;
    }

}
