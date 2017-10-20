package broken.shotgun.fatsecret.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import broken.shotgun.fatsecret.R;

public class SecondActivity extends AppCompatActivity {



    Button newButton;
    EditText fooditem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        newButton = (Button) findViewById(R.id.button);
        fooditem= (EditText) findViewById(R.id.autoCompleteTextView);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodItem = fooditem.getText().toString();

                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("message", foodItem);
                startActivity(intent);

            }
        });
    }
}
