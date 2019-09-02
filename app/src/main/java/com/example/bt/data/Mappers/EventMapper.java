package com.example.bt.data.Mappers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bt.data.Entities.EventEntity;
import com.example.bt.models.Event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper extends FirebaseMapper<EventEntity, Event> {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Event map(EventEntity eventEntity) {
        Event event = new Event();
        event.setEventId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.addToList(eventEntity.getItems());
        LocalDateTime localDateTime = getDateTimeFromIsoString(eventEntity.getEventDate());
        if (localDateTime != null)
        {
            event.setEventDate(localDateTime.toLocalDate());
            event.setEventTime(localDateTime.toLocalTime());
        }
        
        event.setParticipants(eventEntity.getParticipants());
        event.setEventCreatorId(eventEntity.getCreator());

        return event;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime getDateTimeFromIsoString(String isoDateTime)
    {
        if (isoDateTime.isEmpty())
        {
            return null;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDateTime, timeFormatter);

        LocalDateTime date = LocalDateTime.from(Instant.from(offsetDateTime));

        return date;
    }
}
