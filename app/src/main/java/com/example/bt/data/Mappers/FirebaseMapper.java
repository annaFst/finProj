package com.example.bt.data.Mappers;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * A firebase mapper abstract base class
 *
 * @param <Entity> The entity extracted from the Firebase database
 * @param <Model> The model class to be mapped to
 */
public abstract class FirebaseMapper<Entity, Model> implements IMapper<Entity, Model> {

    /**
     * @param dataSnapshot The data snapshot from the firebase database
     * @return Mapped model
     */
    public Model map(DataSnapshot dataSnapshot) {
        Entity entity = dataSnapshot.getValue(getEntityClass());
        return map(entity);
    }


    /**
     * @param dataSnapshot The data snapshot from the firebase database
     * @return List of mapped models
     */
    public List<Model> mapList(DataSnapshot dataSnapshot) {
        List<Model> list = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            list.add(map(item));
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    private Class<Entity> getEntityClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<Entity>) superclass.getActualTypeArguments()[0];
    }

}
