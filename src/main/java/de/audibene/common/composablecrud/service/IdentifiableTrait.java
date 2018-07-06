package de.audibene.common.composablecrud.service;

import de.audibene.common.composablecrud.accessor.NotFoundExceptionAccessor;
import de.audibene.common.composablecrud.accessor.RepositoryAccessor;
import de.audibene.common.composablecrud.domainobject.Identifiable;
import de.audibene.common.composablecrud.problem.EntityNotFoundProblem;
import de.audibene.common.composablecrud.repository.IdentifiableRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

public interface IdentifiableTrait<
        DO extends Identifiable<ID>,
        ID extends Serializable,
        Repository extends IdentifiableRepository<DO, ID>
        >
        extends RepositoryAccessor<Repository>, NotFoundExceptionAccessor<ID> {


    @Transactional(readOnly = true)
    default Optional<DO> findById(final ID id) {
        return getRepository().findById(id);
    }

    @Transactional(readOnly = true)
    default <DTO> Optional<DTO> findById(final ID id, final Function<DO, DTO> mapper) {
        return findById(id).map(mapper);
    }

    @Transactional(readOnly = true)
    default DO getById(final ID id) throws EntityNotFoundProblem {
        return findById(id).orElseThrow(notFoundById(id));
    }

    @Transactional(readOnly = true)
    default <DTO> DTO getById(final ID id, final Function<DO, DTO> mapper) throws EntityNotFoundProblem {
        return findById(id, mapper).orElseThrow(notFoundById(id));
    }

}
