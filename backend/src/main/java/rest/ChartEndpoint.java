/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entities.User;
import errorhandling.InvalidInputException;
import facades.ChartFacade;
import facades.HistoryFacade;
import facades.CoinFacade;
import java.io.IOException;
import java.text.ParseException;
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
@Path("chart")
public class ChartEndpoint {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static ChartFacade FACADE = ChartFacade.getChartFacade(EMF);
    private static HistoryFacade HISTORY = HistoryFacade.getChartFacade(EMF);

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
    @Path("data")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChartData() {
        return FACADE.getChartData();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChartData(@PathParam("name") String name) {
        return FACADE.getChartByName(name);
    }

//    @GET
//    @Path("start")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String startHistory() throws IOException, InterruptedException, ExecutionException, TimeoutException {
//        HistoryFacade.getChartFacade(EMF);
//
//        return "{msg: \"history started \"}";
//    }
    @GET
    @Path("history")
    @Produces(MediaType.APPLICATION_JSON)
    public String makeHistory() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        

        return HISTORY.addCoinsToDb();
    }

    @GET
    @Path("coins")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        return HISTORY.getCoins();
    }

    @GET
    @Path("coinvalues")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCoinvalues() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        return HISTORY.getCoinvalues();
    }
}
