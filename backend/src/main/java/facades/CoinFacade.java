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

    private static String everyCoinsList = "";
    private static Date lastUpdate = null;

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

        Future<List<CoinDTO>> cryptoFuture = es.submit(new CoinStringHandler(crypto));

        List<CoinDTO> cryptoResults = (cryptoFuture.get(10, TimeUnit.SECONDS));

        return GSON.toJson(cryptoResults);
    }

    public String GetAllCoinsWithCurrency(String param) throws IOException, InterruptedException, ExecutionException, TimeoutException, ParseException {

        FetchData crypto = new FetchData("https://api.coinlore.net/api/tickers/");
        FetchData currency = new FetchData("https://api.vatcomply.com/rates?base=USD");

        Future<List<CoinDTO>> cryptoFuture = es.submit(new CoinStringHandler(crypto));
        Future<String> currencyFuture = es.submit(new CoinHandler(currency));

        List<CoinDTO> cryptoResults = (cryptoFuture.get(10, TimeUnit.SECONDS));
        String currencyResults = (currencyFuture.get(10, TimeUnit.SECONDS));

        JsonObject obj = GSON.fromJson(currencyResults, JsonObject.class);

        System.out.println("dasdasdasdasd");

        try {
            for (CoinDTO coinDTO : cryptoResults) {
                coinDTO.setCurrency(param);
                coinDTO.setPrice(coinDTO.getPrice() * Double.parseDouble(obj.get("rates").getAsJsonObject().get(param).toString()));
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("err");
        }
        return GSON.toJson(cryptoResults);

    }

    public String GetEveryCoins() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String URL = "https://api.coinlore.net/api/tickers/";

        Date now = new Date();
        long diffMinutes = 0;
        try {
            Date d1 = sdf.parse(sdf.format(lastUpdate));
            Date d2 = sdf.parse(sdf.format(now));
            long diff = d2.getTime() - d1.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
        } catch (Exception e) {
        }

        if (diffMinutes > 10 || lastUpdate == null) {

            List<FetchData> sites = new ArrayList();
            int incroment = 100;
            int maxloops = 5100;
            int loops = (int) (maxloops / incroment);
            for (int i = 0; i < loops; i++) {
                sites.add(new FetchData(URL + "?start=" + (i * incroment) + "&limit=" + incroment));
            }

            List<Future<List<CoinDTO>>> futures = new ArrayList();
            for (FetchData fd : sites) {
                CoinStringHandler ch = new CoinStringHandler(fd);
                futures.add(es.submit(ch));
            }

            List<CoinDTO> ReturnResults = new ArrayList();
            for (Future<List<CoinDTO>> future : futures) {
                List<CoinDTO> test = future.get(100, TimeUnit.SECONDS);
                for (CoinDTO coinDTO : test) {
                    ReturnResults.add(coinDTO);
                }
            }
            everyCoinsList = GSON.toJson(ReturnResults);
        }
        return everyCoinsList;
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

    FetchData fd;

    CoinStringHandler(FetchData fd) {
        this.fd = fd;
    }

    @Override
    public List<CoinDTO> call() throws Exception {
        fd.get();

        String text = fd.getJson();

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
                    obj.get("volume24").getAsDouble(),
                    obj.get("rank").getAsInt());
            coinsDTO.add(coinDTO);
        }

        return coinsDTO;
    }
}
