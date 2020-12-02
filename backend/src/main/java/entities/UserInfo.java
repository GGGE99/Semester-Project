/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import DTOs.UserInfoDTO;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Marcus
 */
@Entity
@Table(name ="user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToOne
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String favoriteCurrency;
    private String favoriteBitcoin;

    public UserInfo() {
    }

    public UserInfo setInfo(UserInfoDTO userInfoDTO){
        this.favoriteCurrency = userInfoDTO.getFavoriteCurrecny();
        this.favoriteBitcoin = userInfoDTO.getFavoriteBitcoin();
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFavoriteCurrency() {
        return favoriteCurrency;
    }

    public void setFavoriteCurrency(String favoriteCurrency) {
        this.favoriteCurrency = favoriteCurrency;
    }

    public String getFavoriteBitcoin() {
        return favoriteBitcoin;
    }

    public void setFavoriteBitcoin(String favoriteBitcoin) {
        this.favoriteBitcoin = favoriteBitcoin;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
