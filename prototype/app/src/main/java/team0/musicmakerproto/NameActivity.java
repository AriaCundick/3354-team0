package team0.musicmakerproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NameActivity extends AppCompatActivity {

    Button btn;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        btn = (Button)findViewById(R.id.un_btn);

        final Intent mainPageIntent = new Intent(NameActivity.this, MainMenuActivity.class);
       // myIntent.putExtra("username", value); //Optional parameters
       // NameActivity.this.startActivity(myIntent);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainPageIntent);
                finish();
            }
        });
    }
}
