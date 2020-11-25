/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.CoinDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManagerFactory;
import utils.FetchData;
import utils.HttpUtils;

/**
 *
 * @author marcg
 */
public class CurrencyFacade {

    private static EntityManagerFactory emf;
    private static CurrencyFacade instance;
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();

    private CurrencyFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static CurrencyFacade getCurrencyFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CurrencyFacade();
        }
        return instance;
    }
    
    public String getAll() throws IOException{
        
        return HttpUtils.fetchData("https://api.exchangeratesapi.io/latest?base=USD");
        
        
    }

}
