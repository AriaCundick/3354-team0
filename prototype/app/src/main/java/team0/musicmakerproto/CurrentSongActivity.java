package team0.musicmakerproto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.graphics.Color;

public class CurrentSongActivity extends AppCompatActivity {

    // button variables
    ImageButton repeat;
    ImageButton shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        repeat = (ImageButton) findViewById(R.id.repeat);
        shuffle = (ImageButton) findViewById(R.id.shuffle);

        // repeat button onClickListener
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click buton code here
                repeat.setColorFilter(Color.parseColor("#6eb7a7"));
            }
        });

        // shuffle button onClickListener
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click buton code here
                shuffle.setColorFilter(Color.parseColor("#d19e4f"));
            }
        });
    }



}
