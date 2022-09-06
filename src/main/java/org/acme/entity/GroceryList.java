package org.acme.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class GroceryList implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String creator;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant creationTime;

    @OneToMany(mappedBy = "groceryList", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingItem> shoppingItems;

    public Long getId() {
        return id;
    }

    public GroceryList setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public GroceryList setCreator(final String creator) {
        this.creator = creator;
        return this;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public GroceryList setCreationTime(final Instant creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public List<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public GroceryList setShoppingItems(final List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
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
        final GroceryList that = (GroceryList) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
