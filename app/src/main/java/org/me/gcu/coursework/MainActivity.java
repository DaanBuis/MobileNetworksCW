package org.me.gcu.coursework;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private Button navReview;
    private Button postReview;
    private String tempUploadData;
    private TextView text;
    private String[] uploadData;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navReview = (Button)findViewById(R.id.reviewBH);
        postReview = (Button)findViewById(R.id.createRBH);


        navReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReviewPage.class);
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

                text = (TextView)findViewById(R.id.textView);
                text.setText(tempUploadData);
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
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.backpage) {
                Toast.makeText(this, "Backpage Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}