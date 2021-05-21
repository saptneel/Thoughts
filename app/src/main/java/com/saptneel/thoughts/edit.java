package com.saptneel.thoughts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class edit extends AppCompatActivity {

    EditText edtTitle, edtDesc;
    Button btnCancel, btnSave;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        ll = findViewById(R.id.btnHolder);
        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDesc);

        edtDesc.setText(intent.getStringExtra("description"));
        edtTitle.setText(intent.getStringExtra("title"));

        btnCancel = findViewById(R.id.cancel);
        btnSave = findViewById(R.id.save);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note(edtTitle.getText().toString(), edtDesc.getText().toString());
                note.setId(intent.getIntExtra("id", 1));
                if(new NHandler(edit.this).update(note))
                    Toast.makeText(edit.this, "Note updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(edit.this, "Update Fail", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(ll);
        super.onBackPressed();
    }
}