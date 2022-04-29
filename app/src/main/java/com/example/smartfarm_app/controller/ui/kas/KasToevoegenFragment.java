package com.example.smartfarm_app.controller.ui.kas;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;
import com.example.smartfarm_app.controller.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class KasToevoegenFragment extends Fragment {
    private View view;
    private String kasnaam, gebruikersnaam, straatnaam, huisnummer, plaats, postcode;
    private JSONObject nieuweKas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_kas_toevoegen, container, false);

        //Declareren EditTexts & Button
        Button addKas = view.findViewById(R.id.addKas);
        EditText etKasNaam = view.findViewById(R.id.etKasNaam);

        EditText etStraatNaam = view.findViewById(R.id.etStraat);
        EditText etHuisNummer = view.findViewById(R.id.etHuisnummer);
        EditText etPlaats = view.findViewById(R.id.etPlaats);
        EditText etPostcode = view.findViewById(R.id.etPostcode);

        //Actie Button
        addKas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kasnaam = etKasNaam.getText().toString();
                gebruikersnaam = SaveSharedPreference.getUserName(getContext());
                straatnaam = etStraatNaam.getText().toString();
                huisnummer = etHuisNummer.getText().toString();
                plaats = etPlaats.getText().toString();
                postcode = etPostcode.getText().toString();

                inputToJson();
                volleyPost();
            }
        });


        return view;
    }

    private JSONObject inputToJson() {
        nieuweKas = new JSONObject();
        try {
            nieuweKas.put("kasnaam", kasnaam);
            nieuweKas.put("gebruikersnaam", gebruikersnaam);
            nieuweKas.put("straatnaam", straatnaam);
            nieuweKas.put("huisnummer", huisnummer);
            nieuweKas.put("plaats", plaats);
            nieuweKas.put("postcode", postcode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(nieuweKas);
        return nieuweKas;
    }

    private void volleyPost(){
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/newKas";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, nieuweKas, new Response.Listener<JSONObject>() {
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
        System.out.println("newKas verzonden");
        Navigation.findNavController(view).navigate(R.id.action_nav_kasToevoegen_to_nav_Kasbeheer);
    }
}

