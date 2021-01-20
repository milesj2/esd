package com.esd.controller.pagecontrollers.search.searchrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRow {
    protected int id;
    protected List<String> columns;
    protected Map<String, String> searchActions;

    public int getId() {
        return id;
    }

    public List<String> getColumns() {
        return columns;
    }

    public Map<String, String> getSearchActions() {
        return searchActions;
    }
}
