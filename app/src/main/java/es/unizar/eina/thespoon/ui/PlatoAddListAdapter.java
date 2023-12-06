package es.unizar.eina.thespoon.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.thespoon.database.Plato;

public class PlatoAddListAdapter extends ListAdapter<Plato, PlatoAddViewHolder> {
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PlatoAddListAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback, PlatoViewModel platoViewModel) {
        super(diffCallback);
        PlatoViewModel mPlatoViewModel = platoViewModel;
    }

    @Override
    public PlatoAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlatoAddViewHolder.create(parent);
    }

    public Plato getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(PlatoAddViewHolder holder, int position) {
        Plato current = getItem(position);
        holder.bind(current.getNombre());
    }


    static class PlatoDiff extends DiffUtil.ItemCallback<Plato> {

        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getNombre().equals(newItem.getNombre());
        }
    }
}
