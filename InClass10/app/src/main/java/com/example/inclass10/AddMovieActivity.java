package com.example.inclass10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_name;
    private EditText et_description;
    private Spinner genre_drop;
    private SeekBar seekBar;
    private TextView tv_rating1;
    private EditText et_year;
    private EditText et_imdb;
    private Button btn_addmovie;

    String movie_name;
    String movie_description;
    String selected_genre;
    int rating;
    int movie_year;
    String movie_imdb;
    String defaultTextForSpinner = "text here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        setTitle("Add Movie");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Map<String, Object> moviesMap = new HashMap<>();

        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        seekBar = findViewById(R.id.seekBar);
        tv_rating1 = findViewById(R.id.tv_rating1);
        et_year = findViewById(R.id.et_year);
        et_imdb = findViewById(R.id.et_imdb);
        btn_addmovie = findViewById(R.id.btn_addmovie);
        genre_drop = findViewById(R.id.genre_drop);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                tv_rating1.setText(String.valueOf(i));
                rating = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genre_drop.setAdapter(adapter);
        genre_drop.setOnItemSelectedListener(this);

        btn_addmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_name = et_name.getText().toString();
                movie_description = et_description.getText().toString();

                final String tempyear = et_year.getText().toString();
                if (tempyear != null && !tempyear.equals("")) {
                    movie_year = Integer.parseInt(tempyear);
                }
                movie_imdb = et_imdb.getText().toString();

                if (movie_name.trim().equalsIgnoreCase("")) {
                    et_name.setError("Invalid Input");
                } else if (movie_description.trim().equalsIgnoreCase("")) {
                    et_description.setError("Invalid Input");
                } else if (selected_genre.equals("Select")) {
                    Toast.makeText(getApplicationContext(), "Please Select a Genre", Toast.LENGTH_SHORT).show();
                } else if (et_year.getText().toString().trim().equalsIgnoreCase("")) {
                    et_year.setError("Invalid Input");
                } else if (movie_imdb.trim().equalsIgnoreCase("")) {
                    et_imdb.setError("Invalid Input");
                } else {
                    Movie addMovie = new Movie(movie_name, movie_description, selected_genre, rating, movie_year, movie_imdb);
                    Toast.makeText(getApplicationContext(), "New Movie Added: " + movie_name, Toast.LENGTH_SHORT).show();
//                    moviesMap.put(movie_name, addMovie);


                    db.collection("movies").document().set(addMovie);
                    finish();
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_genre = parent.getItemAtPosition(position).toString();
        Log.d("demo", selected_genre);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
