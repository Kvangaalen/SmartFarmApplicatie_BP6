package com.example.smartfarm_app.controller.ui.KasPlant;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;
import com.example.smartfarm_app.controller.SaveSharedPreference;
import com.example.smartfarm_app.controller.ui.kas.KasbeheerFragment;
import com.example.smartfarm_app.controller.utilitys.AdapterKasPlanten;
import com.example.smartfarm_app.model.KasPlant;
import com.example.smartfarm_app.model.LocatieKas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverzichtGewassenFragment extends Fragment {
    View view;
    String kasNaam, gebruikersNaam;
    int vakNummer;
    LocatieKas lk;
    KasPlant kp;
    RecyclerView rvKasPlant;
    private AdapterKasPlanten adapterKasPlanten;
    FloatingActionButton actionButtonKasPlant;

    public OverzichtGewassenFragment(){ }

    public OverzichtGewassenFragment(LocatieKas lk) {
        kasNaam = lk.getKasNaam();
        gebruikersNaam = lk.getGebruikersNaam();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overzicht_gewassen, container, false);

        TextView tvKasNaam = view.findViewById(R.id.tvKasNaam);

        tvKasNaam.setText(kasNaam);

        rvKasPlant = (RecyclerView) view.findViewById(R.id.rvKasPlant);
        loadDataKasPlant();

        actionButtonKasPlant = view.findViewById(R.id.add_plantToKas_button);
        actionButtonKasPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantAanKasToevoegen(kasNaam);

            }
        });

        return view;

    }

    private void plantAanKasToevoegen(String kasNaam) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, new PlantAanKasToevoegenFragment(kasNaam));
        fragmentTransaction.commit();
    }

    private List loadDataKasPlant() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/getKasPlant";
        List <KasPlant> kasPlants = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int plantId = (Integer) jsonObject.get("plantid");
                        String plantSoort = (String) jsonObject.get("plantsoort");
                        String plantRas = (String) jsonObject.get("plantras");
                        vakNummer = (Integer) jsonObject.get("vaknummer");

                        KasPlant kasPlant = new KasPlant(plantId, kasNaam, plantSoort, plantRas, vakNummer, gebruikersNaam);
                        kasPlants.add(kasPlant);
                        kp = kasPlant;
                    }
                    System.out.println("ListSize kas planten : " + kasPlants.size());
                    adapterKasPlanten = new AdapterKasPlanten(getContext(), kasPlants);
                    rvKasPlant.setAdapter(adapterKasPlanten);
                    rvKasPlant.setLayoutManager(new LinearLayoutManager(getContext()));

                    adapterKasPlanten.setOnMoreButtonClickListener(new AdapterKasPlanten.OnMoreButtonClickListener() {
                        @Override
                        public void onItemClick(View view, KasPlant selectedKp, MenuItem item) {
                            if (item.getTitle().equals("Verwijderen")){

                                System.out.println("vak " + selectedKp.getVakNummer() + " van " + selectedKp.getKasNaam() + " wordt leeg gemaakt.");

                                vakLeegmaken(selectedKp);
                            }
                        }
                    });

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
                params.put("kasnaam", kasNaam);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return kasPlants;
    }

    private void vakLeegmaken(KasPlant selectedKp) {
        String url = "https://aad-bp56-smartfarm.herokuapp.com/deleteKasPlant";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("verwijderen kasPlant gelukt.");

                LocatieKas locKas = new LocatieKas(SaveSharedPreference.getUserName(getContext()),kasNaam,null,null,null,null);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, new OverzichtGewassenFragment(locKas));
                fragmentTransaction.commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "VolleyError", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                params.put("kasnaam", selectedKp.getKasNaam());
                params.put("vaknummer", String.valueOf(selectedKp.getVakNummer()));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }


}