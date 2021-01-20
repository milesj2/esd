package com.esd.controller.pagecontrollers.search;

public class SearchColumn {
    String field;
    String name;
    String type;

    public SearchColumn(String field, String name, String type) {
        this.field = field;
        this.name = name;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
