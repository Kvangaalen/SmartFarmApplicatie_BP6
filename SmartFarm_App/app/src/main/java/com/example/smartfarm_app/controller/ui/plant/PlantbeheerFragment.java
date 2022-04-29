package com.example.smartfarm_app.controller.ui.plant;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;
import com.example.smartfarm_app.controller.ui.KasPlant.PlantAanKasToevoegenFragment;
import com.example.smartfarm_app.controller.utilitys.AdapterIdealeWaardes;
import com.example.smartfarm_app.controller.utilitys.AdapterKassen;
import com.example.smartfarm_app.model.IdealeOmstandigheden;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantbeheerFragment extends Fragment {
    private RecyclerView rvIdealeWaardes;
    private AdapterIdealeWaardes adapterIdealeWaardes;
    public View root;
    private FloatingActionButton actionButton;
    private AdapterKassen adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_plantbeheer, container, false);
        rvIdealeWaardes = (RecyclerView) root.findViewById(R.id.recyclerView);
        loadIdealeOmstandigheden();
        actionButton = root.findViewById(R.id.add_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Navigation.findNavController(root).navigate(R.id.action_nav_PlantBeheer_to_nav_plantToevoegen);
            }
        });
        return root;
    }

    private List loadIdealeOmstandigheden() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/idealeWaarde";
        List<IdealeOmstandigheden> idealeOmstandigheid = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response: " + response);
                        try {
                            JSONArray object = new JSONArray(response);
                            for (int i = 0; i < object.length(); i++) {
                                JSONObject object1 = object.getJSONObject(i);
                                String plantSoort = (String) object1.get("plantsoort");
                                String plantRas = (String) object1.get("plantras");
                                Integer idealeTempratuur = (int) object1.get("idealetempratuur");
                                Integer idealeLuchtvochtigheid = (int) object1.get("idealeluchtvochtigheid");
                                Integer idealeBodemvochtigheid = (int) object1.get("idealebodemvochtigheid");
                                Integer idealeLux = (int) object1.get("idealelux");
                                IdealeOmstandigheden io = new IdealeOmstandigheden(plantSoort, plantRas, idealeTempratuur, idealeLuchtvochtigheid, idealeBodemvochtigheid, idealeLux);
                                idealeOmstandigheid.add(io);
                            }
                            System.out.println("List Size: " + idealeOmstandigheid.size());
                            adapterIdealeWaardes = new AdapterIdealeWaardes(getContext(), idealeOmstandigheid);
                            rvIdealeWaardes.setAdapter(adapterIdealeWaardes);
                            rvIdealeWaardes.setLayoutManager(new LinearLayoutManager(getContext()));

                            adapterIdealeWaardes.setOnItemClickListener(new AdapterIdealeWaardes.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, IdealeOmstandigheden obj, int pos) {
                                    //do something
                                }
                            });

                            adapterIdealeWaardes.setOnMoreButtonClickListener(new AdapterIdealeWaardes.OnMoreButtonClickListener() {
                                @Override
                                public void onItemClick(View view, IdealeOmstandigheden io2, MenuItem item) {
                                    System.out.println(item.getTitle());
                                    if (item.getTitle().equals("Voeg aan kas toe")) {
                                        PlantAanKasToevoegenFragment contentMainFragment = new PlantAanKasToevoegenFragment();
                                        FragmentManager fragmentManager = getParentFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.nav_host_fragment_content_user_menu, new PlantAanKasToevoegenFragment(io2));
                                        fragmentTransaction.commit();
                                        // Navigation.findNavController(root).navigate(R.id.nav_PlantAanKasToevoegen);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response.isEmpty()) {
                            Toast.makeText(getContext(), "Ophalen data mislukt", Toast.LENGTH_LONG).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Volley error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return idealeOmstandigheid;
    }

}