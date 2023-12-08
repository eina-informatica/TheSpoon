package es.unizar.eina.thespoon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.thespoon.R;

class PlatoAddViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
    final TextView mPlatoTextview;
    final EditText mPlatoEditText; // Cantidad del plato para ese pedido
    private PlatoAddViewHolder(View itemView) {
        super(itemView);
        mPlatoTextview = itemView.findViewById(R.id.textView);
        mPlatoEditText = itemView.findViewById(R.id.editText);
    }

    public void bind(String nombre, int cantidad) {

        mPlatoTextview.setText(nombre);
        mPlatoEditText.setText(String.valueOf(cantidad));
    }

    static PlatoAddViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_plato_add, parent, false);
        return new PlatoAddViewHolder(view);
    }
}
