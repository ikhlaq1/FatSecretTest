package broken.shotgun.fatsecret.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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


public class HomeActivity extends ActionBarActivity {
    private static final String ACCESS_TOKEN_MISSING = "gone";

    private static final String TAG = HomeActivity.class.getName();
    String subString;
    String responce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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

        final TextView responseText = (TextView) findViewById(R.id.responseText);
        responseText.setText("Searching foods for " + message + "...");

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
                            if (response.isSuccessful()) {

                                responce = response.body().string();
                                String jsonInString = responce;

                                //parsing json from string into jsonObject
                                JSONObject mainObject = new JSONObject(jsonInString);
                                //getting root element of json and saving it in jsonobject
                                JSONObject rootElement = mainObject.getJSONObject("foods");
                                //getting food array from json where all food items are stored
                                JSONArray uniName = rootElement.getJSONArray("food");
                                String foodId = uniName.getJSONObject(0).getString("food_description").toString();
                                String match = "Calories";
                                String end = "kcal";
                                int position = foodId.indexOf(match);
                                int endOf = foodId.indexOf(end);
                                subString = foodId.substring(position, endOf);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        responseText.setText("Amina khaley " + message + " isme sirf " + subString + " hai");
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

        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
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
