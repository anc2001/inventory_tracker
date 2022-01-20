package com.example;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class InventoryControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testInvalidQuery() {
        HttpRequest request = HttpRequest.GET("/items/910");
        JSONObject obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);
    }

    @Test
    public void testCRUD() {
        //Create two objects and ensure that they have been successfully created
        HttpRequest request = HttpRequest.POST("/items", new ItemSaveCommand("pencil", "San Diego", 5));
        JSONObject obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id1 = (Integer) obj.get("id");

        request = HttpRequest.POST("/items", new ItemSaveCommand("pen", "Carlsbad", 20));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id2 = (Integer) obj.get("id");

        request = HttpRequest.GET("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("pencil", obj.get("name"));
        assertEquals("San Diego", obj.get("location"));
        assertEquals(5, obj.get("quantity"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("pen", obj.get("name"));
        assertEquals("Carlsbad", obj.get("location"));
        assertEquals(20, obj.get("quantity"));

        //Update item 1 and check that it has been updated correctly
        ItemUpdateCommand command = new ItemUpdateCommand(id1);
        command.setLocation("Ireland");
        command.setQuantity(2);
        request = HttpRequest.PUT("/items", command);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("pencil", obj.get("name"));
        assertEquals("Ireland", obj.get("location"));
        assertEquals(2, obj.get("quantity"));

        //Update item 2 and check that it has been updated correctly=
        request = HttpRequest.PUT("/items", new ItemUpdateCommand(id2, "thing", "place", 400));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("thing", obj.get("name"));
        assertEquals("place", obj.get("location"));
        assertEquals(400, obj.get("quantity"));

        //Delete item 1
        request = HttpRequest.DELETE("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);

        //Delete item 2
        request = HttpRequest.DELETE("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);
    }
}
