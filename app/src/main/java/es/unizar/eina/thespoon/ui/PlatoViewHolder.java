package es.unizar.eina.thespoon.ui;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.thespoon.R;

class PlatoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView mNoteItemView;



    private PlatoViewHolder(View itemView) {
        super(itemView);
        mNoteItemView = itemView.findViewById(R.id.textView);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(String text) {
        mNoteItemView.setText(text);
    }

    static PlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PlatoViewHolder(view);
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, Platos.DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, Platos.EDIT_ID, Menu.NONE, R.string.menu_edit);
    }


}
