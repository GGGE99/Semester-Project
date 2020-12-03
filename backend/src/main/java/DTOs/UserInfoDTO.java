/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.User;
import entities.UserInfo;

/**
 *
 * @author Marcus
 */
public class UserInfoDTO {
    private String favCurrency;
    private String favCoin;

    public UserInfoDTO(String favCurrency, String favCoin) {
        this.favCurrency = favCurrency;
        this.favCoin = favCoin;
    }
    public UserInfoDTO(UserInfo userInfo) {
        this.favCoin = userInfo.getFavoriteBitcoin();
        this.favCurrency = userInfo.getFavoriteCurrency();
    }
        

    public String getFavoriteCurrecny() {
        return favCurrency;
    }

    public void setFavoriteCurrecny(String favoriteCurrecny) {
        this.favCurrency = favoriteCurrecny;
    }

    public String getFavoriteBitcoin() {
        return favCoin;
    }

    public void setFavoriteBitcoin(String favCoin) {
        this.favCoin = favCoin;
    }
    
    
}
