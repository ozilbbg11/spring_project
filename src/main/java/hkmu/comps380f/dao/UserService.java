/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hkmu.comps380f.dao;

import hkmu.comps380f.model.User;
import java.util.List;

/**
 *
 * @author User
 */
public interface UserService {
    public void save(User user);
    public List<User> findAll();
}
