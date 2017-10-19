package broken.shotgun.fatsecret.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import broken.shotgun.fatsecret.R;
import broken.shotgun.fatsecret.utils.FatSecretUtils;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {
    private static final String ACCESS_TOKEN_MISSING = "gone";

    private static final String TAG = HomeActivity.class.getName();
    private CurrentData mCurrentData;
    private TextView name;
    private TextView descriptionText;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name= (TextView) findViewById(R.id.responseText);
        descriptionText= (TextView) findViewById(R.id.description);

        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("message");
        SharedPreferences pref = getSharedPreferences(FatSecretUtils.PREFERENCES_FILE, MODE_PRIVATE);
        String accessToken = pref.getString(FatSecretUtils.OAUTH_ACCESS_TOKEN_KEY, ACCESS_TOKEN_MISSING);

        if (accessToken.equals(ACCESS_TOKEN_MISSING)) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
            return;
        }

        FatSecretUtils.setContext(this);

        TextView loggedInText = (TextView) findViewById(R.id.loggedInText);
        loggedInText.setText("auth token = " + pref.getString("oauth_access_token", ACCESS_TOKEN_MISSING));

        /*final TextView name = (TextView) findViewById(R.id.name);*/
       // name.setText("Searching foods for " + message + "...");

        try {
            String signedFoodSearchUrl = FatSecretUtils.sign("http://platform.fatsecret.com/rest/server.api?method=foods.search&format=json&search_expression=" + message + "");
            Log.d(TAG, "Signed foods.search URL = " + signedFoodSearchUrl);
            if (isNetworlAvailable()) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(signedFoodSearchUrl).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            HomeActivity.this.response = response.body().string();
                            String jsonInString = HomeActivity.this.response;
                            if (response.isSuccessful()) {
                                mCurrentData = getCurrentDetails(jsonInString);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        updateDisplay();
                                    }
                                });
                                // Toast.makeText(getApplicationContext(), subString, Toast.LENGTH_LONG).show();
                            } else {
                                alertUserAboutError();
                            }
                        } catch (IOException e) {
                            Log.e("errroorrr", "" + e);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                alertUserAboutError();
            }

        } catch (OAuthExpectationFailedException | OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
    }



        private CurrentData getCurrentDetails(String jsonInString) throws JSONException {
        JSONObject mainObject = new JSONObject(jsonInString);
        //getting root element of json and saving it in jsonobject
        JSONObject rootElement = mainObject.getJSONObject("foods");
        //getting food array from json where all food items are stored
        JSONArray uniName = rootElement.getJSONArray("food");
        String food_name = uniName.getJSONObject(0).getString("food_name").toString();
        String food_description = uniName.getJSONObject(0).getString("food_description").toString();
        /*String match = "Calories";
        String end = "kcal";*/
       /* int position = food_name.indexOf(match);
        int endOf = food_name.indexOf(end);
        subString = food_name.substring(position, endOf);*/
        CurrentData currentData = new CurrentData();
        currentData.setFood_name(food_name);
        currentData.setFood_description(food_description);
       //Log.i(TAG,food_name);
        return currentData;
    }

    private void updateDisplay() {
        name.setText(mCurrentData.getFood_name());
        descriptionText.setText(mCurrentData.getFood_description());
    }
    private boolean isNetworlAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        boolean isAvailable=false;
        if (networkInfo!=null && networkInfo.isConnected()){
                isAvailable= true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(),"error_dialog");
    }
}
