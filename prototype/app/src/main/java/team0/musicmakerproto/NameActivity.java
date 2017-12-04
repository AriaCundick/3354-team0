package team0.musicmakerproto;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NameActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create new intent for the main page activity.
        final Intent mainPageIntent = new Intent(NameActivity.this, LibraryActivity.class);

        //If the user has already entered their name, skip this page.
        if(checkForName())
        {
            startActivity(mainPageIntent);
            finish();
        }

        //If the user has not entered their name, create the page.
        super.onCreate(savedInstanceState);
        getPermission();
        setContentView(R.layout.activity_name);
        btn = (Button)findViewById(R.id.un_btn);

        //Create OnClickListener to send page to main activity once the button is pressed.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameRuleCheck()) {
                    saveName();
                    startActivity(mainPageIntent);
                    finish();
                }
            }
        });
    }

    //Description: Gets permission from the user to query the device's external storage
    //Returns ArrayList of all songs on the device.
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermission()
    {
        //Check if permission to access external storage is granted.
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

    }

    //method is invoked when user hits grant/deny on a permission pop-up.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) //switch case for request code
        {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Permission granted! query songs.
                    Toast.makeText(NameActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(NameActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Description: Check to see if the user has already entered their name.
     *              If their name has been entered, return true.
     *              Otherwise, return false.
     * return true if the name exists.
     */
    private boolean checkForName() {
        //Check to see if the user has already entered their name.
        String filename = "username.txt";
        FileInputStream is;

        try {
            is = openFileInput(filename);
            BufferedReader bufr = new BufferedReader(new InputStreamReader(new DataInputStream(is)));
            String name = "";
            name = bufr.readLine();
            bufr.close();
            is.close();

            //If nothing was read from the file, then the name does not exist. Return false.
            if( name.equals("") )
                return false;
            else                //Else the name exists.
                return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        //If any I/O error occurs, return false.
        return false;
    }

    /*
     * Description: Save the user's name to a file to be retrieved later.
     */
    private void saveName() {
        String filename =  "username.txt";
        FileOutputStream outputStream;

        //Parse the string from the text field where the user enters their name.
        String name = ((EditText)findViewById(R.id.username)).getText().toString().trim() + "\n";
        try {

            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(filename.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Description: Checks to see if the name entered meets the requirements.
     *              (i.e. not blank).
     * Return true if name follows constraints
     */
    private boolean nameRuleCheck() {
        //Parse the string from the text field where the user enters their name.
        String name = ((EditText)findViewById(R.id.username)).getText().toString().trim() + "\n";

        //Invalid name, return false.
        if(name.equals("\n"))
        {
            //Make toast to user to tell them what was wrong with their input.
            Toast.makeText(this, "Name can't be blank.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
