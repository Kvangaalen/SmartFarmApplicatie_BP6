package com.example.smartfarm_app.controller.utilitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import com.example.smartfarm_app.R;
import com.example.smartfarm_app.model.ApparaatLocatie;

import java.util.ArrayList;
import java.util.List;

public class AdapterApparaat extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<ApparaatLocatie> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener onItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;
    private AdapterApparaat.OnItemLongClickListener OnItemLongClickListener;

    public AdapterApparaat(Context context, List<ApparaatLocatie> items) {
        this.items = (ArrayList<ApparaatLocatie>) items;
        ctx = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreButtonClickListener(OnMoreButtonClickListener OnMoreButtonClickListener) {
        this.onMoreButtonClickListener = OnMoreButtonClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ApparaatLocatie ArrayList, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ApparaatLocatie ArrayList, MenuItem item);
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, ApparaatLocatie ArrayList, int pos);
    }

    @Override
    public void onClick(View v) { }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView kasnaam, productid, vaknummer;
        public ImageButton moreBtn;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            kasnaam = (TextView) v.findViewById(R.id.kasnaam);
            productid = (TextView) v.findViewById(R.id.productid);
            vaknummer = (TextView) v.findViewById(R.id.vaknummer);
            moreBtn = (ImageButton) v.findViewById(R.id.imageButton);
            lyt_parent = (View) v.findViewById(R.id.mainLayout);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.apparaat_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // aangeroepen door RecyclerView .....?
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final ApparaatLocatie o = items.get(position);
            view.kasnaam.setText(o.getProductId());
            view.productid.setText(o.getKasNaam());
            view.vaknummer.setText(o.getVakNummer());

            // set OnClick Listener
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) return;
                    onItemClickListener.onItemClick(view, o, position);
                }
            });

            // set LongClick Listener
            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });

            // set onClick more Listener
            view.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, o);
                }
            });

        }

    }

    // nodig voor aanmaken van view "AdapterProducts(getContext(), getProducts(this));", in overviewProductFragment.
    @Override
    public int getItemCount() {
        int uniqueItemIdCount = items.size();
        return uniqueItemIdCount;
    }

    private void onMoreButtonClick(final View view, final ApparaatLocatie product) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, product, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_adapter);
        popupMenu.show();
    }


}
