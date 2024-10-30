package org.me.gcu.coursework;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.me.gcu.coursework.R;

public class ReviewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_page);

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
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.backpage) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}