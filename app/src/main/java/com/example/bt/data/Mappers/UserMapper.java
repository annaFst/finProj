package com.example.bt.data.Mappers;

import com.example.bt.data.Entities.UserEntity;
import com.example.bt.models.User;

public class UserMapper extends FirebaseMapper<UserEntity, User> {

    @Override
    public User map(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEvents(userEntity.getEvents());
        user.setName(userEntity.getName());

        return user;
    }


}
