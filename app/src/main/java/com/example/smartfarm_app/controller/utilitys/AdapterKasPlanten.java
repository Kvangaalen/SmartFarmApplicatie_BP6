package com.example.smartfarm_app.controller.utilitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarm_app.R;
import com.example.smartfarm_app.model.KasPlant;
import com.example.smartfarm_app.model.LocatieKas;

import java.util.ArrayList;
import java.util.List;

public class AdapterKasPlanten extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<KasPlant> kasPlants;
    private Context ctx;
    private OnItemClickListener onItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public AdapterKasPlanten(Context context, List<KasPlant> kasPlants) {
        this.kasPlants = (ArrayList<KasPlant>) kasPlants;
        ctx = context;
    }

    public void setOnItemClickListener(AdapterKasPlanten.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreButtonClickListener(AdapterKasPlanten.OnMoreButtonClickListener onMoreButtonClickListener){
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, KasPlant kasPlants, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, KasPlant kasPlants, MenuItem item);
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, AdapterKasPlanten ArrayList, int pos);
    }

    @Override
    public void onClick(View v) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vakNummer, plantSoort, plantRas;
        public ImageButton moreBtn;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            vakNummer = (TextView) v.findViewById(R.id.vakNummerRow);
            plantSoort = (TextView) v.findViewById(R.id.plantSoort);
            plantRas = (TextView) v.findViewById(R.id.plantRas);
            moreBtn = (ImageButton) v.findViewById(R.id.imageButtonKasPlant);
            lyt_parent = (View) v.findViewById(R.id.mainLayout);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kas_plant_row, parent, false);
        //vh = new AdapterKasPlanten().ViewHolder(v);
        vh = new ViewHolder(v);

        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof AdapterKasPlanten.ViewHolder) {
            AdapterKasPlanten.ViewHolder view = (AdapterKasPlanten.ViewHolder) holder;
            final KasPlant kp = kasPlants.get(position);
            String strVaknummer = kp.getVakNummer().toString();
            view.vakNummer.setText(strVaknummer);
            view.plantSoort.setText(kp.getPlantSoort());
            view.plantRas.setText(kp.getPlantRas());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener == null) return;
                    onItemClickListener.onItemClick(v, kp, position);
                }
            });
// set onClick more Listener
            view.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view,kp);
                }
            });
        }
    }
    private void onMoreButtonClick(final View view, final KasPlant kasPlant) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, kasPlant, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_adapter_vakleegmaken);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        int uniqueItemIdCount = kasPlants.size();
        return uniqueItemIdCount;
    }
}
