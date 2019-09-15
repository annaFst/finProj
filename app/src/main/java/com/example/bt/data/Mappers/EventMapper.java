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

    @Override
    public Event map(EventEntity eventEntity) {
        Event event = new Event();
        event.setEventId(eventEntity.getEventId());
        event.setName(eventEntity.getName());
        event.addToList(eventEntity.getItems());
        event.setEventDate(eventEntity.getEventDate());
        event.setEventTime(eventEntity.getEventTime());
        event.setParticipants(eventEntity.getParticipants());
        event.setEventCreatorId(eventEntity.getEventCreatorId());
        //event.setActive(eventEntity.isActive());
        //event.setRepeat(eventEntity.isRepeat());

        return event;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime getDateTimeFromIsoString(String isoDateTime)
    {
        if (isoDateTime == null || isoDateTime.isEmpty())
        {
            return null;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDateTime, timeFormatter);

        LocalDateTime date = LocalDateTime.from(Instant.from(offsetDateTime));

        return date;
    }
}
