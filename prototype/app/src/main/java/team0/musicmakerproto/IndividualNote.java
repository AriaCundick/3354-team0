package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class IndividualNote extends AppCompatActivity {

    DatabaseHelper SQLdb;
    EditText textWindow;
    String path, name, contents;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_note);

        //Bind GUI elements.
        textWindow = (EditText) findViewById(R.id.noteWindow);
        textWindow.setMovementMethod(new ScrollingMovementMethod());

        i = getIntent();
        path = i.getStringExtra("note_path");
        name = i.getStringExtra("note_name");
        contents = i.getStringExtra("note_contents");

        textWindow.setText(contents);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        SQLUpdateNote();
    }

    private void SQLUpdateNote()
    {
        //SQL update of notes db
        if(contents != textWindow.getText().toString())
        {
            //update here
            SQLdb = new DatabaseHelper(this);
            SQLdb.updateNote(name, textWindow.getText().toString());
            SQLdb.close();

        }
    }

    public void btnDone(View v)
    {


        //Activate next activity
        String caller = getIntent().getStringExtra("caller");
        Log.i("call", caller);
        Class c = null;
        try {

            c = Class.forName(caller);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        Intent intent;
        if(c != null)
            intent = new Intent(IndividualNote.this, c);
        else
            intent = new Intent (IndividualNote.this, NotesActivity.class);

        startActivity(intent);

    }
}
