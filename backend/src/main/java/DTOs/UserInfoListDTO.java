/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

import entities.User;
import entities.UserInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus
 */

    public class UserInfoListDTO {
    private List<UserInfoDTO> all = new ArrayList<>();
    

    public UserInfoListDTO(List<UserInfo> userList) {
        for (UserInfo userInfo : userList) {
            all.add(new UserInfoDTO(userInfo));
        }
    }

    public List<UserInfoDTO> getAll() {
        return all;
    }
    
    
}

