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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static List<CoinDTO> coinsList = new ArrayList();
    private static Date lastUpdate = new Date();

    private static EntityManagerFactory emf;
    private static CoinFacade instance;
    private static ExecutorService es = Executors.newCachedThreadPool();
    private static Gson GSON = new Gson();

    SimpleDateFormat sdf
            = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss");

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
        FetchData crypto = new FetchData("https://api.coinlore.net/api/tickers/");

        Future<String> cryptoFuture = es.submit(new CoinHandler(crypto));

        String cryptoResults = (cryptoFuture.get(10, TimeUnit.SECONDS));

        Future<List<CoinDTO>> future1 = es.submit(new CoinStringHandler(cryptoResults));
        List<CoinDTO> results1 = (future1.get(10, TimeUnit.SECONDS));

        return GSON.toJson(results1);
    }

    public String GetAllCoinsWithCurrency(String param) throws IOException, InterruptedException, ExecutionException, TimeoutException, ParseException {
        FetchData crypto = new FetchData("https://api.coinlore.net/api/tickers/");
        FetchData currency = new FetchData("https://api.vatcomply.com/rates?base=USD");

        Future<String> cryptoFuture = es.submit(new CoinHandler(crypto));
        Future<String> currencyFuture = es.submit(new CoinHandler(currency));

        String cryptoResults = (cryptoFuture.get(10, TimeUnit.SECONDS));
        String currencyResults = (currencyFuture.get(10, TimeUnit.SECONDS));

        JsonObject obj = GSON.fromJson(currencyResults, JsonObject.class);

        Future<List<CoinDTO>> future1 = es.submit(new CoinStringHandler(cryptoResults));
        List<CoinDTO> results1 = (future1.get(10, TimeUnit.SECONDS));

        try {
            for (CoinDTO coinDTO : results1) {
                coinDTO.setCurrency(param);
                coinDTO.setPrice(coinDTO.getPrice() * Double.parseDouble(obj.get("rates").getAsJsonObject().get(param).toString()));
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("err");
        }

        Date dt2 = new Date();
        //TODO fix this :D
//        System.out.println(sdf.parse(lastUpdate.toString()));

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
