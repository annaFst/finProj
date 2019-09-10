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

    @Override
    public String add(User user) {
        return addNewUser(user);
    }

    @Override
    public void update(User user) {
        mDataRef.child(user.getId()).setValue(user);
    }

    @Override
    public void remove(User user) {
        mDataRef.child(user.getId()).setValue(null);
    }

//    private void addNewUser(String userId, String name) {
//        User user = new User(userId, name);
//        mDataRef.child(userId).setValue(user);
//    }

    private String addNewUser(User user) {
        String userId = user.getId();
        mDataRef.child(userId).setValue(user);

        return userId;
    }
}
