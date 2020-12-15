package com.esd.model.dao.queryBuilders.joins;

public abstract class Join {
    String table;
    String id1;
    String id2;

    public Join(String table, String id1, String id2) {
        this.table = table;
        this.id1 = id1;
        this.id2 = id2;
    }

    public abstract String generateSQL();
}
