package com.esd.model.dao.queryBuilders.restrictions;

public class Restrictions {

    public static Restriction equalsRestriction(String field, Object value){
        return new EqualRestriction(field, value);
    }

    public static Restriction notEqualsRestriction(String field, Object value){
        return new NotEqualRestriction(field, value);
    }

    public static Restriction lessThanInclusive(String field, Object value){
        return new LessThanRestriction(field, value, true);
    }

    public static Restriction lessThanExclusive(String field, Object value){
        return new LessThanRestriction(field, value, false);
    }

    public static Restriction greaterThanInclusive(String field, Object value){
        return new GreaterThanRestriction(field, value, true);
    }

    public static Restriction greaterThanExclusive(String field, Object value){
        return new GreaterThanRestriction(field, value, false);
    }

    public static Restriction in(String field, Object... values){
        return new InRestriction(field, values);
    }
}
