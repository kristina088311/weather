package com.example.finedweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GetDataFromInternet.AsyncResponse{

    private static final String TAG = "MainActivity";

    @SuppressLint("WrongViewCast")
    private Button searchButton;
    private EditText searchField;
    private TextView cityName;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton=findViewById(R.id.searchField);
        cityName=findViewById(R.id.cityName);

        searchButton=findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(this);
        try {
            URL url=new URL("https://api.openweathermap.org/data/2.5/weather?q=Kazan&appid=f10bf2feedca86136156bda01bc6092a");
            new GetDataFromInternet(this).execute(url);

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v){
        //  URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Kazan&appid=f10bf2feedca86136156bda01bc6092a");
        URL url=buildUrl(searchField.getText().toString());
        cityName.setText(searchField.getText().toString());

        new GetDataFromInternet(this).execute(url);
        
    }

    private URL buildUrl (String city){

        String BASE_URL="https://api.openweathermap.org/data/2.5/weather";
        String PARAM_CITY="q";
        String PARAM_APPID="appid";
        String appid_value="f10bf2feedca86136156bda01bc6092a";

        Uri builtUri=Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_CITY,city).appendQueryParameter(PARAM_APPID,appid_value).build();
        URL url=null;

        try{
            url=new URL(builtUri.toString());

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "buildUrl: "+url);
        return url;
    }

@Override
    public void proccessFinish(String output){
        Log.d(TAG, "proccessFinish: "+output);
        try {

            JSONObject resultJSON=new JSONObject(output);
            JSONObject weather=resultJSON.getJSONObject("main");
            JSONObject sys=resultJSON.getJSONObject("sys");

            TextView temp=findViewById(R.id.tempValue);
            String temp_k=weather.getString("temp");
            float temp_C=Float.parseFloat(temp_k);
            temp_C=temp_C-(float) 273.15;
            String temp_C_string=Float.toString(temp_C);
            temp.setText(temp_C_string);

            TextView pressure=findViewById(R.id.pressureValue);
            pressure.setText(weather.getString("pressure"));

            TextView sunrise=findViewById(R.id.timeSunrise);
            String timeSunrise=sys.getString("sunrise");
            Locale mylocale=new Locale("ru","RU");
            SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss",mylocale);

            String dateString= formatter.format(new Date (Long.parseLong(timeSunrise)*1000+(60*60*1000)*3));

            sunrise.setText(dateString);


            TextView sunset=findViewById(R.id.timeSunset);
            String timeSunset=sys.getString("sunset");
            //Locale mylocale=new Locale("ru","RU");
            //SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss",mylocale);

           dateString= formatter.format(new Date(Long.parseLong(timeSunset)*1000+(60*60*1000)*3));

            sunset.setText(dateString);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

