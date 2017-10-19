package broken.shotgun.fatsecret.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import broken.shotgun.fatsecret.R;

public class SecondActivity extends ActionBarActivity {


    Button newButton;
    AutoCompleteTextView fooditem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        newButton = (Button) findViewById(R.id.button);
        fooditem= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

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
