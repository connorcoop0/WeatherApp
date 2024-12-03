package com.example.assignment5_cjc22m;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // UI elements and logging tag
    private TextInputEditText editTextCity;
    private ListView listViewWeather;
    private FloatingActionButton buttonFetch;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI elements
        editTextCity = findViewById(R.id.editTextCity);
        listViewWeather = findViewById(R.id.listViewWeather);
        buttonFetch = findViewById(R.id.buttonFetch);

        // Set button click listener
        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get city name and fetch weather
                String city = editTextCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    new FetchWeatherTask().execute(city);
                } else {
                    Snackbar.make(v, "Please enter a city name", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    // AsyncTask to fetch weather data
    private class FetchWeatherTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String city = params[0];
            String apiKey = "22357f78602beb2fc28d96c98ae0aa22";
            String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&cnt=16&appid=" + apiKey;
            ArrayList<String> weatherList = new ArrayList<>();

            try {
                URL url = new URL(urlString);
                Log.d(TAG, "URL: " + urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonResponse.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject forecast = jsonArray.getJSONObject(i);
                        JSONObject main = forecast.getJSONObject("main");
                        double tempInK = main.getDouble("temp");
                        double tempInC = tempInK - 273.15;
                        double tempInF = (tempInC * 9/5) + 32;
                        String dateTime = forecast.getString("dt_txt");

                        // Format the date and time
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault());
                        Date date = inputFormat.parse(dateTime);
                        String formattedDateTime = outputFormat.format(date);

                        // Add hydration reminder when it's too hot
                        String weatherInfo;
                        if (tempInF >= 88)
                            weatherInfo = formattedDateTime + ": " + String.format(Locale.getDefault(), "%.2f", tempInF) + "°F - HYDRATE";
                        else
                            weatherInfo = formattedDateTime + ": " + String.format(Locale.getDefault(), "%.2f", tempInF) + "°F";

                        weatherList.add(weatherInfo);
                    }
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    reader.close();
                    Log.e(TAG, "Error Response: " + errorResponse.toString());
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage(), e);
            }

            return weatherList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> weatherList) {
            super.onPostExecute(weatherList);
            if (weatherList.isEmpty()) {
                Snackbar.make(findViewById(R.id.buttonFetch), "Failed to fetch data", Snackbar.LENGTH_SHORT).show();
            } else {
                WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, weatherList);
                listViewWeather.setAdapter(adapter);
            }
        }
    }
}
