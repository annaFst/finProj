package com.example.bt.data.Mappers;

import com.example.bt.data.Entities.UserEntity;
import com.example.bt.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper extends FirebaseMapper<UserEntity, User> {

    @Override
    public User map(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        if (userEntity.getEvents() != null)
        {
            user.setEvents(userEntity.getEvents());
        }
        else{
            user.setEvents(new ArrayList<String>());
        }
        user.setName(userEntity.getName());

        return user;
    }


}
