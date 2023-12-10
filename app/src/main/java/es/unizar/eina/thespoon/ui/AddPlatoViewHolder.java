package es.unizar.eina.thespoon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.thespoon.R;

class AddPlatoViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
    final TextView mPlatoTextview;
    final EditText mPlatoEditText; // Cantidad del plato para ese pedido
    private AddPlatoViewHolder(View itemView) {
        super(itemView);
        mPlatoTextview = itemView.findViewById(R.id.textView);
        mPlatoEditText = itemView.findViewById(R.id.editText);
    }

    public void bind(String nombre, int cantidad) {

        mPlatoTextview.setText(nombre);
        mPlatoEditText.setText(String.valueOf(cantidad));
    }

    static AddPlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_plato_add, parent, false);
        return new AddPlatoViewHolder(view);
    }
}
