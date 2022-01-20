package com.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@ExecuteOn(TaskExecutors.IO)
@Controller("/items")
public class InventoryController {

    protected final IInventory inventory;

    public InventoryController(IInventory inventory) {
        this.inventory = inventory;
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject show(@NotNull Integer id) {
        JSONObject obj = new JSONObject();
        try {
            obj = inventory.findById(id);
            obj.put("status", "ok");
        } catch (Exception e) {
            obj.put("status", "failed");
            obj.put("message", e.getMessage());
        }
        return obj;
    }

    @Get("/list{?args*}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JSONObject> filter(@NotNull FilterArgs args) {
        try {
            return inventory.filter(args);
        } catch (Exception e) {
            return null;
        }
    }

    @Delete("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject delete(@NotNull Integer id) {
        JSONObject obj = new JSONObject();
        try {
            inventory.deleteById(id);
            obj.put("status", "ok");
        } catch (Exception e) {
            obj.put("status", "failed");
            obj.put("message", e.getMessage());
        }
        return obj;
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject save(@Valid ItemSaveCommand command) {
        JSONObject obj = new JSONObject();
        try {
            int id = inventory.save(command);
            obj.put("status", "ok");
            obj.put("id", id);
        } catch (Exception e) {
            obj.put("status", "failed");
            obj.put("message", e.getMessage());
        }
        return obj;
    }

    @Put
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject update(@Valid ItemUpdateCommand command) {
        JSONObject obj = new JSONObject();
        try {
            inventory.update(command);
            obj.put("status", "ok");
        } catch (Exception e) {
            obj.put("status", "failed");
            obj.put("message", e.getMessage());
        }
        return obj;
    }
}
