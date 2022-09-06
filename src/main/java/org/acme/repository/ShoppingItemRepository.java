package org.acme.repository;

import static io.quarkus.panache.common.Parameters.with;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.acme.entity.GroceryList;
import org.acme.entity.ShoppingItem;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ShoppingItemRepository implements PanacheRepositoryBase<ShoppingItem, ShoppingItem.ShoppingItemId> {

    public Uni<ShoppingItem> persist(final Long id, final ShoppingItem shoppingItem) {
        return this.getSession().onItem().transformToUni(session ->
                session.persist(shoppingItem.setGroceryList(session.getReference(GroceryList.class, id))))
                .replaceWith(shoppingItem);
    }

    public Uni<List<ShoppingItem>> findByGroceryList(final Long id) {
        return list("grocerylistid", id);
    }

    public Uni<Boolean> deleteById(final Long id, final String itemName) {
        return delete("grocerylistid = :id AND itemname = :itemName", with("id", id).and("itemName", itemName))
                .onItem().transform(x -> x > 0);
    }
}
