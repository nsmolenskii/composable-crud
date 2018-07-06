package de.audibene.common.composablecrud.repository;

import de.audibene.common.composablecrud.domainobject.Identifiable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

public interface IdentifiableRepository<DO extends Identifiable<ID>, ID extends Serializable> {

    @Transactional(propagation = MANDATORY, readOnly = true)
    Optional<DO> findById(ID id);

}
