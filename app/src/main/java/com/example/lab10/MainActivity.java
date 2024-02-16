package com.example.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView twProgress, twRating;
    RatingBar ratingBar;
    Button bStart, bAdd, bSub;
    Thread loadingThread;


    int rating = 0, maxRating = 5, ratingStep = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        twProgress = findViewById(R.id.textViewProgress);
        ratingBar = findViewById(R.id.ratingBar);
        bStart = findViewById(R.id.start);
        bAdd = findViewById(R.id.add);
        bSub = findViewById(R.id.sub);
        twProgress = (TextView)findViewById(R.id.textViewProgress);
        twRating = (TextView)findViewById(R.id.textViewRating);

        ratingBar.setNumStars(maxRating);
        ratingBar.setRating(rating);
        ratingBar.setStepSize(ratingStep);

        Handler handler = new Handler(Looper.getMainLooper());

        Runnable incProgress = () -> {
            progressBar.setProgress(progressBar.getProgress()+1);
            twProgress.setText(String.valueOf(progressBar.getProgress()) );
        };
        Runnable alertProgressDone = () -> {
            Toast.makeText(this,"done", Toast.LENGTH_SHORT).show();
        };

        Runnable loadingProcess = () -> {
            int progress = 0;
            while (progress !=100){
                try {
                    Thread.sleep(100/(rating+1));
                } catch (InterruptedException e) {throw new RuntimeException(e);}
                progress++;
                handler.post(incProgress);
//                twProgress.setText(progress);
//                progressBar.setProgress(progress);
            }
            handler.post(alertProgressDone);
//            Toast.makeText(this,"done", Toast.LENGTH_SHORT).show();
        };

        loadingThread = new Thread(loadingProcess);

        bStart.setOnClickListener(v -> {
            if (loadingThread.isAlive()){
                Toast.makeText(this,"already running", Toast.LENGTH_SHORT).show();
            } else { // restart
                loadingThread = new Thread(loadingProcess);
                progressBar.setProgress(0);
                twProgress.setText("0");
                loadingThread.start();
            }
        });

        bAdd.setOnClickListener(v -> {
            if (rating < maxRating){
                rating++;
                ratingBar.setRating(rating);
                twRating.setText(String.valueOf(rating));
            }
        });
        bSub.setOnClickListener(v -> {
            if (rating > 0){
                rating--;
                ratingBar.setRating(rating);
                twRating.setText(String.valueOf(rating));
            }
        });
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = (int) ratingBar.getRating();
            twRating.setText(String.valueOf(this.rating));
        });

        progressBar.setProgress(0);

        twRating.setText(String.valueOf(rating));

    }
}