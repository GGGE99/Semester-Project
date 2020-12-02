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
public class ChartFacade {

    private static ChartFacade instance;
    private static EntityManagerFactory emf;
    private static CoinFacade coinFacade;
    private static HashMap<String, Coin> coins = new HashMap();
    private static HashMap<String, CoinDTO> coinsMap;

    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

    public static ChartFacade getChartFacade(EntityManagerFactory _emf, HashMap<String, CoinDTO> _coinsMap) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        if (instance == null) {
            emf = _emf;
            instance = new ChartFacade();
            coinFacade = CoinFacade.getCoinFacade(emf);
        }
        return instance;
    }

    public ChartFacade() {
        ses.scheduleAtFixedRate(
                () -> {
            try {
                addCoinsToDb();
            } catch (IOException ex) {
                Logger.getLogger(ChartFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChartFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ChartFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(ChartFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
                },
                0, 30, TimeUnit.MINUTES
        );
    }

    private void addCoinsToDb() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        EntityManager em = emf.createEntityManager();
        Date date = new Date();
        List<Coin> results = null;
        try {
            TypedQuery<Coin> query = em.createNamedQuery("Coin.getAllRows", Coin.class);
            results = query.getResultList();
        } catch(Exception e) {}
        System.out.println("asdadasdasdasdasda");
        if (results.size() < 1) {
            System.out.println("Hej");
            coinsMap = coinFacade.getCoinsMap();
            em.getTransaction().begin();
            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = new Coin(coinDTO.getName());
                coins.put(k, coin);
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                em.persist(coin);
                System.out.println(k);
            });
            em.getTransaction().commit();
        } else {
            em.getTransaction().begin();
            coinsMap.forEach((k, coinDTO) -> {
                Coin coin = coins.get(k);
                coin.addValue(new CoinValue(coinDTO.getPrice(), date));
                em.merge(coin);
            });
            em.getTransaction().commit();
        }
        System.out.println("end");
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
