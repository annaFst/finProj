package com.example.bt.data.Mappers;

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
        event.setEventId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.addToList(eventEntity.getItems());
        LocalDateTime localDateTime = getDateTimeFromIsoString(eventEntity.getEventDate());
        event.setEventDate(localDateTime.toLocalDate());
        event.setEventTime(localDateTime.toLocalTime());
        event.setParticipants(eventEntity.getParticipants());
        event.setEventCreatorId(eventEntity.getCreator());

        return event;
    }

    private LocalDateTime getDateTimeFromIsoString(String isoDateTime)
    {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2015-10-27T16:22:27.605-07:00", timeFormatter);

        LocalDateTime date = LocalDateTime.from(Instant.from(offsetDateTime));

        return date;
    }
}
