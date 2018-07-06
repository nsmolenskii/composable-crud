package de.audibene.common.composablecrud.service;

import de.audibene.common.composablecrud.accessor.RepositoryAccessor;
import de.audibene.common.composablecrud.domainobject.Identifiable;
import de.audibene.common.composablecrud.repository.RemovableRepository;

import java.io.Serializable;

public interface RemovableTrait<
        DO extends Identifiable<ID>,
        ID extends Serializable,
        Repository extends RemovableRepository<DO, ID>
        >
        extends RepositoryAccessor<Repository> {

    default void delete(final ID id) {
        getRepository().delete(id);
    }

    default void delete(final DO entity) {
        delete(entity.getId());
    }
}
