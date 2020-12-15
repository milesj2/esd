package com.esd.model.dao.queryBuilders.restrictions;

public class EqualRestriction extends Restriction{
    public EqualRestriction(String field, Object value) {
        super(field, value);
    }

    @Override
    public String generateSql() {
        return field + "=?";
    }
}
