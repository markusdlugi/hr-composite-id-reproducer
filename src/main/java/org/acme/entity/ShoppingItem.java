package org.acme.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@IdClass(ShoppingItem.ShoppingItemId.class)
public class ShoppingItem implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "grocerylistid")
    @JsonIgnore
    private GroceryList groceryList;

    @Id
    private String itemName;

    private int itemCount;

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public ShoppingItem setGroceryList(final GroceryList groceryList) {
        this.groceryList = groceryList;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public ShoppingItem setItemName(final String itemName) {
        this.itemName = itemName;
        return this;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ShoppingItem setItemCount(final int itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public static class ShoppingItemId implements Serializable {

        private String itemName;
        private GroceryList groceryList;

        public String getItemName() {
            return itemName;
        }

        public ShoppingItemId setItemName(final String itemName) {
            this.itemName = itemName;
            return this;
        }

        public GroceryList getGroceryList() {
            return groceryList;
        }

        public ShoppingItemId setGroceryList(final GroceryList groceryList) {
            this.groceryList = groceryList;
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final ShoppingItemId that = (ShoppingItemId) o;
            return itemName.equals(that.itemName) && groceryList.equals(that.groceryList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemName, groceryList);
        }
    }
}
