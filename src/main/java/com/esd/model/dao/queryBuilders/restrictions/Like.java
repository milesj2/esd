package com.esd.model.dao.queryBuilders.restrictions;

public class Like extends Restriction{

    public Like(String field, Object value) {
        super(field, value);
    }

    @Override
    public String generateSql() {
        //this || sybol is derbys way of concatenating strings
        return " lower(" + field + ") like lower('%' || ? || '%') ";
    }
}
