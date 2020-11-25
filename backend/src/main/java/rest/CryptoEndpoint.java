/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entities.User;
import facades.CoinFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;

/**
 *
 * @author marcg
 */
@Path("crypto")
public class CryptoEndpoint {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CoinFacade FACADE = CoinFacade.getCoinFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetAll() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        return FACADE.GetAllCoins();
    }

    @GET
    @Path("every")
    @Produces(MediaType.APPLICATION_JSON)
    public String GetEvery() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        return FACADE.GetEveryCoins();
    }

    @GET
    @Path("all/{Currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllWithCurrency(@PathParam("Currency") String currency) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        return FACADE.GetAllCoinsWithCurrency(currency);
    }
}
