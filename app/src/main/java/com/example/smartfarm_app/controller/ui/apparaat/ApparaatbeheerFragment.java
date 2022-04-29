package com.example.smartfarm_app.controller.ui.apparaat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;

import com.example.smartfarm_app.controller.SaveSharedPreference;
import com.example.smartfarm_app.controller.utilitys.AdapterApparaat;
import com.example.smartfarm_app.model.ApparaatLocatie;
import com.example.smartfarm_app.model.KasPlant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApparaatbeheerFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterApparaat adapter;
    private View root;
    private FloatingActionButton fab;
    boolean test;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_apparaatbeheer, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        loadDataRecyleview();
        fab = root.findViewById(R.id.add_button);
        CheckGebruikerKas();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {

                if (test==false){
                    Toast.makeText(getContext(), "Voeg eerst 1 kast toe", Toast.LENGTH_LONG).show();
                } else {
                    Navigation.findNavController(root).navigate(R.id.action_nav_Apparaatbeheer_to_nav_ApparaatToevoegen);

                }

            }


        });

        return root;
    }

    private void CheckGebruikerKas() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/kassen";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            System.out.println(jsonArray.length());
                            if (jsonArray.length() > 0){
                                test= true;

                            } else {
                                test = false;
                            }


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


    }
    private List loadDataRecyleview() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/apparaat";
        List<ApparaatLocatie> apparaat = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray object = new JSONArray(response);
                            for (int i = 0; i < object.length(); i++) {
                                JSONObject object1 = object.getJSONObject(i);
                                String productid = (String) object1.get("productid");
                                String kasnaam = (String) object1.get("kasnaam");
                                String vaknummer = (String) object1.get("vaknummer");
                                System.out.println(productid);
                                ApparaatLocatie a = new ApparaatLocatie(productid, kasnaam, vaknummer);
                                apparaat.add(a);
                            }
                            System.out.println("Hoeveelheid apparaten op lijst: " + apparaat.size());
                            adapter = new AdapterApparaat(getContext(), apparaat);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            // item klik
                            adapter.setOnItemClickListener(new AdapterApparaat.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, ApparaatLocatie obj, int pos) {
                                    System.out.println(obj.getProductId());
                                }
                            });

                            adapter.setOnMoreButtonClickListener(new AdapterApparaat.OnMoreButtonClickListener() {
                                @Override
                                public void onItemClick(View view, ApparaatLocatie obj, MenuItem item) {
                                    if (item.getTitle().equals("Verwijderen")) {
                                        System.out.println("verwijderen");
                                        verwijderenApparaat(obj);

                                    } else if (item.getTitle().equals("Bewerken")) {
                                        bewerkenAparaatFragment fragment2 = new bewerkenAparaatFragment(obj);
                                        Navigation.findNavController(root).navigate(R.id.action_nav_Apparaatbeheer_to_nav_ApparaatToevoegen);
                                        FragmentManager fragmentManager = getParentFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, fragment2);
                                        fragmentTransaction.commit();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (apparaat.isEmpty()) {
                            Toast.makeText(getContext(), "U heeft nog geen apparaten", Toast.LENGTH_SHORT).show();
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
        return apparaat;
    }

    private void verwijderenApparaat(ApparaatLocatie obj) {

        System.out.println(obj.getProductId());

        String url = "https://aad-bp56-smartfarm.herokuapp.com/deleteApparaat";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), obj.getKasNaam(), Toast.LENGTH_LONG).show();
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
                params.put("productid", obj.getProductId());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}