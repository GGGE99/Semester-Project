/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.CoinDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import entities.Coin;
import entities.CoinValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcg
 */
public class ChartFacade {

    private static ChartFacade instance;
    private static EntityManagerFactory emf;

//    private static HashMap<String, Coin> coins = new HashMap();
//    private static HashMap<String, CoinDTO> coinsMap;
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public static ChartFacade getChartFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ChartFacade();
        }
        return instance;
    }

    public String getChart() {

        EntityManager em = emf.createEntityManager();

        TypedQuery<Coin> query = em.createQuery("SELECT c from Coin c WHERE c.name = 'Bitcoin'", Coin.class);
        Coin coin = query.getSingleResult();

        String url = "https://quickchart.io/chart?c=";
//        String data = "";
//        String labels = "";

        JsonArray labels = new JsonArray();
        JsonArray data = new JsonArray();
        JsonArray datasets = new JsonArray();
        JsonObject dataset = new JsonObject();
        for (CoinValue value : coin.getValues()) {
            data.add(value.getPrice());
            labels.add(value.getDate().toString());
        }

        dataset.addProperty("label", "Bitcoin");
        dataset.add("data", data);
        dataset.addProperty("fill", false);
        dataset.addProperty("borderColor", "blue");
        datasets.add(dataset);

        JsonObject chart = new JsonObject();
        JsonObject data1 = new JsonObject();
        data1.add("labels", labels);
        data1.add("datasets", datasets);

        chart.addProperty("type", "line");
        chart.add("data", data1);

        System.out.println(url + chart.toString());
        return url + chart.toString();
    }

    public String getChartData() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Coin> query = em.createQuery("SELECT c from Coin c WHERE c.name = 'Bitcoin'", Coin.class);
        Coin coin = query.getSingleResult();

        JsonArray labels = new JsonArray();
        JsonArray data = new JsonArray();

        for (CoinValue value : coin.getValues()) {
            data.add(value.getPrice());
            labels.add(value.getDate().toString());
        }

        JsonObject chart = new JsonObject();
        chart.add("labels", labels);
        chart.add("data", data);

        JsonArray arr = new JsonArray();
        arr.add(chart);

        return arr.toString();
    }

    public String getChartByName(String name) {
        EntityManager em = emf.createEntityManager();

        String[] names = name.split(",");
        String res = "";
        for (String name1 : names) {
            res += "c.name = '" + name1 + "'" + " OR ";
        }
        res = res.substring(0, res.length() - 3);
        System.out.println(res);

        List<Coin> coin = new ArrayList();
        try {
            TypedQuery<Coin> query = em.createQuery("SELECT c from Coin c WHERE " + res, Coin.class);
            coin = query.getResultList();
        } catch (Exception e) {
        }
        JsonArray arr = new JsonArray();
        for (Coin coin1 : coin) {
            System.out.println(coin1);
            JsonArray labels = new JsonArray();
            JsonArray data = new JsonArray();
            JsonPrimitive jpName = new JsonPrimitive(coin1.getName());

            for (CoinValue value : coin1.getValues()) {
                data.add(value.getPrice());
                labels.add(value.getDate().toString());
            }

            JsonObject chart = new JsonObject();
            chart.add("labels", labels);
            chart.add("data", data);
            chart.add("name", jpName);

            arr.add(chart);
        }
        System.out.println(arr.toString());
        return arr.toString();
    }
}
