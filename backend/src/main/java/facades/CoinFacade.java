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

        Future<String> future = es.submit(new CoinHandler(site));
        String results = (future.get(10, TimeUnit.SECONDS));

        Future<List<CoinDTO>> future1 = es.submit(new CoinStringHandler(results));

        List<CoinDTO> results1 = (future1.get(10, TimeUnit.SECONDS));

        return GSON.toJson(results1);
    }

    public String GetEveryCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String URL = "https://api.coinlore.net/api/tickers/";

        List<FetchData> sites = new ArrayList();
        for (int i = 0; i < 51; i++) {
            sites.add(new FetchData(URL + "?start=" + (i * 100) + "&limit=100"));
        }

        List<Future<String>> futures = new ArrayList();
        for (FetchData fd : sites) {
            CoinHandler ch = new CoinHandler(fd);
            futures.add(es.submit(ch));
        }

        List<String> results = new ArrayList();
        for (Future<String> future : futures) {
            results.add(future.get(100, TimeUnit.SECONDS));
        }

        List<Future<List<CoinDTO>>> futuresRes = new ArrayList();
        for (String result : results) {
            CoinStringHandler csh = new CoinStringHandler(result);
            futuresRes.add(es.submit(csh));
        }

        List<CoinDTO> ReturnResults = new ArrayList();
        for (Future<List<CoinDTO>> future : futuresRes) {
            List<CoinDTO> test = future.get(100, TimeUnit.SECONDS);
            for (CoinDTO coinDTO : test) {
                ReturnResults.add(coinDTO);
            }

        }
        return GSON.toJson(ReturnResults);
    }
}

class CoinHandler implements Callable<String> {

    FetchData fd;

    CoinHandler(FetchData fd) {
        this.fd = fd;
    }

    @Override
    public String call() throws Exception {
        fd.get();

        return new String(fd.getJson());
    }
}

class CoinStringHandler implements Callable<List<CoinDTO>> {

    private static Gson GSON = new Gson();

    String text;

    CoinStringHandler(String text) {
        this.text = text;
    }

    @Override
    public List<CoinDTO> call() throws Exception {
        JsonObject jo = GSON.fromJson(text, JsonObject.class);
        JsonArray arr = jo.getAsJsonArray("data");
        List<CoinDTO> coinsDTO = new ArrayList();
        Date myDate = new Date();

        for (JsonElement jsonElement : arr) {
            JsonObject obj = jsonElement.getAsJsonObject();
            CoinDTO coinDTO = new CoinDTO(
                    "USD",
                    obj.get("name").getAsString(),
                    obj.get("price_usd").getAsDouble(),
                    myDate,
                    obj.get("volume24").getAsDouble());
            coinsDTO.add(coinDTO);
        }

        return coinsDTO;
    }
}
