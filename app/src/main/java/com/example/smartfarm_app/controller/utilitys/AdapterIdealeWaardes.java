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
import com.example.smartfarm_app.model.IdealeOmstandigheden;

import java.util.ArrayList;
import java.util.List;

public class AdapterIdealeWaardes extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ArrayList<IdealeOmstandigheden> idealeOmstandighedens;
    private Context ctx;
    private AdapterIdealeWaardes.OnItemClickListener onItemClickListener;
    private AdapterIdealeWaardes.OnMoreButtonClickListener onMoreButtonClickListener;

    public AdapterIdealeWaardes(Context context, List<IdealeOmstandigheden> idealeOmstandighedens) {
        this.idealeOmstandighedens = (ArrayList<IdealeOmstandigheden>) idealeOmstandighedens;
        ctx = context;
    }

    public void setOnItemClickListener(AdapterIdealeWaardes.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMoreButtonClickListener(AdapterIdealeWaardes.OnMoreButtonClickListener OnMoreButtonClickListener) {
        this.onMoreButtonClickListener = OnMoreButtonClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, IdealeOmstandigheden ArrayList, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, IdealeOmstandigheden ArrayList, MenuItem item);
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, IdealeOmstandigheden ArrayList, int pos);
    }

    @Override
    public void onClick(View v) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView plantType, plantInfo1, plantInfo2;
        public ImageButton moreBtn;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            plantType = (TextView) v.findViewById(R.id.plantType);
            plantInfo1 = (TextView) v.findViewById(R.id.tempLux);
            plantInfo2 = (TextView) v.findViewById(R.id.bodemLucht);
            moreBtn = (ImageButton) v.findViewById(R.id.imageButton);
            lyt_parent = (View) v.findViewById(R.id.mainLayout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ideale_waardes_row, parent, false);
        vh = new ViewHolder(v);

        return vh;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof AdapterIdealeWaardes.ViewHolder) {
            AdapterIdealeWaardes.ViewHolder view = (AdapterIdealeWaardes.ViewHolder) holder;
            final IdealeOmstandigheden o = idealeOmstandighedens.get(position);
            view.plantType.setText(o.getPlantRas() + "   " + o.getPlantSoort());
            view.plantInfo1.setText("Tempratuur : " + o.getIdealeTempratuur() + "°C\n" + "Lichtsterkte : " + o.getIdealeLux() + " Lux");
            view.plantInfo2.setText("Bodemvochtigheid : " + o.getIdealeBodemvochtigheid() + "%\n" + "Luchtvochtigheid    : " + o.getIdealeLuchtvochtigheid() + "%");

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

    private void onMoreButtonClick(final View view, final IdealeOmstandigheden o) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, o, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_adapter_planttoevoegen);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        int uniqueItemIdCount = idealeOmstandighedens.size();
        return uniqueItemIdCount;
    }

}
