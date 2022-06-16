package com.example.noteappsharedpreference;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    static ArrayAdapter arrayAdapter;
    private ListView listView;
    static ArrayList<String> arrayListTitle = new ArrayList<String>();
    static ArrayList<String> arrayListDescription = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getApplicationContext().getSharedPreferences(
                "com.example.noteappsharedpreference",
                MODE_PRIVATE
        );

        HashSet<String> hashSetTitle = (HashSet<String>) sharedPreferences.getStringSet("title", null);
        HashSet<String> hashSetDescription = (HashSet<String>) sharedPreferences.getStringSet("description", null);
        if(hashSetTitle != null){
            arrayListTitle = new ArrayList<>(hashSetTitle);
        }
        if(hashSetDescription != null){
            arrayListDescription = new ArrayList<>(hashSetDescription);
        }

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, R.layout.list_item_bg, arrayListTitle);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = (String) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("data", position);
//                intent.putExtra("description", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this?")
                        .setPositiveButton(
                                "Yes", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        arrayListTitle.remove(position);
                                        arrayListDescription.remove(position);
                                        arrayAdapter.notifyDataSetChanged();

                                        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.noteappsharedpreference", Context.MODE_PRIVATE);
                                        HashSet<String> hashSetTitle = new HashSet<String>(MainActivity.arrayListTitle);
                                        HashSet<String> hashSetDescription = new HashSet<String>(MainActivity.arrayListDescription);
                                        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
                                        sharedPreferenceEditor.putStringSet("title", hashSetTitle);
                                        sharedPreferenceEditor.putStringSet("description", hashSetDescription);
                                        sharedPreferenceEditor.apply();
                                        Toast.makeText(MainActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).setNegativeButton("No", null).show();

                return true;
            }
        });

        searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                arrayAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    public void onClickFab(View view){
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    public void onClickSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}