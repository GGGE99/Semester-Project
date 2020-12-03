package utils;

import DTOs.UserInfoDTO;
import entities.Coin;
import entities.CoinValue;
import entities.Role;
import entities.User;
import entities.UserInfo;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        User user = new User("user", "test2");
        User admin = new User("admin", "test2");
        User both = new User("user_admin", "test2");
        Coin coin1 = new Coin("Bitcoin");
        Coin coin2 = new Coin("Bitcoin1");
        Coin coin3 = new Coin("Bitcoin2");
        Coin coin4 = new Coin("Bitcoin3");
        Coin coin5 = new Coin("Bitcoin4");
        

        if (admin.getUserPass().equals("test") || user.getUserPass().equals("test") || both.getUserPass().equals("test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }
        

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        user.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);
        Date date = new Date();
//        coin1.addValue(new CoinValue(1000, date));
//        coin1.addValue(new CoinValue(1231231, date));
//        coin1.addValue(new CoinValue(101231300, date));
//        coin2.addValue(new CoinValue(112312, date));
//        coin2.addValue(new CoinValue(345345, date));
//        coin3.addValue(new CoinValue(1043534500, date));
//        coin3.addValue(new CoinValue(345345, date));
//        coin3.addValue(new CoinValue(103453400, date));
//        coin4.addValue(new CoinValue(1034534500, date));
//        coin4.addValue(new CoinValue(333333, date));
//        coin4.addValue(new CoinValue(5555555, date));
//        coin5.addValue(new CoinValue(345345345, date));
//        coin5.addValue(new CoinValue(43535345, date));
//        coin5.addValue(new CoinValue(312312, date));

//        em.persist(coin1);
//        em.persist(coin2);
//        em.persist(coin3);
//        em.persist(coin4);
//        em.persist(coin5);

        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);
        em.getTransaction().commit();
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

    }

}
