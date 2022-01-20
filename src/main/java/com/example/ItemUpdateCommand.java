package com.example;

import io.micronaut.core.annotation.Introspected;
import org.json.simple.JSONObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Introspected
public class ItemUpdateCommand {
    @NotNull
    private Integer id;

    private String name;

    private String location;

    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public ItemUpdateCommand(@NotNull Integer id){
        this.id = id;
        this.name = null;
        this.location = null;
        this.quantity = null;
    }

    public ItemUpdateCommand(@NotNull Integer id,
                             String name, String location, Integer quantity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.quantity = quantity;
    }

    public JSONObject toJSON() {
        JSONObject toReturn = new JSONObject();
        toReturn.put("id", this.id);
        if (this.name != null) {
            toReturn.put("name", this.name);
        }
        if (this.location != null) {
            toReturn.put("location", this.location);
        }
        if (this.quantity != null) {
            toReturn.put("quantity", this.quantity);
        }
        return toReturn;
    }
}
