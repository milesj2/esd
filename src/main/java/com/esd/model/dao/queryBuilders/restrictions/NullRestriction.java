package com.esd.model.dao.queryBuilders.restrictions;

public class NullRestriction extends Restriction{

    public NullRestriction(String field) {
        super(field, null);
    }

    @Override
    public String generateSql() {
        return field + " IS NULL ";
    }
}
