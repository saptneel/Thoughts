package com.saptneel.thoughts;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NHolder> {

    ArrayList<Note> notes;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;

    public NoteAdapter(ArrayList<Note> notes, Context context, ItemClicked itemClicked) {
        this.context = context;
        this.notes = notes;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override
    public NHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note, parent, false);
        this.parent = parent;
        return new NHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NHolder holder, int position) {

        holder.title.setText(notes.get(position).getTitle());
        holder.description.setText(notes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    class NHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView edt;
        public NHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
            description = itemView.findViewById(R.id.desc);
            edt = itemView.findViewById(R.id.edt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(description.getMaxLines() == 1)
                        description.setMaxLines(Integer.MAX_VALUE);
                    else
                        description.setMaxLines(1);
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClicked.onClick(getAdapterPosition(), itemView);
                }
            });
        }
    }

    interface ItemClicked {
        void onClick(int position, View view);
    }
}
