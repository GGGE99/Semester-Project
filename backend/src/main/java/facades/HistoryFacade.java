/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOs.CoinDTO;
import com.google.gson.JsonObject;
import entities.Coin;
import entities.CoinValue;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
    private static HashMap<String, Coin> coins = new HashMap();

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

    public void addCoinsToDb() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        EntityManager em = emf.createEntityManager();
        HashMap<String, CoinDTO> coinsMap = coinFacade.getCoinsMap();
        List<Coin> results = null;
        Date date = new Date();

        try {
            TypedQuery<Coin> query = em.createNamedQuery("Coin.getAllRows", Coin.class);
            results = query.getResultList();
        } catch (Exception e) {
        }

        System.out.println(results);

        if (coins.isEmpty()) {
            for (Coin result : results) {
                coins.put(result.getName(), result);
            }
//            coinsMap.forEach((k, coinDTO) -> {
//                Coin coin = new Coin(coinDTO.getName());
//                coins.put(k, coin);
//            });
        }

        if (results.isEmpty()) {
            em.getTransaction().begin();
            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = new Coin(coinDTO.getName());
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                em.persist(coin);
            });
            em.getTransaction().commit();
        } else {
            System.out.println(date);

            em.getTransaction().begin();

            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = coins.get(k);
                CoinValue coinValue = new CoinValue(coinDTO.getPrice(), date);
                coin.addValue(coinValue);
                em.merge(coin);
            });
            em.getTransaction().commit();
        }
    }

//    public static void getCoins() {
//        coinsMap.forEach((k, coinDTO) -> {
//            Coin coin = new Coin(coinDTO.getName());
//            coins.put(k, coin);
//        });
//    }
    public String getCoinHistory() {
        EntityManager em = emf.createEntityManager();
        Coin coin1 = em.createQuery("SELECT c from Coin c where c.name = 'Bitcoin'", Coin.class).getSingleResult();
        Coin coin = coins.get("Bitcoin");
        List<CoinValue> vals = coin1.getValues();

        JsonObject obj = new JsonObject();
        for (CoinValue val : vals) {
            obj.addProperty(val.getDate().toString(), val.getPrice());
        }
        return obj.toString();
    }
}
