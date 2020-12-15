package com.esd.model.dao.queryBuilders.restrictions;

public class NotEqualRestriction extends Restriction{
    public NotEqualRestriction(String field, Object value) {
        super(field, value);
    }

    @Override
    public String generateSql() {
        return field + "!=?";
    }
}