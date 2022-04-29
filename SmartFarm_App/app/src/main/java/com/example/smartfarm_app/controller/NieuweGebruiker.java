package com.example.smartfarm_app.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NieuweGebruiker extends AppCompatActivity {
    private String gebruikersNaam, wachtwoord, email, naam, telefoonnummer, bedrijfsnaam;
    private EditText etGebruikersnaam, etWachtwoord, etEmail, etNaam, etTelefoon, etBedrijf;
    private JSONObject nieuweGebruiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_gebruiker);

        //Declareren edittext
        etGebruikersnaam = findViewById(R.id.etGebruikersnaam);
        etWachtwoord = findViewById(R.id.etWachtwoord);
        etEmail = findViewById(R.id.etEmail);
        etNaam = findViewById(R.id.etNaam);
        etTelefoon = findViewById(R.id.etTelefoon);
        etBedrijf = findViewById(R.id.etBedrijf);

        ButRegistreer();
    }

    private void ButRegistreer() {
        //Declareren button
        Button butRegistreer = findViewById(R.id.butRegistreer);

        butRegistreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OphalenInvoer();
                //Prints kunnen later weggehaald worden.
                System.out.println("Invoer gebruikersnaam: " + gebruikersNaam);
                System.out.println("Invoer wachtwoord: " + wachtwoord);
                System.out.println("Invoer email: " + email);
                System.out.println("Invoer naam: " + naam);
                System.out.println("Invoer telefoonnummer: " + telefoonnummer);
                System.out.println("Invoer bedrijfsnaam: " + bedrijfsnaam);
            }
        });
    }

    private void OphalenInvoer() {
        //Ophalen invoer naar String
        gebruikersNaam = etGebruikersnaam.getText().toString();
        wachtwoord = etWachtwoord.getText().toString();
        email = etEmail.getText().toString();
        naam = etNaam.getText().toString();
        telefoonnummer = etTelefoon.getText().toString();
        bedrijfsnaam = etBedrijf.getText().toString();
        controlerenInvoer();
    }

    // functie controleert of de invoer leeg  is.
    private void controlerenInvoer() {
        if (gebruikersNaam.isEmpty()) {
            System.out.println("Ongeldige invoer");
            AlertDialog alertDialog = new AlertDialog.Builder(NieuweGebruiker.this).create();
            alertDialog.setTitle("Ongeldige gebruikersnaam");
            alertDialog.setMessage("Voer een gebruikersnaam in");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else if (wachtwoord.isEmpty()) {
            System.out.println("Ongeldige invoer");
            AlertDialog alertDialog = new AlertDialog.Builder(NieuweGebruiker.this).create();
            alertDialog.setTitle("Ongeldig wachtwoord");
            alertDialog.setMessage("Voer een wachtwoord in");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else if (email.isEmpty()) {
            System.out.println("Ongeldige invoer");
            AlertDialog alertDialog = new AlertDialog.Builder(NieuweGebruiker.this).create();
            alertDialog.setTitle("Ongeldig email");
            alertDialog.setMessage("Voer een email in");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            System.out.println("Geldige Invoer");
            AlertDialog alertDialog = new AlertDialog.Builder(NieuweGebruiker.this).create();
            alertDialog.setTitle("Controleer & bevestig de gegevens");
            alertDialog.setMessage("Gebruikersnaam  " + gebruikersNaam + "\nEmail adres  " + email + "\nNaam  " + naam + "\nTelefoon nummer  " + telefoonnummer + "\nBedrijfsnaam  " + bedrijfsnaam);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Hier insert naar Database maken
                            parseInputToJson();
                            volleyPost();
                            System.out.println(nieuweGebruiker);
                            //Statement om terug naar het homescreen te gaan
                            Intent i = new Intent(NieuweGebruiker.this, com.example.smartfarm_app.controller.MainActivity.class);
                            startActivity(i);
                        }
                    });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuleer",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    private void parseInputToJson() {
        nieuweGebruiker = new JSONObject();
        try {
            nieuweGebruiker.put("gebruikersNaam", gebruikersNaam);
            nieuweGebruiker.put("wachtwoord", wachtwoord);
            nieuweGebruiker.put("email", email);
            nieuweGebruiker.put("naam", naam);
            nieuweGebruiker.put("telefoonnummer", telefoonnummer);
            nieuweGebruiker.put("bedrijfsNaam", bedrijfsnaam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void volleyPost() {
        // String postUrl = "https://webhook.site/37076a97-84f8-49be-8b1d-fc7ddfea8e0c";
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/gebruikers/addnew";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, nieuweGebruiker, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError fout) {
                fout.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}

