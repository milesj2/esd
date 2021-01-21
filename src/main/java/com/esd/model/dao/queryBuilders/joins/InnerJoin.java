package com.esd.model.dao.queryBuilders.joins;

public class InnerJoin extends Join{

    public InnerJoin(String table, String id1, String id2) {
        super(table, id1, id2);
    }

    public InnerJoin(String table, String alias, String id1, String id2) {
        super(table, alias, id1, id2);
    }

    @Override
    public String generateSQL() {
        String base = "inner join " + table;
        String sqlAlias = alias != null ? " " + alias : "";
        String restriction = " on " + id1 + "=" + id2;
        return  base + sqlAlias + restriction;
    }
}
