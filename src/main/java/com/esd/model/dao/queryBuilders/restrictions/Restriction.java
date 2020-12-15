package com.esd.model.dao.queryBuilders.restrictions;

public abstract class Restriction {
    String field;
    Object[] values;
    String operation = "AND";

    public abstract String generateSql();

    public Restriction(String field, Object value) {
        this.field = field;
        this.values = new Object[]{value};
    }

    public Restriction(String field, Object[] values) {
        this.field = field;
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    public String getField() {
        return field;
    }

    public String getOperator() {
        return operation;
    }

    public void setOperation(String operation){
        this.operation = operation;
    }
}
