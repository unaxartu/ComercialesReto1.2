package com.example.comercialesreto12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Initialize HTTP client
        client = new OkHttpClient();

        // Set up the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Call the method to login
                try {
                    loginToOdoo(username, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error in request.");
                }
            }
        });
    }

    private void loginToOdoo(String username, String password) throws JSONException {
        // Create JSON-RPC object
        JSONObject json = new JSONObject();
        json.put("jsonrpc", "2.0");
        json.put("method", "call");

        JSONObject params = new JSONObject();
        params.put("service", "object");
        params.put("method", "execute_kw");

        // Request arguments
        JSONObject args = new JSONObject();
        args.put("db", "Madagascar"); // Change this to your database name
        args.put("uid", 2);  // User ID
        args.put("password", "admin"); // Access password (for validation)
        args.put("model", "res.users"); // User model
        args.put("method", "search_read"); // Method to read users
        args.put("args", new JSONArray().put(new JSONArray())); // Empty filter

        // Fields to return
        JSONObject fields = new JSONObject();
        fields.put("fields", new JSONArray().put("id").put("login").put("name").put("email"));

        // Structure the request
        json.put("params", params);
        json.put("id", 2);

        // Make the HTTP request using OkHttp
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Request request = new Request.Builder()
                .url("remoto.cebanc.com:20203/jsonrpc") // Change this to your Odoo server URL
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showToast("Connection error.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        if (jsonResponse.has("result")) {
                            // Validate if the username and password are correct
                            showToast("Login successful.");
                            // Navigate to the next activity, if necessary
                        } else {
                            showToast("Incorrect credentials.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Error processing response.");
                    }
                }
            }
        });
    }

    // Method to show a Toast message
    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
