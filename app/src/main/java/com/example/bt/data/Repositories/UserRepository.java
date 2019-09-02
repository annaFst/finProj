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

    public void writeNewUser(String userId, String name) {
        User user = new User(userId, name);

        mDataRef.child("users").child(userId).setValue(user);
    }
}
