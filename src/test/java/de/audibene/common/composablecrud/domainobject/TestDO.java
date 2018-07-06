package de.audibene.common.composablecrud.domainobject;

import lombok.Data;

@Data
public class TestDO implements Identifiable<String>, Archivable {

    private String id;
    private boolean archived;

    @Override
    public void archive() {
        setArchived(false);
    }
}
