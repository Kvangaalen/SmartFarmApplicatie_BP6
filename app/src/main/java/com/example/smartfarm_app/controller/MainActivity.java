package com.example.smartfarm_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String gebruikersnaam, wachtwoord;
    EditText loginGebruiker, loginWachtwoord;
    Button butLogin, butNieuwAccount;

    private JSONObject objLoginGebruiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Controleren of er een gebruikersnaam beschikbaar is...
        if (com.example.smartfarm_app.controller.SaveSharedPreference.getUserName(MainActivity.this).length() != 0) {
            // gebruiker is niet ingelogt
            volleyLogin();
            System.out.println("call Login Activity");
        }
        ClearCredentials();
        NieuwAccount();
        Login();

    }

    private void Login() {
        butLogin = findViewById(R.id.butLogin);
        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyLogin();
            }
        });
    }

    private void volleyLogin() {
        if (loginGebruiker == null || loginWachtwoord == null) {
            gebruikersnaam = SaveSharedPreference.getUserName(this);
            wachtwoord = SaveSharedPreference.getUserWachtwoord(this);
        } else {
            gebruikersnaam = loginGebruiker.getText().toString();
            wachtwoord = loginWachtwoord.getText().toString();
        }

        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/logins";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(MainActivity.this, "gebruikersnaam of wachtwoord onjuist", Toast.LENGTH_SHORT).show();
                        } else {
                            SaveSharedPreference.setUserName(MainActivity.this, gebruikersnaam, wachtwoord);
                            Intent i = new Intent(MainActivity.this, UserMenu.class);
                            startActivity(i);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "VolleyError", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gebruikersNaam", gebruikersnaam);
                params.put("wachtwoord", wachtwoord);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void NieuwAccount() {
        butNieuwAccount = findViewById(R.id.butNieuwAccount);
        butNieuwAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), com.example.smartfarm_app.controller.NieuweGebruiker.class);
                startActivity(i);
            }
        });
    }

    private void ClearCredentials() {
        loginGebruiker = findViewById(R.id.loginGebruiker);
        loginWachtwoord = findViewById(R.id.loginWachtwoord);

        //clearen EditText gebruikersnaam met Double-Click
        loginGebruiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginGebruiker.setHint("");
                loginGebruiker.setTextColor(Color.BLACK);
            }
        });

        //clearen EditText wachtwoord met Double-Click
        loginWachtwoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWachtwoord.setHint("");
                loginWachtwoord.setTextColor(Color.BLACK);
            }
        });
    }

}