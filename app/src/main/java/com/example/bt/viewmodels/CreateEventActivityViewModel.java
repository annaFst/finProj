package com.example.bt.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.bt.app.CurrentUserAccount;
import com.example.bt.data.Repositories.RepositoryFactory;
import com.example.bt.models.Contact;
import com.example.bt.models.Event;
import com.example.bt.models.User;

/**
 * The ViewModel for the CreateEventActivity
 */
public class CreateEventActivityViewModel extends ViewModel {

    public Event initEvent()
    {
        Event newEvent = new Event();
        User currentUser = CurrentUserAccount.getInstance().getCurrentUser();

        if (!currentUser.getId().isEmpty())
        {
            newEvent.setEventCreatorId(currentUser.getId());
            newEvent.getParticipants().add(new Contact(currentUser.getName(), currentUser.getId()));
        }

        return newEvent;
    }

    /**
     * Updates the Firebase database
     * @param event To be updated in the DB
     */
    public void updateDb(Event event) {
        // Add new event
        String eventKey = RepositoryFactory.
                GetRepositoryInstance(RepositoryFactory.RepositoryType.EventRepository)
                .add(event);
        event.setEventId(eventKey);
        CurrentUserAccount.getInstance().getCurrentUser().addEvent(event);
        CurrentUserAccount.getInstance().GetCurrentUserEventList().add(event);
        RepositoryFactory.
                GetRepositoryInstance(RepositoryFactory.RepositoryType.UserRepository)
                .update(CurrentUserAccount.getInstance().getCurrentUser());
    }
}
