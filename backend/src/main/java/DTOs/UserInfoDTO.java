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
    private String favoriteCurrecny;
    private String favoriteBitcoin;

    public UserInfoDTO(String favoriteCurrecny, String favoriteBitcoin) {
        this.favoriteCurrecny = favoriteCurrecny;
        this.favoriteBitcoin = favoriteBitcoin;
    }
    public UserInfoDTO(UserInfo userInfo) {
        this.favoriteBitcoin = userInfo.getFavoriteBitcoin();
        this.favoriteCurrecny = userInfo.getFavoriteCurrency();
    }
        

    public String getFavoriteCurrecny() {
        return favoriteCurrecny;
    }

    public void setFavoriteCurrecny(String favoriteCurrecny) {
        this.favoriteCurrecny = favoriteCurrecny;
    }

    public String getFavoriteBitcoin() {
        return favoriteBitcoin;
    }

    public void setFavoriteBitcoin(String favoriteBitcoin) {
        this.favoriteBitcoin = favoriteBitcoin;
    }
    
    
}
