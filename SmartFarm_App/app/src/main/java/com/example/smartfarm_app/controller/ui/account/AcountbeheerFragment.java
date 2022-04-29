package com.example.smartfarm_app.controller.ui.account;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.smartfarm_app.controller.SaveSharedPreference;
import com.example.smartfarm_app.model.GebruikersInformatie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcountbeheerFragment extends Fragment {
    private View root;
    private String gebruikersnaam, naam, email, telefoonnummer, bedrijfsnaam;
    private Button butBewerken;
    private EditText etGebruikersnaam, etNaam, etEmail, etTelefoon, etBedrijf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acountbeheer, container, false);
        butBewerken = root.findViewById(R.id.butBewerken);
        etGebruikersnaam = root.findViewById(R.id.etGebruikersnaam);
        etNaam = root.findViewById(R.id.etNaam);
        etEmail = root.findViewById(R.id.etEmailadres);
        etTelefoon = root.findViewById(R.id.etTelefoon);
        etBedrijf = root.findViewById(R.id.etBedrijf);
        etGebruikersnaam.setEnabled(false);

        System.out.println(getGebruikergevens());
        butBewerken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGebruiker();
            }
        });
        return root;
    }

    private List<GebruikersInformatie> getGebruikergevens() {
        this.gebruikersnaam = SaveSharedPreference.getUserName(getContext());
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/getGebruiker";
        List<GebruikersInformatie> gebruikers = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String gebruikersNaam = (String) object.get("gebruikersNaam");
                            String naamEigenaar = (String) object.get("naamEigenaar");
                            String emailAdres = (String) object.get("emailAdres");
                            String telefoonEigenaar = (String) object.get("telefoonEigenaar");
                            String bedrijfsNaam = (String) object.get("bedrijfsNaam");
                            GebruikersInformatie g = new GebruikersInformatie(gebruikersNaam, naamEigenaar, emailAdres, telefoonEigenaar, null, bedrijfsNaam);
                            gebruikers.add(g);
                            etGebruikersnaam.setText(gebruikersNaam);
                            etNaam.setText(naamEigenaar);
                            etEmail.setText(emailAdres);
                            etTelefoon.setText(telefoonEigenaar);
                            etBedrijf.setText(bedrijfsNaam);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Volley Error", Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return gebruikers;
    }

    private void editGebruiker() {

        gebruikersnaam = etGebruikersnaam.getText().toString();
        naam = etNaam.getText().toString();
        email = etEmail.getText().toString();
        telefoonnummer = etTelefoon.getText().toString();
        bedrijfsnaam = etBedrijf.getText().toString();

        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/editGebruiker";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "@String/MSG_Passed_addApparaat", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "VolleyError", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                params.put("naam", naam);
                params.put("email", email);
                params.put("telefoonnummer", telefoonnummer);
                params.put("bedrijfsNaam", bedrijfsnaam);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}

