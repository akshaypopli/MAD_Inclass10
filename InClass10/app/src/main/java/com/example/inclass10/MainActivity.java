package com.example.inclass10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    // MAD InClass 10
    // MainActivity
    // Akshay Popli

    private Button add_movie;
    private Button edit_movie;
    private Button delete_movie;
    private Button show_year;
    private Button show_rating;
    public ArrayList<Movie> movielist = new ArrayList<>();
    public ArrayList<String> ids = new ArrayList<>();

    int movieindex;


    static String MOVIE_KEY = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Favorite Movies");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        add_movie = findViewById(R.id.add_movie);
        edit_movie = findViewById(R.id.edit_movie);
        delete_movie = findViewById(R.id.delete_movie);
        show_year = findViewById(R.id.show_year);
        show_rating = findViewById(R.id.show_rating);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a Movie");

        add_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
                intent.putExtra(MOVIE_KEY, 100);
                startActivityForResult(intent, 1);
            }
        });

        edit_movie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ArrayList<String> names = new ArrayList<>();
                db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("data", document.getId() + " => " + document.getData());
                                ids.add(document.getId());

                                names.add(document.getData().get("name").toString());

                            }
                            final String[] movieArr = names.toArray(new String[names.size()]);

                            builder.setItems(movieArr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i = new Intent(MainActivity.this, EditMovieActivity.class);
                                    i.putExtra("movieSelected", ids.get(which));
                                    startActivity(i);
                                }
                            });
                            final AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Log.d("data", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

        delete_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> names = new ArrayList<>();
                db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("data", document.getId() + " => " + document.getData());
                                ids.add(document.getId());

                                names.add(document.getData().get("name").toString());

                            }
                            final String[] movieArr = names.toArray(new String[names.size()]);

                            builder.setItems(movieArr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("movies").document(ids.get(which)).delete();
                                    Toast.makeText(getApplicationContext(), "Movie Deleted: " + movieArr[which], Toast.LENGTH_SHORT).show();
                                }
                            });
                            final AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Log.d("data", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

        show_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Movie> moviesData = new ArrayList<>();
                db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Movie m = new Movie(queryDocumentSnapshot.get("name").toString(), queryDocumentSnapshot.get("description").toString(),
                                    queryDocumentSnapshot.get("genre").toString(), Integer.parseInt(queryDocumentSnapshot.get("rating").toString()),
                                    Integer.parseInt(queryDocumentSnapshot.get("year").toString()), queryDocumentSnapshot.get("imdb").toString());

                            moviesData.add(m);
                        }
                        if(moviesData.size() != 0){
                            Intent intent = new Intent(MainActivity.this, ListActivity.class);
                            intent.putExtra("allmovies", movielist);
                            intent.putExtra(MOVIE_KEY, 400);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No Movies Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        show_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Movie> moviesData = new ArrayList<>();
                db.collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Movie m = new Movie(queryDocumentSnapshot.get("name").toString(), queryDocumentSnapshot.get("description").toString(),
                                    queryDocumentSnapshot.get("genre").toString(), Integer.parseInt(queryDocumentSnapshot.get("rating").toString()),
                                    Integer.parseInt(queryDocumentSnapshot.get("year").toString()), queryDocumentSnapshot.get("imdb").toString());

                            moviesData.add(m);
                        }
                        if(moviesData.size() != 0){
                            Intent intent = new Intent(MainActivity.this, ListActivity.class);
                            intent.putExtra("allmovies", movielist);
                            intent.putExtra(MOVIE_KEY, 500);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No Movies Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Movie movie0 = (Movie) data.getExtras().getSerializable("movie1");
                Log.d("demo2", movie0.toString());
                movielist.add(movie0);
            } else if (resultCode == AddMovieActivity.RESULT_CANCELED) {
                Log.d("demo2", "no data");
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Movie movie0 = (Movie) data.getExtras().getSerializable("movie1");
                Log.d("demo2", movie0.toString());
                movielist.set(movieindex, movie0);
            } else if (resultCode == AddMovieActivity.RESULT_CANCELED) {
                Log.d("demo2", "no data");
            }
        }
    }
}
