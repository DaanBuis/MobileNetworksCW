package org.me.gcu.coursework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import org.me.gcu.coursework.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class PostReviewPage extends AppCompatActivity {

    int SELECT_PICTURE = 200;

    EditText nameText;
    EditText locationText;
    ImageView photo;
    Button uploadPhoto;
    Button submitButton;
    RatingBar rating;
    private Uri tempUri;
    private String textBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_review_page);

        nameText = (EditText)findViewById(R.id.prpName);
        locationText = (EditText)findViewById(R.id.prpLocation);
        photo = (ImageView)findViewById(R.id.prpPhoto);
        uploadPhoto = (Button)findViewById(R.id.prpSubmitPhoto);
        submitButton = (Button)findViewById(R.id.prpSubmitReview);
        rating = (RatingBar)findViewById(R.id.prpRating);



        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the text from the EditText

                textBox = nameText.getText().toString() + "," + locationText.getText().toString() + "," + String.valueOf(rating.getRating());

                // put the String to pass back into an Intent and close this activity
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, textBox);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


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

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    photo.setImageURI(selectedImageUri);
                    saveImageToExternalStorage(selectedImageUri);
                }
            }
        }
    }

    private void saveImageToExternalStorage(Uri imageUri) {
        try {
            // Get the InputStream from the Uri
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // Create an output file in the external storage directory (e.g., Pictures)
            File externalDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!externalDirectory.exists()) {
                externalDirectory.mkdirs();
            }

            // Define the output file
            File outputFile = new File(externalDirectory, "saved_image.jpg");

            // Create OutputStream to write the image
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // Copy the content from the InputStream to the OutputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams
            inputStream.close();
            outputStream.close();

            Log.d("ExternalStorage", "Image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
