package org.me.gcu.coursework;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import java.util.List;
import java.util.Locale;
import android.location.LocationManager;
import android.provider.Settings;

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
    Button getLocation;
    private TrackGPS gps;
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int GPS_REQUEST_CODE = 60;
    String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION};


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
        getLocation = (Button)findViewById(R.id.getLocBut);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {

                    if (checkSelfPermission(mPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostReviewPage.this, mPermission, REQUEST_CODE_PERMISSION);
                        return;
                    }
                }

                gps = new TrackGPS(PostReviewPage.this);

                if (isGPSEnabled()) {
                    if (gps.canGetLocation()) {
                        getAddressFromLocation(gps.getLatitude(), gps.getLongitude());

                    } else {
                        gps.showAlert();
                    }
                } else {
                    // Show an alert to prompt the user to enable GPS
                    promptForGPS();
                }
            }
        });



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

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void promptForGPS() {
        new android.app.AlertDialog.Builder(this)
                .setMessage("GPS is not enabled. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    // Redirect the user to the location settings
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPS_REQUEST_CODE);  // Use the GPS request code
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .show();
    }



    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0); // Full address line
                locationText.setText(addressText); // Set the address in the EditText
            } else {
                Toast.makeText(PostReviewPage.this, "No address found for current location", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PostReviewPage.this, "Geocoder service failed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(PostReviewPage.this,SettingsActivity.class);
            startActivity(intent);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle GPS settings result
        if (requestCode == GPS_REQUEST_CODE) {
            if (isGPSEnabled()) {
                if (gps.canGetLocation()) {
                    getAddressFromLocation(gps.getLatitude(), gps.getLongitude());
                }
            } else {
                Toast.makeText(this, "GPS is still disabled. Unable to get location.", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle image selection result
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                // Update the preview image in the layout
                photo.setImageURI(selectedImageUri);
                saveImageToExternalStorage(selectedImageUri);
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
