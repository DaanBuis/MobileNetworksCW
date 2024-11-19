package org.me.gcu.coursework;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    private List<Location> locationList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("");



        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = (SearchView)findViewById(R.id.searchView);



        // Initialize location data
        locationList = new ArrayList<>();
        locationList.add(new Location("Eiffel Tower", "Av. Gustave Eiffel, 75007 Paris, France", 4.5F, R.drawable.tower));
        locationList.add(new Location("The Colosseum", "Piazza del Colosseo, 1, 00184 Roma RM, Italy", 4.0F, R.drawable.colosseum));
        locationList.add(new Location("Big Ben", "London SW1A 0AA", 3.5F, R.drawable.big_ben));
        locationList.add(new Location("McDonalds", "209/215 Argyle St, Glasgow G2 8DL", 1.5F, R.drawable.mcdonalds));
        locationList.add(new Location("Five Guys", "Unit R7, Silverburn Shopping Centre, Glasgow G53 6AG", 4.5F, R.drawable.five_guys));
        locationList.add(new Location("Alton Towers", "Farley Ln, Alton, Stoke-on-Trent ST10 4DB", 5.0F, R.drawable.alton_towers));

        locationAdapter = new LocationAdapter(this, locationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // We don't need to do anything when the query is submitted
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform filtering when the query changes
                locationAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(SearchPage.this,SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.backpage) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}