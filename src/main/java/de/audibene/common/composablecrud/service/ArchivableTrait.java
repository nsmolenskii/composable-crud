package de.audibene.common.composablecrud.service;

import de.audibene.common.composablecrud.accessor.NotFoundExceptionAccessor;
import de.audibene.common.composablecrud.accessor.RepositoryAccessor;
import de.audibene.common.composablecrud.domainobject.Archivable;
import de.audibene.common.composablecrud.domainobject.Identifiable;
import de.audibene.common.composablecrud.repository.IdentifiableRepository;
import de.audibene.common.composablecrud.repository.PersistableRepository;

import java.io.Serializable;

public interface ArchivableTrait<
        DO extends Identifiable<ID> & Archivable,
        ID extends Serializable,
        Repository extends PersistableRepository<DO, ID> & IdentifiableRepository<DO, ID>
        >
        extends RepositoryAccessor<Repository>, NotFoundExceptionAccessor<ID> {

    default void archive(final ID id) {
        archive(getRepository().findById(id).orElseThrow(notFoundById(id)));
    }

    default void archive(final DO entity) {
        entity.archive();
        getRepository().saveAndFlush(entity);
    }

}
