package com.example.bt.data.Repositories;

import java.util.Hashtable;
import java.util.Map;

public class RepositoryFactory {

    public enum RepositoryType
    {
        UserRepository,
        EventRepository
    }

    private static Map<RepositoryType, FirebaseDatabaseRepository> repositoryContainer = new Hashtable<>();

    public static FirebaseDatabaseRepository GetRepositoryInstance(RepositoryType repositoryType) throws Exception {
        FirebaseDatabaseRepository repository;

        switch (repositoryType){
            case UserRepository:
                repository = repositoryContainer.get(repositoryType);
                if (repository == null) {
                    repository = new UserRepository();
                    repositoryContainer.put(repositoryType, repository);
                }
                break;
            case EventRepository:
                repository = repositoryContainer.get(repositoryType);
                if (repository == null) {
                    repository = new EventRepository();
                    repositoryContainer.put(repositoryType, repository);
                }
                break;
            default:
                throw new Exception();
        }

        return repository;
    }
}
