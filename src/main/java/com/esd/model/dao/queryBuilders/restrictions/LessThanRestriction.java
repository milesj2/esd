package com.esd.model.dao.queryBuilders.restrictions;

public class LessThanRestriction extends Restriction{
    boolean inclusive = false;

     public LessThanRestriction(String field, Object value, boolean inclusive) {
        super(field, value);
        this.inclusive = inclusive;
    }

    @Override
    public String generateSql() {
        if(inclusive){
            return field + "<=?";
        }
        return field + "<?";
    }
}
