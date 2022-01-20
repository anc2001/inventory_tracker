package com.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for an inventory, all throw exceptions for error checking
 * @Valid present to validate commands passed in
 */
public interface IInventory {

    /**
     * Returns item information in JSON format given id,
     * @param id integer id, must be non null
     * @return JSON
     * @throws Exception SQL Exception, or Custom
     */
    JSONObject findById(@NotNull Integer id) throws Exception;

    /**
     * Deletes item given id
     * @param id integer id, must be non null
     * @throws Exception SQL Exception, or Custom
     */
    void deleteById(@NotNull Integer id) throws Exception;

    /**
     * Saves an item
     * Note for future - make ItemSaveCommand capable of having null fields
     * @param command fields for item saved
     * @return the id of the item saved
     * @throws Exception SQL Exception, or Custom
     */
    Integer save(@Valid ItemSaveCommand command) throws Exception;

    /**
     * Will update an inventory item with an id with the given command
     * Note for future - make ItemUpdateCommand and method generalizable to editing off of any field
     * @param command fields to update the item with
     * @throws Exception SQL Exception, or Custom
     */
    void update(@Valid ItemUpdateCommand command) throws Exception;

    /**
     * Will return a list of items matching the arguments, only one field right now
     * Note for future - make generalizable for multiple field queries
     * @param args field and value to match items with
     * @return List of items matching field and value
     * @throws Exception SQL Exception, or Custom
     */
    List<JSONObject> filter(@Valid FilterArgs args) throws Exception;

}
