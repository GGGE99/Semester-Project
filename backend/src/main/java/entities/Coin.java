/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Coin.deleteAllRows", query = "DELETE from Coin")
@NamedQuery(name = "Coin.getAllRows", query = "SELECT c from Coin c")
public class Coin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "coin")
    private List<CoinValue> values;

    public Coin() {
    }

    public Coin(String name) {
        this.name = name;
        this.values = new ArrayList();
    }

    public void addValue(CoinValue coinValue) {
        coinValue.setCoin(this);
        values.add(coinValue);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CoinValue> getValues() {
        return values;
    }
    
    

}
