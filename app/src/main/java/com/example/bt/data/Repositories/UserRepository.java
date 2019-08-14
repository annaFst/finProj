package com.example.bt.data.Repositories;

import com.example.bt.data.Mappers.UserMapper;
import com.example.bt.models.User;

public class UserRepository extends FirebaseDatabaseRepository<User> {


    public UserRepository() {
        super(new UserMapper());
    }

    @Override
    protected String getRootNode() {
        return "users";
    }
}
