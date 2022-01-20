package com.example;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.logging.Filter;

@Introspected
public class FilterCommand {

    @NotBlank
    private String field;

    @NotNull
    private Object value;

    public FilterCommand() {}

    public FilterCommand(@NotBlank  String field, @NotNull Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
