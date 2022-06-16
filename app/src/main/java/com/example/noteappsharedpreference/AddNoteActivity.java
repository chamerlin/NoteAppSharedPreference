package com.example.noteappsharedpreference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashSet;

public class AddNoteActivity extends AppCompatActivity {

    int index;
    EditText title;
    EditText description;
    ImageButton addNote;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        index = getIntent().getIntExtra("data", -1);
        title = findViewById(R.id.et_title);
        description = findViewById(R.id.et_description);

        if(index == -1){
            MainActivity.arrayListTitle.add("");
            MainActivity.arrayListDescription.add("");
            index = MainActivity.arrayListTitle.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        } else {
            title.setText(MainActivity.arrayListTitle.get(index));
            description.setText(MainActivity.arrayListDescription.get(index));
        }

        addNote = findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MainActivity.arrayListTitle.set(index, title.getText().toString());
                MainActivity.arrayListDescription.set(index, description.getText().toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();

                sharedPreferences = getApplicationContext().getSharedPreferences(
                        "com.example.noteappsharedpreference",
                        MODE_PRIVATE
                );

                HashSet<String> hashSetTitle = new HashSet<String>(MainActivity.arrayListTitle);
                HashSet<String> hashSetDescription = new HashSet<String>(MainActivity.arrayListDescription);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putStringSet("title", hashSetTitle);
                sharedPreferencesEditor.putStringSet("description", hashSetDescription);
                sharedPreferencesEditor.apply();

                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onClickBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}