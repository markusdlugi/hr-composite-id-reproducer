package org.acme.repository;

import static io.quarkus.panache.common.Parameters.with;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityGraph;

import org.acme.entity.GroceryList;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class GroceryListRepository implements PanacheRepository<GroceryList> {

    public Uni<GroceryList> findByIdWithShoppingItemsUsingJoin(final Long id) {
        return find("FROM GroceryList g LEFT JOIN FETCH g.shoppingItems WHERE id = :id", with("id", id)).singleResult();
    }

    public Uni<GroceryList> findByIdWithShoppingItemsUsingEntityGraph(final Long id) {
        return this.getSession().onItem().transformToUni(session -> {
            EntityGraph<GroceryList> entityGraph = session.createEntityGraph(GroceryList.class);
            entityGraph.addAttributeNodes("shoppingItems");
            return session.find(entityGraph, id);
        });
    }
}
