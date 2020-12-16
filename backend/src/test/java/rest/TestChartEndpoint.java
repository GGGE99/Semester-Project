/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.Coin;
import entities.CoinValue;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author baske
 */
public class TestChartEndpoint {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();
        
        Coin coin = new Coin("Bitcoin");
        
        coin.addValue(new CoinValue(2000, new Date()));
        
        em.getTransaction().begin();
        em.persist(coin);
        em.getTransaction().commit();
        
        
        

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/chart");
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }
    @Test
    public void testGetChartData() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/chart/data").then()
                .statusCode(200)
                .body(notNullValue());
    }
    
        @Test
    public void testGetChartDataByName() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/chart/Bitcoin").then()
                .statusCode(200)
                .body(notNullValue());
    }
    
        @Test
    public void testGetCoinsHistory() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/chart/coins").then()
                .statusCode(200)
                .body(notNullValue());
    }
    
        @Test
    public void testGetCoinValues() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/chart/coinvalues").then()
                .statusCode(200)
                .body(notNullValue());
    }
    
}
