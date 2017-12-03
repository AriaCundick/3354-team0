package team0.musicmakerproto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;

public class IndividualNote extends AppCompatActivity {

    EditText textWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_note);

        //Bind GUI elements.
        textWindow = (EditText) findViewById(R.id.noteWindow);
        textWindow.setMovementMethod(new ScrollingMovementMethod());
    }


    public void btnDone(View v)
    {
        //SQL update of notes db
    }
}
