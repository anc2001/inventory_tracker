package com.example;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.logging.Filter;

/**
 * Filter args, can only search on one condition for now
 */
@Introspected
public class FilterArgs {

    @NotNull
    @Pattern(regexp = "id|name|location|quantity")
    private String field;

    @NotNull
    private Object value;

    public FilterArgs() {}

    public FilterArgs(String field, Object value) {}

    public String getField() {
        return field;
    }

    public void setField(@NotNull String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(@NotNull Object value) {
        this.value = value;
    }
}
