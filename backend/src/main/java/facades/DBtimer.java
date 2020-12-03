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
public class DBtimer {

    private static DBtimer instance;
    private static EntityManagerFactory emf;
    private static CoinFacade coinFacade;
    private static HashMap<String, Coin> coins = new HashMap();
    private static HashMap<String, CoinDTO> coinsMap;
    private static Date date = null;

    private static ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public static DBtimer getChartFacade(EntityManagerFactory _emf) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        if (instance == null) {
            emf = _emf;
            instance = new DBtimer();
            coinFacade = CoinFacade.getCoinFacade(emf);
            System.out.println(ses);
        }
        ses.scheduleAtFixedRate(() -> {
            System.out.println("theard");
            try {
                addCoinsToDb();
            } catch (IOException ex) {
                Logger.getLogger(DBtimer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DBtimer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(DBtimer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(DBtimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        },
                0, 30, TimeUnit.MINUTES
        );
        return instance;
    }

//    public DBtimer() {
//
//    }

    private static void addCoinsToDb() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        EntityManager em = emf.createEntityManager();
        date = new Date();
        List<Coin> results = null;
        try {
            TypedQuery<Coin> query = em.createNamedQuery("Coin.getAllRows", Coin.class);
            results = query.getResultList();
        } catch (Exception e) {
        }
        coinsMap = coinFacade.getCoinsMap();

        if (results.size() < 1) {
            em.getTransaction().begin();
            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = new Coin(coinDTO.getName());
                coins.put(k, coin);
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                em.persist(coin);
            });
            em.getTransaction().commit();
        } else {
            if (coins.isEmpty()) {
                getCoins();
            }

            em.getTransaction().begin();

            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = coins.get(k);
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                em.merge(coin);
            });
            em.getTransaction().commit();
        }
    }

    public static void getCoins() {
        coinsMap.forEach((k, coinDTO) -> {
            Coin coin = new Coin(coinDTO.getName());
            coins.put(k, coin);
        });
    }

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
