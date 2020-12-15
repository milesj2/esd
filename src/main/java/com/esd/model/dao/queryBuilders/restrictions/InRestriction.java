package com.esd.model.dao.queryBuilders.restrictions;

public class InRestriction extends Restriction{

    public InRestriction(String field, Object[] values) {
        super(field, values);
    }

    @Override
    public String generateSql() {
        String sql = field + " in (";

        for (int i = 0; i < values.length; i++) {
            if(i != 0){
                sql += ",";
            }
            sql += " ?";
        }

        sql += ")";
        return sql;
    }
}
