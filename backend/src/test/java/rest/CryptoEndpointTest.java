/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOs.CoinDTO;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static rest.LoginEndpointTest.startServer;
import utils.EMF_Creator;

/**
 *
 * @author baske
 */
public class CryptoEndpointTest {

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

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/all");
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @Test
    public void testGetAllCoins() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/all").then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void testGetCoin() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/Bitcoin").then()
                .statusCode(200)
                .body("name", equalTo("Bitcoin"));
    }

    @Test
    public void testGetCoinWithCurrency() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/Bitcoin/USD").then()
                .statusCode(200)
                .body("currency", equalTo("USD"))
                .body("name", equalTo("Bitcoin"));
    }

    @Test
    public void testGetAllCoinsWithCurrencies() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/all/DKK").then()
                .statusCode(200)
                //.body(notNullValue())
                .body(containsString("DKK"));
    }

    
    @Test
    public void testGetEveryCoin() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/every").then()
                .statusCode(200)
                .body(notNullValue());
    }
    
    @Test
    public void testGetEveryName() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/every/name").then()
                .statusCode(200)
                .body(containsString("Bitcoin"));
    }
    
    @Test
    @Disabled
    public void testGetHistoryForCoin() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/crypto/history").then()
                .statusCode(200)
                .body(notNullValue());
    }

}
