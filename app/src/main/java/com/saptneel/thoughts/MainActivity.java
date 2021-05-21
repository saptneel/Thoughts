package com.saptneel.thoughts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton btn;
    ArrayList<Note> notes;
    RecyclerView recycler;
    NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewIP = inflater.inflate(R.layout.input, null, false);
                final EditText title = viewIP.findViewById(R.id.title);
                final EditText desc = viewIP.findViewById(R.id.description);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewIP).setTitle("Add Note").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nTitle = title.getText().toString();
                        String nDesc = desc.getText().toString();

                        Note note = new Note(nTitle, nDesc);

                        boolean check = new NHandler(MainActivity.this).create(note);

                        if(check) {
                            Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                            loadNotes();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to save note", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });


        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        loadNotes();

        ItemTouchHelper.SimpleCallback itCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new NHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itHelper = new ItemTouchHelper(itCallback);
        itHelper.attachToRecyclerView(recycler);
    }

    public ArrayList<Note> readNotes() {
        return new NHandler(this).readNotes();
    }

    public void loadNotes() {
        notes = readNotes();
        adapter = new NoteAdapter(notes, this, new NoteAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                openEdit(notes.get(position).getId(), view);
            }
        });
        recycler.setAdapter(adapter);
    }

    private void openEdit(int id, View view) {
        NHandler handler = new NHandler(this);
        Note note = handler.readSingleNote(id);
        Intent intent = new Intent(this, edit.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("id", note.getId());

        ActivityOptionsCompat actOptComp = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent, 1, actOptComp.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
            loadNotes();
    }
}