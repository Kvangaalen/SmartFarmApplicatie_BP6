package com.example.smartfarm_app.controller.ui.apparaat;


import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToevoegenAparaatFragment extends Fragment {
    private View view;
    private Spinner spinnerKaasnaam;
    private Button btnVoegToe;
    private String vaknummer, productId;
    private EditText etVaknummer, etProductid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toevoegen_aparaat, container, false);
        btnVoegToe = view.findViewById(R.id.butVoegToe);
        spinnerKaasnaam = view.findViewById(R.id.spinnerKasnaam);
        getKassen();

        btnVoegToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addApparaat();
            }
        });

        return view;
    }

    private void addApparaat() {
        LocatieKas lk = (LocatieKas) spinnerKaasnaam.getSelectedItem();
        etProductid = view.findViewById(R.id.etProductid);
        etVaknummer = view.findViewById(R.id.etVaknummer);
        productId = etProductid.getText().toString();
        vaknummer = etVaknummer.getText().toString();
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/newApparaat";
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
                params.put("vaknummer", vaknummer);
                params.put("productid", "urn:dev:DEVEUI:" + productId + ":");
                params.put("kasnaam", lk.getKasNaam());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void loadSpinnerProducten(List<LocatieKas> locatieKas) {
        ArrayList<LocatieKas> lk = (ArrayList<LocatieKas>) locatieKas;
        ArrayAdapter<LocatieKas> adapter = new ArrayAdapter<LocatieKas>(getContext(), android.R.layout.simple_spinner_dropdown_item, lk);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKaasnaam.setAdapter(adapter);
        spinnerKaasnaam.setSelection(0);

        //  spinnerKaasnaam.setSelection(getIndex(spinnerKaasnaam, this.eenheid));
    }

    // standaard waarde instellen
    private int getIndex(Spinner spinner, String productgroep) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(productgroep)) {
                return i;
            }
        }
        return 0;
    }

    private List getKassen() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/kassen";
        List<LocatieKas> locatieKas = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray object = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String kasNaam = (String) jsonObject.get("kasnaam");
                                //String gebruiker = (String) jsonObject.get("gebruikersnaam");
                                String straat = (String) jsonObject.get("straatnaam");
                                String huisnummer = (String) jsonObject.get("huisnummer");
                                String plaats = (String) jsonObject.get("plaats");
                                String postcode = (String) jsonObject.get("postcode");
                                LocatieKas lk = new LocatieKas(null, kasNaam, straat, huisnummer, plaats, postcode);
                                System.out.println(lk.getKasNaam());
                                System.out.println(lk.getPlaats());
                                System.out.println(lk.getHuisNummer());
                                System.out.println(lk.getPlaats());
                                System.out.println(lk.getPostcode());
                                locatieKas.add(lk);
                            }
                            loadSpinnerProducten(locatieKas);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return locatieKas;
    }

}