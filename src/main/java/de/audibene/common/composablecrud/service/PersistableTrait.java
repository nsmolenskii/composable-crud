package de.audibene.common.composablecrud.service;

import de.audibene.common.composablecrud.accessor.NotFoundExceptionAccessor;
import de.audibene.common.composablecrud.accessor.RepositoryAccessor;
import de.audibene.common.composablecrud.domainobject.Identifiable;
import de.audibene.common.composablecrud.problem.EntityNotFoundProblem;
import de.audibene.common.composablecrud.repository.IdentifiableRepository;
import de.audibene.common.composablecrud.repository.PersistableRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Transactional
public interface PersistableTrait<
        DO extends Identifiable<ID>,
        ID extends Serializable,
        Repository extends PersistableRepository<DO, ID> & IdentifiableRepository<DO, ID>
        >
        extends RepositoryAccessor<Repository>, NotFoundExceptionAccessor<ID> {

    default DO create(final Supplier<DO> factory) {
        return getRepository().saveAndFlush(factory.get());
    }

    default <DTO> DTO create(final Supplier<DO> factory, Function<DO, DTO> mapper) {
        return mapper.apply(create(factory));
    }

    default DO updateById(final ID id, final Consumer<DO> updater) throws EntityNotFoundProblem {
        final DO entity = getRepository().findById(id).orElseThrow(notFoundById(id));
        updater.accept(entity);
        return getRepository().saveAndFlush(entity);
    }

    default <DTO> DTO updateById(final ID id, final Consumer<DO> updater, final Function<DO, DTO> mapper) throws EntityNotFoundProblem {
        return mapper.apply(updateById(id, updater));
    }

}
