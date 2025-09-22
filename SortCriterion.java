// SortCriterion.java
package com.example.camunda.operate.dto;

public class SortCriterion {
    private String field;
    private SortOrder order;

    public SortCriterion() {}

    public SortCriterion(String field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }
}



// SortOrder.java
package com.example.camunda.operate.dto;

public enum SortOrder {
    ASC, DESC
}


List<SortCriterion> sort = List.of(new SortCriterion("version", SortOrder.DESC));