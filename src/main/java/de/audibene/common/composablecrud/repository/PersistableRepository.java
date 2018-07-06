package de.audibene.common.composablecrud.repository;

import de.audibene.common.composablecrud.domainobject.Identifiable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

public interface PersistableRepository<DO extends Identifiable<ID>, ID extends Serializable> {

    @Modifying
    @Transactional(propagation = MANDATORY)
    DO saveAndFlush(DO entity);

}
