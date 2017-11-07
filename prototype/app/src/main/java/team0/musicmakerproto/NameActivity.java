package team0.musicmakerproto;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class NameActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create new intent for the main page activity.
        final Intent mainPageIntent = new Intent(NameActivity.this, MainMenuActivity.class);

        //If the user has already entered their name, skip this page.
        if(checkForName())
        {
            startActivity(mainPageIntent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        btn = (Button)findViewById(R.id.un_btn);

        //Create OnClickListener to send page to main activity once the button is pressed.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveName();
                startActivity(mainPageIntent);
                finish();
            }
        });
    }

    /*
     * Author: Aria Cundick
     * Description: Check to see if the user has already entered their name.
     *              If their name has been entered, return true.
     *              Otherwise, return false.
     * @throws Exception An exception is thrown if an error with reading from the file occurs.
     * @return true if the name exists.
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
     * Author: Aria Cundick
     * Description: Save the user's name to a file to be retrieved later.
     * @throws Exception An exception is thrown if an error occurs with opening the output stream.
     * @return Nothing.
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
}
