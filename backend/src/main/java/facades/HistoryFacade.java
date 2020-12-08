/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.CoinDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entities.Coin;
import entities.CoinValue;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcg
 */
public class HistoryFacade {

    private static HistoryFacade instance;
    private static EntityManagerFactory emf;
    private static CoinFacade coinFacade;
//    private static HashMap<String, Coin> coins = new HashMap();
//    private static HashMap<String, CoinDTO> coinsMap = new HashMap();
    private static ExecutorService es = Executors.newCachedThreadPool();

    public static HistoryFacade getChartFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HistoryFacade();
            try {
                coinFacade = CoinFacade.getCoinFacade(emf);
            } catch (IOException ex) {
                Logger.getLogger(HistoryFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(HistoryFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(HistoryFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(HistoryFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    public String addCoinsToDb() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        es.submit(new addToDb(emf));
        return "Added";
    }

    public String getCoins() {
        EntityManager em = emf.createEntityManager();
        List<Coin> results = null;

        try {
            TypedQuery<Coin> query = em.createQuery("SELECT c FROM Coin c", Coin.class
            );
            results = query.getResultList();
        } catch (Exception e) {
            results = null;
        }
        return "" + results.size();
    }

    public String getCoinvalues() {
        EntityManager em = emf.createEntityManager();
        List<CoinValue> results = null;

        try {
            TypedQuery<CoinValue> query = em.createQuery("SELECT c FROM CoinValue c", CoinValue.class
            );
            results = query.getResultList();
        } catch (Exception e) {
            results = null;
        }
        return "" + results.size();
    }

    public String getCoinHistory() {
        EntityManager em = emf.createEntityManager();
        Coin coin1 = em.createQuery("SELECT c from Coin c where c.name = 'Bitcoin'", Coin.class
        ).getSingleResult();
//        Coin coin = coins.get("Bitcoin");
        List<CoinValue> vals = coin1.getValues();

        JsonObject obj = new JsonObject();
        for (CoinValue val : vals) {
            obj.addProperty(val.getDate().toString(), val.getPrice());
        }
        return obj.toString();
    }
}

class addToDb implements Callable<String> {

    private static EntityManager em;
    private static HashMap<String, CoinDTO> coinsMap;
    private static CoinFacade coinFacade;

    public addToDb(EntityManagerFactory emf) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        coinFacade = CoinFacade.getCoinFacade(emf);
        this.em = emf.createEntityManager();
        coinsMap = coinFacade.getCoinsMap();
    }

    @Override
    public String call() throws Exception {

        HashMap<String, Coin> coins = new HashMap();
        

        List<Coin> results = null;
        Date date = new Date();

        try {
            TypedQuery<Coin> query = em.createQuery("SELECT c FROM Coin c", Coin.class);
            results = query.getResultList();
        } catch (Exception e) {
            results = null;
        }

        System.out.println(
                "start");
        if (results.isEmpty()
                || results == null) {
            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = new Coin(coinDTO.getName());
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                coins.put(k, coin);
            });

            em.getTransaction().begin();
            coins.forEach((k, coin) -> {
                System.out.println(k);
                em.persist(coin);
            });
            em.getTransaction().commit();
        } else {
            if (coins.isEmpty()) {
                for (Coin result : results) {
                    coins.put(result.getName(), result);
                }
            }
            if (coins.isEmpty()) {
                return "";
            }
            em.getTransaction().begin();

            coins.forEach((k, coin) -> {
                CoinDTO coinDTO = coinsMap.get(k);
                if (coin != null && coinDTO != null) {
                    coin.addValue(new CoinValue(coinDTO.getPrice(), date));

                }
                if (coin != null && coinDTO != null) {
                    em.merge(coin);
                }

            });
            em.getTransaction().commit();
            System.out.println("done");
            return "done";
        }

        System.out.println("done: " + coins.size());
        return "";
    }
}
