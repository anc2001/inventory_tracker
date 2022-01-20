package com.example;

import jakarta.inject.Singleton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class Inventory implements IInventory{
    private static final String url = "jdbc:sqlite::memory:";
    private int itemCounter = 0;
    private Connection conn;

    public Inventory() {
        connect();
    }

    private void connect() {
        conn = null;
        try {
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established");

            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The Driver name is " + meta.getDriverName());
            String sql = "CREATE TABLE IF NOT EXISTS main (\n" +
                    "  id INTEGER PRIMARY KEY,\n" +
                    "  name TEXT NOT NULL,\n" +
                    "  location TEXT NOT NULL,\n" +
                    "  quantity INTEGER NOT NULL\n" +
                    ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public JSONObject findById(@NotNull Integer id) throws Exception {
        if (conn != null) {
            String sql = "SELECT * FROM main WHERE id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            JSONObject toReturn = new JSONObject();
            while (rs.next()) {
                toReturn.put("id", rs.getInt("id"));
                toReturn.put("name", rs.getString("name"));
                toReturn.put("location", rs.getString("location"));
                toReturn.put("quantity", rs.getInt("quantity"));
            }
            return toReturn;
        } else{
            System.out.println("Connection not established");
            throw new Exception("Connection not established");
        }

    }

    @Override
    public Integer save(ItemSaveCommand command) throws Exception {
        if (conn != null) {
            int id = itemCounter++;
            String sql = "INSERT INTO main" +
                    "(id, name, location, quantity) "
                    + "VALUES(" + id
                    + ", '" + command.getName()
                    + "', '" + command.getLocation()
                    + "', " + command.getQuantity() + ")";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            return id;
        } else{
            System.out.println("Connection not established");
            throw new Exception("Connection not established");
        }
    }

    @Override
    public void deleteById(@NotNull Integer id) throws Exception {
        if (conn != null) {
            String sql = "DELETE FROM main WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.execute();
        } else {
            System.out.println("Connection not established");
            throw new Exception("Connection not established");
        }
    }

    @Override
    public void update(ItemUpdateCommand command) throws Exception {
        if (conn != null) {
            JSONObject obj = command.toJSON();
            if (!(obj.isEmpty())) {
                String sql= "UPDATE main SET ";
                String end = "WHERE id = " + command.getId();
                ArrayList<String> accum = new ArrayList<String>();
                for (Object key : obj.keySet()) {
                    if (!key.toString().equals("id")) {
                        if (key.toString().equals("name") || key.toString().equals("location")) {
                            accum.add(key + " = " + "'" +obj.get(key) + "'");
                        } else {
                            accum.add(key + " = " + obj.get(key));
                        }
                    }
                }
                String toAppend = String.join(",", accum);
                sql += toAppend + " " + end;
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } else {
            System.out.println("Connection not established");
            throw new Exception("Connection not established");
        }
    }

    @Override
    public List<JSONObject> filter(FilterArgs args) throws Exception {
        if (conn != null) {
            ArrayList ok = (ArrayList) args.getValue();
            Object value = ok.get(0);
            String sql = "SELECT * FROM main WHERE " + args.getField() + " = '" + value + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<JSONObject> toReturn = new JSONArray();
            while (rs.next()) {
                JSONObject temp = new JSONObject();
                temp.put("id", rs.getInt("id"));
                temp.put("name", rs.getString("name"));
                temp.put("location", rs.getString("location"));
                temp.put("quantity", rs.getInt("quantity"));
                toReturn.add(temp);
            }
            return toReturn;
        } else {
            System.out.println("Connection not established");
            throw new Exception("Connection not established");
        }
    }

}
