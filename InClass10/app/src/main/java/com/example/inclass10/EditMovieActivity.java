package com.example.inclass10;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_name;
    private EditText et_description;
    private Spinner genre_drop;
    private SeekBar seekBar;
    private TextView tv_rating1;
    private EditText et_year;
    private EditText et_imdb;
    private Button btn_save;

    String movie_name;
    String movie_description;
    String selected_genre;
    int rating;
    int movie_year;
    String movie_imdb;
    String movieSelected;
    int stringPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        setTitle("Edit Movie");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        seekBar = findViewById(R.id.seekBar);
        tv_rating1 = findViewById(R.id.tv_rating1);
        et_year = findViewById(R.id.et_year);
        et_imdb = findViewById(R.id.et_imdb);
        btn_save = findViewById(R.id.btn_save);
        genre_drop = findViewById(R.id.genre_drop);

        movieSelected = getIntent().getExtras().getString("movieSelected");

        DocumentReference docRef = db.collection("movies").document(movieSelected);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("doc", document.get("name").toString());
                        et_name.setText(document.get("name").toString());
                        et_description.setText(document.get("description").toString());
                        et_year.setText(document.get("year").toString());
                        et_imdb.setText(document.get("imdb").toString());

                        rating = Integer.parseInt(document.get("rating").toString());
                        seekBar.setProgress(rating);
                        tv_rating1.setText(document.get("rating").toString());
                        selected_genre = document.get("genre").toString().trim();

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditMovieActivity.this,
                                R.array.genre_array, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        genre_drop.setAdapter(adapter);
                        String[] stringArray = getResources().getStringArray(R.array.genre_array);
                        for (int i = 0; i < stringArray.length; i++) {
                            if (stringArray[i].contains(selected_genre)) {
                                stringPos = i;
                                genre_drop.setSelection(stringPos);
                            }
                        }
                        genre_drop.setOnItemSelectedListener(EditMovieActivity.this);
                    }
                }
            }
        });


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


        btn_save.setOnClickListener(new View.OnClickListener() {
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
                    Movie movie1 = new Movie(movie_name, movie_description, selected_genre, rating, movie_year, movie_imdb);
                    Toast.makeText(getApplicationContext(), "Movie Updated: " + movie_name, Toast.LENGTH_SHORT).show();

//                    Intent returnIntent = getIntent();
//                    returnIntent.putExtra("movie1", movie1);
//                    setResult(Activity.RESULT_OK, returnIntent);

                    db.collection("movies").document(movieSelected)
                            .set(movie1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("failure", "DocumentSnapshot failure!");
                                }
                            });

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