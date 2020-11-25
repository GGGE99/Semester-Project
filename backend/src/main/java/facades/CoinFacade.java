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
import com.nimbusds.jose.crypto.impl.AAD;
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
import utils.JokeFinder;

/**
 *
 * @author marcg
 */
public class CoinFacade {

    private static EntityManagerFactory emf;
    private static CoinFacade instance;
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();

    private CoinFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static CoinFacade getCoinFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CoinFacade();
        }
        return instance;
    }

    public String GetAllCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String URL = "https://api.coinlore.net/api/tickers/";

        FetchData site = new FetchData(URL);

        Future<List<CoinDTO>> future = es.submit(new CoinHandler(site));

        List<CoinDTO> results = (future.get(10, TimeUnit.SECONDS));

        return GSON.toJson(results);
    }

}

class CoinHandler implements Callable<List<CoinDTO>> {

    private static Gson GSON = new Gson();

    FetchData fd;

    CoinHandler(FetchData fd) {
        this.fd = fd;
    }

    @Override
    public List<CoinDTO> call() throws Exception {
        fd.get();

        String results = fd.getJson();
        JsonObject jo = GSON.fromJson(results, JsonObject.class);
        JsonArray arr = jo.getAsJsonArray("data");
        List<CoinDTO> coinsDTO = new ArrayList();

        for (JsonElement jsonElement : arr) {
            Date myDate = new Date();

            JsonObject obj = jsonElement.getAsJsonObject();

            CoinDTO coinDTO = new CoinDTO(
                    "USD",
                    obj.get("name").getAsString(),
                    obj.get("price_usd").getAsDouble(),
                    myDate,
                    obj.get("volume24").getAsDouble());
            coinsDTO.add(coinDTO);
            System.out.println(coinDTO);
        }

        return coinsDTO;
    }
}
