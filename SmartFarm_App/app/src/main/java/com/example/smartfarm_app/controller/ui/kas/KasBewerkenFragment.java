package com.example.smartfarm_app.controller.ui.kas;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


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
import com.example.smartfarm_app.model.LocatieKas;

import java.util.HashMap;
import java.util.Map;


public class KasBewerkenFragment extends Fragment {
    private View root;
    EditText etKasNaam, etStraatNaam, etHuisnummer, etPlaatsNaam, etPostcode;
    String kasNaam;
    String straatNaam;
    String huisNummer;
    String plaatsNaam;
    String postcode;
    LocatieKas lk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_kas_bewerken, container, false);

        etKasNaam = (EditText) root.findViewById(R.id.etKasNaamBewerken);
        etKasNaam.setText(kasNaam);

        etStraatNaam = root.findViewById(R.id.etStraat);
        etStraatNaam.setText(straatNaam);
        etStraatNaam.setTextColor(Color.GRAY);

        etHuisnummer = root.findViewById(R.id.etHuisnummer);
        etHuisnummer.setText(huisNummer);
        etHuisnummer.setTextColor(Color.GRAY);

        etPlaatsNaam = root.findViewById(R.id.etPlaats);
        etPlaatsNaam.setText(plaatsNaam);
        etPlaatsNaam.setTextColor(Color.GRAY);

        etPostcode = root.findViewById(R.id.etPostcode);
        etPostcode.setText(postcode);
        etPostcode.setTextColor(Color.GRAY);
        
        Button butUpdate = root.findViewById(R.id.updateKas);
        
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKasVolley(lk);
                Navigation.findNavController(root).navigate(R.id.action_nav_KasBewerken_to_nav_Kasbeheer);
            }
        });
                

        return root;
    }

    private void updateKasVolley(LocatieKas lk) {
        System.out.println("huidige kasnaam : " + kasNaam);
        kasNaam = etKasNaam.getText().toString();
        System.out.println("Nieuwe kasnaam : " + kasNaam);
        lk.setKasNaam(kasNaam);

        String url = "https://aad-bp56-smartfarm.herokuapp.com/updateKas";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), lk.getKasNaam() + " aangepast", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "VolleyError", Toast.LENGTH_LONG).show();
                    }
    }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kasnaam", lk.getKasNaam());
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                params.put("straatnaam", lk.getStraatNaam());
                params.put("huisnummer", lk.getHuisNummer());
                params.put("postcode", lk.getPostcode());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
       // Navigation.findNavController(root).navigate(R.id.action_nav_Kasbeheer_self);
    }



    public KasBewerkenFragment(LocatieKas lk){
        kasNaam = lk.getKasNaam();
        straatNaam = lk.getStraatNaam();
       huisNummer = lk.getHuisNummer();
        plaatsNaam = lk.getPlaats();
        postcode = lk.getPostcode();
        this.lk = lk;

    }

    public KasBewerkenFragment() {
        // Required empty public constructor
    }
}