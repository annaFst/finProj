package com.example.bt.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt.R;
import com.example.bt.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> mEvents;
    private ClickListener mClickListener;

    public EventAdapter(List<Event> events) {
        if (events != null)
            mEvents = events;
        else
            mEvents = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        if (mEvents.isEmpty()) return;
        holder.bind(mEvents.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(mEvents.get(position).getEventId(), v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener<T> {
        void onItemClick(String itemId, View v);
        void onItemLongClick(String itemId, View v);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.text_list_event);
        }

        public void bind(Event event) {
            eventName.setText(event.getName());
        }
    }
}
