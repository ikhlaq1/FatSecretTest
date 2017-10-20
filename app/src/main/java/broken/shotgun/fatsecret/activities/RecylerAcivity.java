package broken.shotgun.fatsecret.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.Arrays;

import broken.shotgun.fatsecret.R;

public class RecylerAcivity extends AppCompatActivity {
    private TextView name;
    CurrentData[] mCurrentData;
    private TextView descriptionText;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_recy_layout);
        recyclerView= (RecyclerView) findViewById(R.id.myrecyclerView);

       /* name= (TextView) findViewById(R.id.responseText);
        descriptionText= (TextView) findViewById(R.id.description);*/

        Intent intent= getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(HomeActivity.DATA);
        mCurrentData= Arrays.copyOf(parcelables,parcelables.length,CurrentData[].class);


        DataAdapter adapter =new DataAdapter(this,mCurrentData);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
    }
}
