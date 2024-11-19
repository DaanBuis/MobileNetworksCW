package org.me.gcu.coursework;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ReviewPage extends AppCompatActivity {

    TextView nameText;
    TextView locationText;
    ImageView photo;
    RatingBar ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_page);

        Intent intent = getIntent();

        String text = intent.getStringExtra(Intent.EXTRA_TEXT);




        nameText = (TextView)findViewById(R.id.rpName);
        locationText = (TextView)findViewById(R.id.rpLocation);
        photo = (ImageView)findViewById(R.id.rpPhoto);
        ratings = (RatingBar)findViewById(R.id.rpratingBar);




        if (text != null) {
            String[] strSplit = text.split("#");
            nameText.setText(strSplit[0]);
            locationText.setText(strSplit[1]);
            ratings.setRating(Float.parseFloat(strSplit[2]));
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "review_image.jpg");
            if (file.exists()) {
                Uri imageUri = Uri.fromFile(file);
                photo.setImageURI(imageUri);  // Display the saved image in an ImageView
            }
        }






        ActionBar actionBar = getSupportActionBar();

       actionBar.setTitle("");



        // methods to display the icon in the ActionBar
       actionBar.setDisplayUseLogoEnabled(true);
       actionBar.setDisplayShowHomeEnabled(true);




    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(ReviewPage.this,SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.backpage) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}