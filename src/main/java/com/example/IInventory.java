package com.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public interface IInventory {

    JSONObject findById(@NotNull Integer id) throws Exception;

    void deleteById(@NotNull Integer id) throws Exception;

    Integer save(@Valid ItemSaveCommand command) throws Exception;

    void update(@Valid ItemUpdateCommand command) throws Exception;

    List<JSONObject> filter(@Valid FilterArgs args) throws Exception;

}
