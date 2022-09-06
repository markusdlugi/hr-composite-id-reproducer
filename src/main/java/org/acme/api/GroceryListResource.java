package org.acme.api;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.entity.GroceryList;
import org.acme.entity.ShoppingItem;
import org.acme.repository.GroceryListRepository;
import org.acme.repository.ShoppingItemRepository;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@Path("/groceries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ReactiveTransactional
public class GroceryListResource {

    @Inject
    GroceryListRepository listRepository;

    @Inject
    ShoppingItemRepository itemRepository;

    @POST
    @Path("/{user}")
    public Uni<GroceryList> createList(final String user) {
        return listRepository.persist(new GroceryList().setCreator(user));
    }

    @GET
    @Path("/{id}")
    public Uni<GroceryList> getListWithItems(final Long id) {
        return listRepository.findByIdWithShoppingItemsUsingJoin(id);
    }

    @GET
    @Path("/{id}/entitygraph")
    public Uni<GroceryList> getListWithItemsUsingEntityGraph(final Long id) {
        return listRepository.findByIdWithShoppingItemsUsingEntityGraph(id);
    }

    @DELETE
    public Uni<Boolean> deleteList(final Long id) {
        return listRepository.deleteById(id);
    }

    @POST
    @Path("/{id}/items")
    public Uni<ShoppingItem> addItem(final Long id, final ShoppingItem item) {
        return itemRepository.persist(id, new ShoppingItem().setItemName(item.getItemName()).setItemCount(item.getItemCount()));
    }

    @GET
    @Path("/{id}/items")
    public Uni<List<ShoppingItem>> getItems(final Long id) {
        return itemRepository.findByGroceryList(id);
    }

    @DELETE
    @Path("/{id}/items/{itemName}")
    public Uni<Boolean> removeItem(final Long id, final String itemName) {
        return itemRepository.deleteById(id, itemName);
    }
}