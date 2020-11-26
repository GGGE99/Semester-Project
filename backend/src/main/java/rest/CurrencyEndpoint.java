/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import errorhandling.InvalidInputException;
import facades.CoinFacade;
import facades.CurrencyFacade;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
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
@Path("currency")
public class CurrencyEndpoint {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CurrencyFacade FACADE = CurrencyFacade.getCurrencyFacade(EMF);
    private static CoinFacade COINFACADE;

    public CurrencyEndpoint() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        COINFACADE = CoinFacade.getCoinFacade(EMF);
    }
    

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
    public String getAll() throws IOException {
        return FACADE.getAll();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getACoinByName(@PathParam("name") String name) throws InterruptedException, ExecutionException, TimeoutException, IOException, ParseException, InvalidInputException {
        return COINFACADE.getCorrencyByName(name);
    }

}
