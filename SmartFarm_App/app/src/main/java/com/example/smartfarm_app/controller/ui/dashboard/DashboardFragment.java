package com.example.smartfarm_app.controller.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.smartfarm_app.model.ApparaatLocatie;
import com.example.smartfarm_app.model.Gemetenverschilwaarde;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashboardFragment extends Fragment {
    private View root;
    public Spinner spinnerProductId, spinnerCategorie;
    private String productId, Categorie;
    GraphView graph;

    ArrayList<Gemetenverschilwaarde> listNews = new ArrayList<Gemetenverschilwaarde>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        spinnerProductId = root.findViewById(R.id.spinnerProductId);
        spinnerCategorie = root.findViewById(R.id.spinnerKlimaat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categorie_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);
        getApparaat();
        spinnerProductId.setAdapter(null);

        graph = (GraphView) root.findViewById(R.id.graph);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    return formatter.format(value);
                }
                return super.formatLabel(value, isValueX);
            }
        });
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Datum & tijd");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Waarde");


        spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getMeetwaard();
                listNews.clear();
                graph.removeAllSeries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return root;
    }

    private void getMeetwaard() {
        try {
            productId = spinnerProductId.getSelectedItem().toString();

        } catch (NullPointerException exception) {
            return;
        }
        Categorie = spinnerCategorie.getSelectedItem().toString();
        String postUrl = "https://aad-bp56-smartfarm.herokuapp.com/gemetenverschikwaardes";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray object = new JSONArray(response);
                            for (int i = 0; i < object.length(); i++) {
                                JSONObject object1 = object.getJSONObject(i);
                                String productid = (String) object1.get("productid");
                                String datum = (String) object1.get("datum");
                                String tijdstip = (String) object1.get("tijdstip");
                                int gemetentempratuur = (int) object1.get("gemetentempratuur");
                                int gemetenluchtvochtigheid = (int) object1.get("gemetenluchtvochtigheid");
                                int gemetenbodemvochtigheid = (int) object1.get("gemetenbodemvochtigheid");
                                int gemetenlux = (int) object1.get("gemetenlux");
                                Gemetenverschilwaarde gvw = new Gemetenverschilwaarde(productid, datum, tijdstip, gemetentempratuur, gemetenluchtvochtigheid, gemetenbodemvochtigheid, gemetenlux);
                                listNews.add(gvw);

                            }
                            fillGraphView(listNews, Categorie);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                        if (listNews.isEmpty()) {
                            Toast.makeText(getContext(), "U heeft nog geen gemeten waardes", Toast.LENGTH_SHORT).show();
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
                params.put("productId", productId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void fillGraphView(ArrayList<Gemetenverschilwaarde> listNews, String categorie) throws ParseException {
        System.out.println(categorie);

        DataPoint[] dataPoints = new DataPoint[listNews.size()];
        for (int i = 0; i < listNews.size(); i++) {
            String datum = listNews.get(i).getDatum();
            String tijd = listNews.get(i).getTijdstip();
            Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(datum + " " + tijd);

            switch (categorie) {
                case "gemetentempratuur":
                    dataPoints[i] = new DataPoint(date1, listNews.get(i).getGemetentempratuur());
                    graph.setTitle("gemetentempratuur");
                    break;
                case "gemetenluchtvochtigheid":
                    dataPoints[i] = new DataPoint(date1, listNews.get(i).getGemetenluchtvochtigheid());
                    graph.setTitle("gemetenluchtvochtigheid");
                    break;
                case "gemetenbodemvochtigheid":
                    dataPoints[i] = new DataPoint(date1, listNews.get(i).getGemetenbodemvochtigheid());
                    graph.setTitle("gemetenbodemvochtigheid");
                    break;
                case "gemetenlux":
                    dataPoints[i] = new DataPoint(date1, listNews.get(i).getGemetenlux());
                    graph.setTitle("gemetenlux");
                    break;
            }

        }


        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getViewport().setMinX(series.getLowestValueX());
        graph.getViewport().setMaxX(series.getLowestValueX() + 5 * 24 * 60 * 60 * 1000); // + 3 days


        //graph.setCursorMode(true);

        graph.getViewport().setScalable(true);
        series.setDrawDataPoints(true);
        series.setTitle(categorie);
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.setCursorMode(true);
            }
        });

        series.setDrawBackground(true);
        series.setThickness(8);
        graph.addSeries(series);

    }

    private List getApparaat() {
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
                                ApparaatLocatie a = new ApparaatLocatie(productid, kasnaam, vaknummer);
                                apparaat.add(a);
                            }
                            loadSpinnerProductId(apparaat);
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

    private void loadSpinnerProductId(List<ApparaatLocatie> apparaat) {
        ArrayList<ApparaatLocatie> lk = (ArrayList<ApparaatLocatie>) apparaat;
        ArrayAdapter<ApparaatLocatie> adapter = new ArrayAdapter<ApparaatLocatie>(getContext(), android.R.layout.simple_spinner_dropdown_item, lk);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductId.setAdapter(adapter);
    }

}