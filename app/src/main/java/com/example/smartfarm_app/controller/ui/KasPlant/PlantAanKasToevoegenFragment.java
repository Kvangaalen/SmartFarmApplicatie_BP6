package com.example.smartfarm_app.controller.ui.KasPlant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarm_app.R;
import com.example.smartfarm_app.controller.SaveSharedPreference;
import com.example.smartfarm_app.model.IdealeOmstandigheden;
import com.example.smartfarm_app.model.KasPlant;
import com.example.smartfarm_app.model.LocatieKas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantAanKasToevoegenFragment extends Fragment {
    private View root;
    private KasPlant kp;
    private IdealeOmstandigheden io2;
    Spinner spinnerPlantSoort, spinnerPlantRas, spinnerKas;
    String selectedPlantSoort, tempSoort, tempRas, kasNaam, invoerKasNaam, plantSoort, plantRas;
    Integer vakNummer;

    EditText invoerVak;



    public PlantAanKasToevoegenFragment() {
    }

    public PlantAanKasToevoegenFragment(IdealeOmstandigheden io2) {
        this.io2 = io2;
        tempSoort = io2.getPlantSoort();
        tempRas = io2.getPlantRas();
    }

    public PlantAanKasToevoegenFragment(String kasNaam) {

        this.kasNaam = kasNaam;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_plant_aan_kas_toevoegen, container, false);

        System.out.println(" add plant to : " + kasNaam);

        Button addPlantToKas = root.findViewById(R.id.addPlantToKas);
        invoerVak = root.findViewById(R.id.invoerVaknummer);
        TextView headerKasNaam = root.findViewById(R.id.headerKasNaam);
        spinnerKas = root.findViewById(R.id.spinnerKas);

        if (kasNaam == null) {
            headerKasNaam.setVisibility(View.INVISIBLE);
            spinnerKas.setVisibility(View.VISIBLE);
            listKassen();
        } else {
            spinnerKas.setVisibility(View.INVISIBLE);
            headerKasNaam.setVisibility(View.VISIBLE);
            headerKasNaam.setText(kasNaam);
        }

        spinnerPlantSoort = root.findViewById(R.id.spinnerPlantSoort);

        listPlantSoorten();

        spinnerPlantRas = root.findViewById(R.id.spinnerPlantRas);
        spinnerPlantSoort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPlantSoort = spinnerPlantSoort.getSelectedItem().toString();
                System.out.println(selectedPlantSoort);
                listPlantRassen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                System.out.println("niks geselecteerd");
            }

        });

        addPlantToKas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ophalen kas naam
                if (kasNaam == null) {
                    invoerKasNaam = spinnerKas.getSelectedItem().toString();
                } else {
                    invoerKasNaam = headerKasNaam.getText().toString();
                }


                if (invoerVak.getText().toString().isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Ongeldig vaknummer");
                    alertDialog.setMessage("Voer een vaknummer in");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    String vaknummer = invoerVak.getText().toString();
                    vakNummer = Integer.parseInt(vaknummer);

                    //ophalen plantSoort
                    plantSoort = spinnerPlantSoort.getSelectedItem().toString();

                    //ophalen plantRas
                    plantRas = spinnerPlantRas.getSelectedItem().toString();

                    System.out.println("Invoer nieuwe plant in kas is : \n" + invoerKasNaam + "\n" + vakNummer + "\n" + plantSoort + "\n" + plantRas);
                }

                //

               // inputToJson();
                volleyPost();

            }

        });

        return root;
    }

    private void volleyPost() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/plantToKas";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        boolean b = Boolean.parseBoolean(response);
                        if (!b){

                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Ongeldige invoer");
                            alertDialog.setMessage("Deze combinatie bestaat al. ");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else {
                            Toast.makeText(getContext(), plantRas + " van " + plantSoort + " is toegevoegd aan vaknummer " + vakNummer + " van " + kasNaam, Toast.LENGTH_LONG).show();
                           // int increment = vakNummer + 1;
                            invoerVak.setText(String.valueOf(vakNummer+ 1));
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
                params.put("kasnaam", kasNaam);
                params.put("plantsoort", plantSoort);
                params.put("plantras", plantRas);
                params.put("vaknummer", String.valueOf(vakNummer));
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }



    private List listKassen() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/kassen";
        List<LocatieKas> locatieKasList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArrayKas = new JSONArray(response);
                    for (int i = 0; i < jsonArrayKas.length(); i++) {
                        JSONObject kas = jsonArrayKas.getJSONObject(i);
                        String kasNaam = (String) kas.get("kasnaam");
                        LocatieKas lk = new LocatieKas(null, kasNaam, null, null, null, null);
                        locatieKasList.add(lk);
                    }
                    loadKasSpinner(locatieKasList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
        return locatieKasList;
    }


    private List listPlantRassen() {

        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/spinnerfiller";
        List<IdealeOmstandigheden> idealeOmstandighedenList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArrayPlantRassen = new JSONArray(response);
                    for (int i = 0; i < jsonArrayPlantRassen.length(); i++) {
                        JSONObject object = jsonArrayPlantRassen.getJSONObject(i);
                        String plantRas = (String) object.get("plantras");
                        IdealeOmstandigheden io = new IdealeOmstandigheden(null, plantRas, null, null, null, null);
                        idealeOmstandighedenList.add(io);
                    }
                    ArrayList<String> ras = new ArrayList<String>();
                    for (IdealeOmstandigheden element : idealeOmstandighedenList) {
                        if (!ras.contains(element.getPlantRas())) {
                            ras.add(element.getPlantRas());
                        }
                    }
                    loadSpinnerPlantRas(ras);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Volley Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gebruikersNaam", SaveSharedPreference.getUserName(getContext()));
                params.put("selectedplantsoort", selectedPlantSoort);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return idealeOmstandighedenList;
    }

    private List listPlantSoorten() {
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/idealeWaarde";
        List<IdealeOmstandigheden> idealeOmstandighedenList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArrayPlantSoorten = new JSONArray(response);
                    for (int i = 0; i < jsonArrayPlantSoorten.length(); i++) {
                        JSONObject object = jsonArrayPlantSoorten.getJSONObject(i);
                        String plantSoort = (String) object.get("plantsoort");
                        String plantRas = (String) object.get("plantras");

                        IdealeOmstandigheden io = new IdealeOmstandigheden(plantSoort, plantRas, null, null, null, null);
                        idealeOmstandighedenList.add(io);
                    }

                    ArrayList<String> soort = new ArrayList<String>();
                    ArrayList<String> ras = new ArrayList<String>();
                    for (IdealeOmstandigheden element : idealeOmstandighedenList) {
                        if (!soort.contains(element.getPlantSoort())) {
                            soort.add(element.getPlantSoort());
                        }
                    }
                    loadSpinnerPlantSoort(soort);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        return idealeOmstandighedenList;
    }

    private void loadSpinnerPlantSoort(List<String> idealeOmstandighedenList) {
        ArrayList<String> idealeOmstandighedenArrayList = (ArrayList<String>) idealeOmstandighedenList;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, idealeOmstandighedenArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantSoort.setAdapter(adapter);
        if (tempSoort != null) {
            int spinnerPosition = adapter.getPosition(tempSoort);
            spinnerPlantSoort.setSelection(spinnerPosition);
        }
    }

    private void loadSpinnerPlantRas(List<String> idealeOmstandighedenList) {
        ArrayList<String> idealeOmstandighedenArrayList = (ArrayList<String>) idealeOmstandighedenList;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, idealeOmstandighedenArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantRas.setAdapter(adapter);
        if (tempRas != null) {
            int spinnerPosition = adapter.getPosition(tempRas);
            spinnerPlantRas.setSelection(spinnerPosition);
        }
    }

    private void loadKasSpinner(List<LocatieKas> locatieKasList) {
        ArrayList<LocatieKas> locatieKasArrayList = (ArrayList<LocatieKas>) locatieKasList;
        ArrayAdapter<LocatieKas> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, locatieKasArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKas.setAdapter(adapter);
    }

    // standaard waarde instellen
    private int getIndex(Spinner spinner, String plantSoort) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinnerPlantSoort.getItemAtPosition(i).toString().equalsIgnoreCase(plantSoort)) {
                return i;
            }
        }
        return 0;
    }
}