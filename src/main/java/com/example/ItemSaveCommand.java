package com.example;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Introspected
public class ItemSaveCommand {
    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @PositiveOrZero
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(@NotBlank String location) {
        this.location = location;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@PositiveOrZero Integer quantity) {
        this.quantity = quantity;
    }

    public ItemSaveCommand(){}

    public ItemSaveCommand(@NotBlank String name, @NotBlank String location, @PositiveOrZero Integer quantity) {
        this.name = name;
        this.location = location;
        this.quantity = quantity;
    }

}
