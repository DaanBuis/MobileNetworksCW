package org.me.gcu.coursework;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.me.gcu.coursework.R;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final int POST_REVIEW_REQUEST_CODE = 0;
    private static final String PREFS_NAME = "AppSettings";
    private static final String THEME_KEY = "theme";

    private Button navReview;
    private Button postReview;
    private Button profilePage;
    private Button searchPage;
    private String tempUploadData;
    private TextView text;
    private String[] uploadData;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkTheme = prefs.getBoolean(THEME_KEY, false); // Default to light theme

        // Set the appropriate night mode based on the stored preference
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);
        navReview = (Button)findViewById(R.id.reviewBH);
        postReview = (Button)findViewById(R.id.createRBH);
        profilePage = (Button)findViewById(R.id.profileBH);
        searchPage = (Button)findViewById(R.id.searchBH);


        navReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReviewPage.class);
                intent.putExtra(Intent.EXTRA_TEXT, tempUploadData);
                startActivity(intent);
            }
        });

        searchPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchPage.class);
                intent.putExtra(Intent.EXTRA_TEXT, tempUploadData);
                startActivity(intent);
            }
        });

        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PostReviewPage.class);

                startActivityForResult(intent, POST_REVIEW_REQUEST_CODE );
            }
        });

        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);

                startActivity(intent);
            }
        });
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();

        // providing title for the ActionBar
        actionBar.setTitle("");



        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == POST_REVIEW_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                tempUploadData = data.getStringExtra(Intent.EXTRA_TEXT);

            }
        }
    }

    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.backpage) {
                Toast.makeText(this, "Backpage Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}