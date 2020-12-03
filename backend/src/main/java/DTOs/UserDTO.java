/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.User;
import entities.UserInfo;
import java.util.List;

/**
 *
 * @author marcg
 */
public class UserDTO {

    private String username;
    private String password;
    private List<String> roles;
    private String favoriteBitcoin;
    private String favoriteCurrency;

    public UserDTO(String name, String password, List<String> roles ) {
        this.username = name;
        this.roles = roles;
        this.password = password;
    }

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.roles = user.getRolesAsStrings();
        this.favoriteBitcoin = user.getUserInfo().getFavoriteBitcoin();
        this.favoriteCurrency = user.getUserInfo().getFavoriteCurrency();
    }

    public String getFavoriteBitcoin() {
        return favoriteBitcoin;
    }

    public void setFavoriteBitcoin(String favoriteBitcoin) {
        this.favoriteBitcoin = favoriteBitcoin;
    }

    public String getFavoriteCurrency() {
        return favoriteCurrency;
    }

    public void setFavoriteCurrency(String favoriteCurrency) {
        this.favoriteCurrency = favoriteCurrency;
    }
    

    public String getName() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}
