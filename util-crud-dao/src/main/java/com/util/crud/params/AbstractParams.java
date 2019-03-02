package com.util.crud.params;

public class AbstractParams {

    @QueryParam(column = "id", cause = Cause.EQ)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
