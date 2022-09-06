package org.acme;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.acme.entity.GroceryList;
import org.acme.entity.ShoppingItem;
import org.jboss.logmanager.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class GroceryListResourceTest {

    protected Long id = null;

    @BeforeEach
    public void prepareGroceryList() {
        // Create GroceryList
        GroceryList groceryList = given()
                .contentType(ContentType.JSON)
                .when().post("/groceries/{user}", "user")
                .then().statusCode(200)
                .extract().body().as(GroceryList.class);

        id = groceryList.getId();
        assertNotNull(id);
        assertEquals("user", groceryList.getCreator());

        // Retrieve it - still working
        groceryList = given()
                .contentType(ContentType.JSON)
                .when().get("/groceries/{id}", id)
                .then().statusCode(200)
                .extract().body().as(GroceryList.class);

        assertNotNull(groceryList.getShoppingItems());
        assertTrue(groceryList.getShoppingItems().isEmpty());

        // Add item to GroceryList
        ShoppingItem item = given()
                .contentType(ContentType.JSON)
                .body(new ShoppingItem().setItemName("Bananas").setItemCount(3))
                .when().post("/groceries/{id}/items", id)
                .then().statusCode(200)
                .extract().body().as(ShoppingItem.class);

        assertEquals("Bananas", item.getItemName());
        assertEquals(3, item.getItemCount());
    }

    @Test
    public void testGetListWithItemsUsingEntityGraph() {
        // Call getListWithItemsUsingEntityGraph
        final GroceryList groceryList = given()
                .contentType(ContentType.JSON)
                .when().get("/groceries/{id}/entitygraph", id)
                .then().statusCode(200)
                .extract().body().as(GroceryList.class);

        assertNotNull(groceryList.getShoppingItems());
        assertEquals(1, groceryList.getShoppingItems().size());
        assertEquals("Bananas", groceryList.getShoppingItems().get(0).getItemName());
        assertEquals(3, groceryList.getShoppingItems().get(0).getItemCount());
    }

    @Test
    public void testGetListWithItems() {
        // Call getListWithItems
        final GroceryList groceryList = given()
                .contentType(ContentType.JSON)
                .when().get("/groceries/{id}", id)
                .then().statusCode(200)
                .extract().body().as(GroceryList.class);

        assertNotNull(groceryList.getShoppingItems());
        assertEquals(1, groceryList.getShoppingItems().size());
        assertEquals("Bananas", groceryList.getShoppingItems().get(0).getItemName());
        assertEquals(3, groceryList.getShoppingItems().get(0).getItemCount());
    }

    @Test
    public void testGetItems() {
        // Call getItems
        final List itemList = given()
                .contentType(ContentType.JSON)
                .when().get("/groceries/{id}/items", id)
                .then().statusCode(200)
                .extract().body().as(List.class);


        assertEquals(1, itemList.size());
        assertInstanceOf(ShoppingItem.class, itemList.get(0));
        final ShoppingItem item = (ShoppingItem) itemList.get(0);

        assertEquals("Bananas", item.getItemName());
        assertEquals(3, item.getItemCount());
    }
}