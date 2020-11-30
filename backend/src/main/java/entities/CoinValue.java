/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author marcg
 */
@Entity
public class CoinValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Coin coin;
    private double price;
    private Date date;

    public CoinValue() {
    }

    public CoinValue(double price, Date date) {
        this.price = price;
        this.date = date;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    
    

}
