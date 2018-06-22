package org.asl19.qod;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import ie.equalit.ouinet.Ouinet;


public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Ouinet _ouinet;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _ouinet = new Ouinet(this);

        _ouinet.setInjectorEndpoint("127.0.0.1:7070");
        _ouinet.setIPNS("QmQdMc9wqxhmcwr5iYn4t48EDVUCD96FjyVn7ZdgowkxL2");
        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());


        //Downloading the quote of the day
        String qod_api_url = "http://quotes.rest/qod.json";

        OkHttpClient qod_client = new OkHttpClient();

        Request qod_request =  new Request.Builder().url(qod_api_url).build();

        qod_client.newCall(qod_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject qod_json = new JSONObject(response.body().string());
                        final String quote_of_the_day = qod_json.getJSONObject("contents").getJSONArray("quotes").getJSONObject(0).getString("quote");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(quote_of_the_day);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
