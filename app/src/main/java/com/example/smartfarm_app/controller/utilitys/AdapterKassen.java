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
import com.example.smartfarm_app.model.LocatieKas;

import java.util.ArrayList;
import java.util.List;

public class AdapterKassen extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<LocatieKas> alLocatieKas;
    private Context ctx;
    private OnItemClickListener onItemClickListener;
    private AdapterKassen.OnMoreButtonClickListener onMoreButtonClickListener;

    public AdapterKassen(Context context, List<LocatieKas> alLocatieKas) {
        this.alLocatieKas = (ArrayList<LocatieKas>) alLocatieKas;
        ctx = context;
    }

    public void setOnItemClickListener(AdapterKassen.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreButtonClickListener(AdapterKassen.OnMoreButtonClickListener OnMoreButtonClickListener) {
        this.onMoreButtonClickListener = OnMoreButtonClickListener;
    }



    public interface OnItemClickListener {
        void onItemClick(View view, LocatieKas lk, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, LocatieKas lk, MenuItem item);
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, AdapterKassen ArrayList, int pos);
    }

    @Override
    public void onClick(View v) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView kasNaam, kasAdres;
        public ImageButton moreBtn;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            kasNaam = (TextView) v.findViewById(R.id.kasNaam);
            kasAdres = (TextView) v.findViewById(R.id.kasAdres);
            moreBtn = (ImageButton) v.findViewById(R.id.imageButtonKas);
            lyt_parent = (View) v.findViewById(R.id.mainLayout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kas_locatie_row, parent, false);
        vh = new ViewHolder(v);

        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof AdapterKassen.ViewHolder) {
            AdapterKassen.ViewHolder view = (AdapterKassen.ViewHolder) holder;
            final LocatieKas lk = alLocatieKas.get(position);
            view.kasNaam.setText(lk.getKasNaam());
            view.kasAdres.setText(lk.getStraatNaam() + "  " + lk.getHuisNummer() + "\n" + lk.getPostcode() + "  " + lk.getPlaats());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener == null) return;
                    onItemClickListener.onItemClick(v, lk, position);
                }
            });
// set onClick more Listener
            view.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view,lk);
                }
            });
        }
    }

    private void onMoreButtonClick(final View view, final LocatieKas lk) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, lk, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_adapter);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        int uniqueItemIdCount = alLocatieKas.size();
        return uniqueItemIdCount;
    }

}
