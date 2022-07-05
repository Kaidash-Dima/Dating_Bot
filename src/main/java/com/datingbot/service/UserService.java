package com.datingbot.service;

import com.datingbot.entity.User;
import com.datingbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public long getCountUsers(){
        return userRepository.count();
    }

    public User getFirstUser(){
        return userRepository.findTopByOrderByIdAsc();
    }

    public User getLastUser(){
        return userRepository.findTopByOrderByIdDesc();
    }

    public User nextProfile(long oppositeSexId, int oppositeSex) {
        List<User> users = userRepository.findAllBySex(oppositeSex);
        User user = new User();
        users.sort(Comparator.comparingInt(o -> (int) o.getId()));

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == oppositeSexId) {
                if (oppositeSexId == users.get(users.size() - 1).getId()){
                    user = users.get(0);
                }else {
                    user = findById(users.get(i + 1).getId());
                }
                break;
            }
        }
        return user;
    }

    public User findById(long id){
        Optional<User> userOptional =  userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public User findByUserId(long userId){
        return userRepository.findByUserId(userId);
    }

    public void updateUser(User user){
        User user1 = findByUserId(user.getUserId());
        user1.setName(user.getName());
        user1.setNickname(user.getNickname());
        user1.setAge(user.getAge());
        user1.setSex(user.getSex());
        user1.setOppositeSex(user.getOppositeSex());
        user1.setCity(user.getCity());
        user1.setDescription(user.getDescription());
        user1.setOppositeSexId(user.getOppositeSexId());
        user1.setChatStatus(user.getChatStatus());
        user1.setLanguage(user.getLanguage());
        saveUser(user1);
    }

    public void saveUser (User user){
        userRepository.save(user);
    }
}
