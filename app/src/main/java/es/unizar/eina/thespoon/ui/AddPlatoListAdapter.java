package es.unizar.eina.thespoon.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import android.util.Pair;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;
import java.util.ListIterator;

import es.unizar.eina.thespoon.database.Plato;

public class AddPlatoListAdapter extends ListAdapter<Pair<Plato, Integer>, AddPlatoViewHolder> {
    private int position;

    public List<Pair<Plato, Integer>> allPlatos;

    public int getPosition() {
        return position;
    }

    public void setList(List<Pair<Plato, Integer>> list) { allPlatos = list; }

    public void setPosition(int position) {
        this.position = position;
    }

    private PriceChangeListener priceChangeListener;

    public AddPlatoListAdapter(@NonNull DiffUtil.ItemCallback<Pair<Plato, Integer>> diffCallback,
                               PlatoViewModel platoViewModel,
                               PriceChangeListener priceChangeListener) {
        super(diffCallback);
        PlatoViewModel mPlatoViewModel = platoViewModel;
        this.priceChangeListener = priceChangeListener;
    }

    @Override
    public AddPlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AddPlatoViewHolder.create(parent);
    }

    public Pair<Plato, Integer> getCurrent() {
        return getItem(getPosition());
    }

    @Override
    public void onBindViewHolder(AddPlatoViewHolder holder, int position) {
        Pair<Plato, Integer> current = getItem(position);
        holder.bind(current.first.getNombre(), current.second);

        holder.mPlatoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!str.isEmpty()) {
                    int cantidad = Integer.parseInt(str);
                    Pair<Plato, Integer> updatedPair = new Pair<>(current.first, cantidad);

                    // Iterar por todos los platos
                    ListIterator<Pair<Plato, Integer>> iterator = allPlatos.listIterator();
                    while (iterator.hasNext()) {
                        Pair<Plato, Integer> pair = iterator.next();
                        // Si las IDs coinciden, editamos el plato
                        if (pair.first.getId() == current.first.getId()) {
                            iterator.set(updatedPair);
                        }
                    }
                    if (priceChangeListener != null) {
                        priceChangeListener.onQuantityChanged(allPlatos);
                    }
                }
            }
        });
    }


    static class PlatoDiff extends DiffUtil.ItemCallback<Pair<Plato, Integer>> {

        @Override
        public boolean areItemsTheSame(@NonNull Pair<Plato, Integer> oldItem, @NonNull Pair<Plato, Integer> newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.first.getId() == newItem.first.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pair<Plato, Integer> oldItem, @NonNull Pair<Plato, Integer> newItem) {
            //android.util.Log.d ( "PlatoDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.first.getNombre().equals(newItem.first.getNombre());
        }
    }

    public interface PriceChangeListener {
        void onQuantityChanged(List<Pair<Plato, Integer>> platoList);
    }
}
