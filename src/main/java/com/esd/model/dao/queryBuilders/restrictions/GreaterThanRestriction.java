package com.esd.model.dao.queryBuilders.restrictions;

public class GreaterThanRestriction extends Restriction{
    boolean inclusive = false;

    public GreaterThanRestriction(String field, Object value, boolean inclusive) {
        super(field, value);
        this.inclusive = inclusive;
    }

    @Override
    public String generateSql() {
        if(inclusive){
            return field + ">=?";
        }
        return field + ">?";
    }
}
