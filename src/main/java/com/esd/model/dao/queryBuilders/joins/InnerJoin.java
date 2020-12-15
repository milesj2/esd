package com.esd.model.dao.queryBuilders.joins;

public class InnerJoin extends Join{

    public InnerJoin(String table, String id1, String id2) {
        super(table, id1, id2);
    }

    @Override
    public String generateSQL() {
        return "inner join " + table + " on " + id1 + "=" + id2;
    }
}
