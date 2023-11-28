package es.unizar.eina.thespoon.ui;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.thespoon.R;

class PlatoViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
    final TextView mPlatoItemView;
    ImageView mPlatoEditButton;
    ImageView mPlatoDeleteButton;
    private PlatoViewHolder(View itemView) {
        super(itemView);
        mPlatoItemView = itemView.findViewById(R.id.textView);
        mPlatoEditButton = itemView.findViewById(R.id.editPlato);
        mPlatoDeleteButton= itemView.findViewById(R.id.deletePlato);
        //mPlatoVerButton=itemView.findViewById(R.id.VerPlato);

        //itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(String text) {
        mPlatoItemView.setText(text);
    }

    static PlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PlatoViewHolder(view);
    }


    /*public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, Platos.DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, Platos.EDIT_ID, Menu.NONE, R.string.menu_edit);
    }*/
}
