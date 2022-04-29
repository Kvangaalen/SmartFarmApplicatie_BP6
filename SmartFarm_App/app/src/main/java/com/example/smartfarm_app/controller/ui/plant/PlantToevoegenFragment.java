package com.example.smartfarm_app.controller.ui.plant;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
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
import com.example.smartfarm_app.controller.utilitys.InputFilterMinMax;


import org.json.JSONException;
import org.json.JSONObject;

public class PlantToevoegenFragment extends Fragment {
    private View view;
    private String plantSoort, plantRas;
    private int idealeTemp, idealeLucht, idealeBodem, idealeLux;
    private JSONObject nieuwePlant;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_toevoegen, container, false);

      //Declareren knop & Edittexts
        Button butVoegToe = view.findViewById(R.id.butVoegToe);
        EditText etPlantsoort = view.findViewById(R.id.etPlantsoort);
        EditText etPlantras = view.findViewById(R.id.etPlantras);
        EditText etIdealeTemp = view.findViewById(R.id.etIdealeTemp);
        etIdealeTemp.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        EditText etIdealeLucht = view.findViewById(R.id.etIdealeLucht);
        etIdealeLucht.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        EditText etIdealeBodem = view.findViewById(R.id.etIdealeBodem);
        etIdealeBodem.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        EditText etIdealeLux = view.findViewById(R.id.etIdealeLux);
        etIdealeLux.setFilters(new InputFilter[]{new InputFilterMinMax("0", "150000")});

        // Actie van Button
        butVoegToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plantSoort = etPlantsoort.getText().toString();
                plantRas = etPlantras.getText().toString();


                idealeTemp = Integer.parseInt((etIdealeTemp.getText().toString()));
                idealeLucht = Integer.parseInt((etIdealeLucht.getText().toString()));
                idealeBodem = Integer.parseInt((etIdealeBodem.getText().toString()));
                idealeLux = Integer.parseInt((etIdealeLux.getText().toString()));

                System.out.println(plantSoort + " " + plantRas + " " + idealeTemp + " " + idealeLucht + " " + idealeBodem + " " + idealeLux);

               // Methode om input in JsonObject te zetten
                inputToJson();

                // Methode om JsonObject te versturen naar server
                volleyPost();
            }
        });
        return view;
    }

    private JSONObject inputToJson() {
        nieuwePlant = new JSONObject();
        try {
            nieuwePlant.put("plantras", plantRas);
            nieuwePlant.put("plantsoort", plantSoort);
            nieuwePlant.put("idealetempratuur", idealeTemp);
            nieuwePlant.put("idealeluchtvochtigheid", idealeLucht);
            nieuwePlant.put("idealebodemvochtigheid", idealeBodem);
            nieuwePlant.put("idealelux", idealeLux);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(nieuwePlant);
        return nieuwePlant;
    }

    private void volleyPost(){
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/newPlant";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, nieuwePlant, new Response.Listener<JSONObject>() {
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
        System.out.println("newPlant verzonden");
        Intent i = new Intent(view.getContext(), com.example.smartfarm_app.controller.UserMenu.class);
        startActivity(i);
    }

}

