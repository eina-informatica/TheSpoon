package es.unizar.eina.thespoon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.thespoon.R;

class PlatoAddViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
    final CheckBox mPlatoCheckbox;
    private PlatoAddViewHolder(View itemView) {
        super(itemView);
        mPlatoCheckbox = itemView.findViewById(R.id.checkbox);
    }

    public void bind(String text) {
        mPlatoCheckbox.setText(text);
    }

    static PlatoAddViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_plato_add, parent, false);
        return new PlatoAddViewHolder(view);
    }
}
