package com.example.smartfarm_app.controller.ui.plant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartfarm_app.R;
import com.example.smartfarm_app.model.ApparaatLocatie;
import com.example.smartfarm_app.model.IdealeOmstandigheden;
import com.example.smartfarm_app.model.LocatieKas;

public class plantAanKastToevoegenFragment extends Fragment {
    private View root;
    public plantAanKastToevoegenFragment(IdealeOmstandigheden obj) { }

    public plantAanKastToevoegenFragment(LocatieKas obj) {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_plant_aan_kast_toevoegen, container, false);

        return root;
    }
}