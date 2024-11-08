package org.me.gcu.coursework;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class ProfilePage extends AppCompatActivity {

    int SELECT_PICTURE = 201;
    private ImageView profilePic;
    private Button randomiser;
    private Button updateButton;
    private EditText nameText;
    private EditText phoneText;
    private EditText emailText;
    private EditText addressText;
    private Button changePic;

    private String name;
    private String email;
    private String phone;
    private String address;

    private String fileName = "data.txt";





    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        String[] randomNames = {"SilverPine", "OceanEcho", "CrystalWave", "SkyBlossom", "BrightBreeze", "QuietCloud", "LunarStone", "VelvetStorm", "GoldenFern", "SilentShade", "EchoingStream", "FrostWhisper", "BlueCedar", "MoonlitPath", "GoldenHorizon", "StarlitField", "RadiantBloom", "CleverFox", "ShadowRaven", "BlueMoss", "TwilightGlade", "GoldenPebble", "AmberDawn", "EmeraldGale", "RusticFog", "MysticEmber"};

        ActionBar actionBar = getSupportActionBar();

        updateButton = (Button)findViewById(R.id.pUpdateButton);
        nameText = (EditText)findViewById(R.id.pNameText);
        phoneText = (EditText)findViewById(R.id.pPhoneText);
        emailText = (EditText)findViewById(R.id.pEmailText);
        addressText = (EditText)findViewById(R.id.pAddressText);
        randomiser = (Button)findViewById(R.id.randomButton);
        changePic = (Button)findViewById(R.id.changePic);
        profilePic = (ImageView)findViewById(R.id.profilePic);

        initialiseData();




        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameText.getText().toString()  ;
                email = emailText.getText().toString();
                phone = phoneText.getText().toString();
                address = addressText.getText().toString();

                String content = name + "\n" + email + "\n" + phone + "\n" + address + "\n";

                writeToFile("file.txt", content);

            }
        });

        randomiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ranName = randomNames[(int) (Math.random() * 26)];

                nameText.setText(ranName);
            }
        });

        // providing title for the ActionBar
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

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.backpage) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialiseData() {
        String content = readFromFile("file.txt");

        String[] lines = content.split(System.lineSeparator());
        String lineLength = String.valueOf(lines.length);

        if (lines.length == 4) {
            nameText.setText(lines[0]);
            emailText.setText(lines[1]);
            phoneText.setText(lines[2]);
            addressText.setText(lines[3]);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "saved_image.jpg");
            if (file.exists()) {
                Uri imageUri = Uri.fromFile(file);
                profilePic.setImageURI(imageUri);  // Display the saved image in an ImageView
            }

        }



        // Toast.makeText(getApplicationContext(), lineLength, Toast.LENGTH_SHORT).show();
    }

    public String readFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }


    }

    public void writeToFile(String fileName, String content) {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "file.txt"),false);
            writer.write(content.getBytes());
            writer.close();
            //Toast.makeText(getApplicationContext(), "Wrote", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                    profilePic.setImageURI(selectedImageUri);
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
