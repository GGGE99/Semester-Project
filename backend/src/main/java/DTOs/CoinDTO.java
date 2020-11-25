/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import java.util.Date;

/**
 *
 * @author marcg
 */
public class CoinDTO {

    private String currency;
    private String name;
    private double price;
    private String lastUpdated;
    private double volume;

    public CoinDTO() {
    }

    public CoinDTO(String currency, String name, double price, Date lastUpdated, double volume) {
        this.currency = currency;
        this.name = name;
        this.price = price;
        this.lastUpdated = lastUpdated.toString();
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "CoinDTO{" + "currency=" + currency + ", name=" + name + ", price=" + price + ", lastUpdated=" + lastUpdated + ", volume=" + volume + '}';
    }

    public String getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public double getVolume() {
        return volume;
    }
    
    
    

}
