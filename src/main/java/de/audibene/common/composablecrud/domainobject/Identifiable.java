package de.audibene.common.composablecrud.domainobject;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {

    ID getId();

}
