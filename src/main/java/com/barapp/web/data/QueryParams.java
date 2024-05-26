package com.barapp.web.data;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Query;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Getter
public class QueryParams {
    private final List<Filter> filters = new ArrayList<>();
    private final List<Entry<String, Query.Direction>> orders = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void addOrder(String property, Query.Direction direction) {
        orders.add(Map.entry(property, direction));
    }
}
