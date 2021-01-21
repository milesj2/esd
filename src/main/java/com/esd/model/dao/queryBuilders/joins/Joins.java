package com.esd.model.dao.queryBuilders.joins;

public class Joins {

    public static InnerJoin innerJoin(String table, String id1, String id2){
        return new InnerJoin(table, id1, id2);
    }
    public static InnerJoin innerJoin(String table, String alias, String id1, String id2){
        return new InnerJoin(table, alias, id1, id2);
    }
}
