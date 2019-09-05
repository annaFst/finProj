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

    public void addNewUser(String userId, String name) {
        User user = new User(userId, name);
        mDataRef.child(userId).setValue(user);
    }

    public void addNewUser(User user) {
        String userId = user.getId();
        mDataRef.child(userId).setValue(user);
    }
}
