package com.example.smartfarm_app.controller.ui.kas;

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
import com.example.smartfarm_app.controller.ui.KasPlant.OverzichtGewassenFragment;
import com.example.smartfarm_app.controller.utilitys.AdapterKassen;
import com.example.smartfarm_app.model.LocatieKas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KasbeheerFragment extends Fragment {
    private RecyclerView rvKassen;
    private AdapterKassen adapterKassen;
    private View root;
    private FloatingActionButton actionButton;

    public KasbeheerFragment(LocatieKas lk) {

    }

    public KasbeheerFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_kasbeheer, container, false);
        rvKassen = (RecyclerView) root.findViewById(R.id.rvKassen);
        loadKassen();

        actionButton = root.findViewById(R.id.add_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_Kasbeheer_to_nav_kasToevoegen);
            }
        });


        return root;
    }

    public  List loadKassen() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/kassen";
        List<LocatieKas> locatieKas = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String kasNaam = (String) jsonObject.get("kasnaam");
                                String gebruikersNaam = SaveSharedPreference.getUserName(getContext());
                                String straat = (String) jsonObject.get("straatnaam");
                                String huisnummer = (String) jsonObject.get("huisnummer");
                                String plaats = (String) jsonObject.get("plaats");
                                String postcode = (String) jsonObject.get("postcode");

                                LocatieKas lk = new LocatieKas(gebruikersNaam, kasNaam, straat, huisnummer, plaats, postcode);
                                locatieKas.add(lk);
                            }
                            System.out.println("List Size Kassen : " + locatieKas.size());
                            adapterKassen = new AdapterKassen(getContext(), locatieKas);
                            rvKassen.setAdapter(adapterKassen);
                            rvKassen.setLayoutManager(new LinearLayoutManager(getContext()));

                            adapterKassen.setOnItemClickListener(new AdapterKassen.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, LocatieKas lk, int pos) {
                                    String item = lk.getKasNaam();
                                    System.out.println("item gedrukt : " + item);

                                    overzichtGewassen(lk);
                                    closeFragment();
                                }
                            });

                            adapterKassen.setOnMoreButtonClickListener(new AdapterKassen.OnMoreButtonClickListener() {
                                @Override
                                public void onItemClick(View view, LocatieKas lk, MenuItem item) {
                                    if (item.getTitle().equals("Verwijderen")) {
                                        System.out.println("Kas wordt verwijderd");
                                        kasVerwijderen(lk);
                                    } else if (item.getTitle().equals("Bewerken")) {
                                        System.out.println("Kas wordt bewerkt");
                                        kasBewerken(lk);
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
                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return locatieKas;
    }

    private void closeFragment() {
        getActivity().getFragmentManager().popBackStack();
    }


    private void overzichtGewassen(LocatieKas lk) {
        System.out.println("-----------------------------------");
        System.out.println("test300$" + lk.getKasNaam());
        OverzichtGewassenFragment overzichtGewassenFragment = new OverzichtGewassenFragment(lk);
        //Navigation.findNavController(root).navigate(R.id.action_nav_Kasbeheer_to_nav_OverzichtGewassen);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, overzichtGewassenFragment);
        fragmentTransaction.commit();

    }

    private void kasBewerken(LocatieKas lk) {
        System.out.println("Dit object wordt bewerkt : " + lk.getKasNaam() + " " + lk.getGebruikersNaam());
        KasBewerkenFragment kasBewerkenFragment = new KasBewerkenFragment(lk);
        Navigation.findNavController(root).navigate(R.id.action_nav_Kasbeheer_to_nav_KasBewerken);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, kasBewerkenFragment);
        fragmentTransaction.commit();
    }

    private void kasVerwijderen(LocatieKas lk) {
        System.out.println("dit object wordt verwijderd : " + lk.getKasNaam() + " " + lk.getGebruikersNaam());

        String url = "https://aad-bp56-smartfarm.herokuapp.com/deleteKas";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), lk.getKasNaam() + " verwijderd", Toast.LENGTH_SHORT).show();
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
                params.put("kasnaam", lk.getKasNaam());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        Navigation.findNavController(root).navigate(R.id.action_nav_Kasbeheer_self);
    }

}