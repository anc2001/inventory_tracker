package com.example;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;

import java.util.*;

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
    public void testFilter1() {
        HttpRequest request = HttpRequest.POST("/items", new ItemSaveCommand("pencil", "San Diego", 5));
        JSONObject obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id1 = (Integer) obj.get("id");

        request = HttpRequest.GET("/items/list?field=location&value=San%20Diego");
        List<JSONObject> items = client.toBlocking().retrieve(request, List.class);

        if (items != null) {
            JSONObject item = new JSONObject(items.get(0));
            assertEquals(id1, item.get("id"));
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void testFilter2() {
        //Create 4 different Objects
        HttpRequest request = HttpRequest.POST("/items", new ItemSaveCommand("thing1", "place", 1));
        JSONObject obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id1 = (Integer) obj.get("id");

        request = HttpRequest.POST("/items", new ItemSaveCommand("thing1", "place", 1));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id2 = (Integer) obj.get("id");

        request = HttpRequest.POST("/items", new ItemSaveCommand("thing2", "me", 1));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id3 = (Integer) obj.get("id");

        request = HttpRequest.POST("/items", new ItemSaveCommand("thing2", "place", 1));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        Integer id4 = (Integer) obj.get("id");

        //Filter those objects
        //All 4 have quantity 1
        request = HttpRequest.GET("/items/list?field=quantity&value=1");
        List<JSONObject> items = client.toBlocking().retrieve(request, List.class);

        assertEquals(4, items.size());
        Set<Integer> actual = new HashSet<>();
        Set<Integer> expected = new HashSet<>();
        expected.add(id1);
        expected.add(id2);
        expected.add(id3);
        expected.add(id4);
        if (items != null) {
            for (Map temp : items) {
                JSONObject item = new JSONObject(temp);
                actual.add((Integer) item.get("id"));
            }
        } else {
            assertTrue(false);
        }
        assertEquals(expected, actual);

        //name = thing1
        request = HttpRequest.GET("/items/list?field=name&value=thing1");
        items = client.toBlocking().retrieve(request, List.class);

        assertEquals(2, items.size());
        actual = new HashSet<>();
        expected = new HashSet<>();
        expected.add(id1);
        expected.add(id2);
        if (items != null) {
            for (Map temp : items) {
                JSONObject item = new JSONObject(temp);
                actual.add((Integer) item.get("id"));
            }
        } else {
            assertTrue(false);
        }
        assertEquals(expected, actual);

        //location = place
        request = HttpRequest.GET("/items/list?field=location&value=place");
        items = client.toBlocking().retrieve(request, List.class);

        assertEquals(3, items.size());
        actual = new HashSet<>();
        expected = new HashSet<>();
        expected.add(id1);
        expected.add(id2);
        expected.add(id4);
        if (items != null) {
            for (Map temp : items) {
                JSONObject item = new JSONObject(temp);
                actual.add((Integer) item.get("id"));
            }
        } else {
            assertTrue(false);
        }
        assertEquals(expected, actual);

        //name = Adrian
        request = HttpRequest.GET("/items/list?field=name&value=Adrian");
        items = client.toBlocking().retrieve(request, List.class);

        assertEquals(0, items.size());

        //Delete those 4 objects
        request = HttpRequest.DELETE("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);

        request = HttpRequest.DELETE("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);

        request = HttpRequest.DELETE("/items/" + id3);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id3);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals(obj.keySet().size(), 1);

        request = HttpRequest.DELETE("/items/" + id4);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id4);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

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

        assertEquals("ok", obj.get("status"));
        assertEquals("pencil", obj.get("name"));
        assertEquals("San Diego", obj.get("location"));
        assertEquals(5, obj.get("quantity"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
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
        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id1);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals("pencil", obj.get("name"));
        assertEquals("Ireland", obj.get("location"));
        assertEquals(2, obj.get("quantity"));

        //Update item 2 and check that it has been updated correctly=
        request = HttpRequest.PUT("/items", new ItemUpdateCommand(id2, "thing", "place", 400));
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
        assertEquals("ok", obj.get("status"));

        request = HttpRequest.GET("/items/" + id2);
        obj = client.toBlocking().retrieve(request, JSONObject.class);

        assertEquals("ok", obj.get("status"));
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
