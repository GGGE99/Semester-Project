/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.CoinDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author baske
 */
public class CoinFacadeTest {

    private static EntityManagerFactory emf;
    private static Gson GSON = new Gson();
    private static CoinFacade facade  = null;

    @BeforeAll
    public static void setUpClass() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        facade = CoinFacade.getCoinFacade(emf);
    }

    
    @Test
    public void testGetAllCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String cDTOList = facade.GetAllCoins();
        System.out.println("Test get coins");
        Boolean expResult = true;

        String result = cDTOList;

        assertEquals(expResult, result.contains("Bitcoin"));
        assertEquals(expResult, result.contains("Ethereum"));
    }

}
