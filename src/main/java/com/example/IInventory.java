package com.example;

import org.json.simple.JSONObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface IInventory {

    JSONObject findById(@NotNull Integer id) throws Exception;

    void deleteById(@NotNull Integer id) throws Exception;

    Integer save(ItemSaveCommand command) throws Exception;

    void update(ItemUpdateCommand command) throws Exception;

    List<JSONObject> filter(String field, Object value) throws Exception;

}
