/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author baske
 */
public class CurrencyFacadeTest {
        private static EntityManagerFactory emf;
        private static CurrencyFacade facade = CurrencyFacade.getCurrencyFacade(emf);
    
        @Test
        public void testGetAllCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String currencyList = facade.getAll();
        System.out.println("Test get currencies");
        Boolean expResult = true;
       
        String result = currencyList;
        
        assertEquals(expResult, result.contains("USD"));
        assertEquals(expResult, result.contains("DKK"));
    }
    
}
