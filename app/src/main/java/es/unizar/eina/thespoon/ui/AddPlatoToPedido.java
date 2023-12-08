package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;

public class AddPlatoToPedido extends AppCompatActivity {

    private PlatoViewModel mPlatoViewModel;

    SearchView mSearchView;

    RecyclerView mRecyclerView;

    PlatoAddListAdapter mAdapter;

    LiveData<List<Plato>> allPlatosLiveData;
    List<Pair<Plato, Integer>> allPlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plato_to_pedido);

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        allPlatosLiveData = mPlatoViewModel.getAllPlatos();

        mSearchView = findViewById(R.id.searchView);
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        PlatoViewModel platoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mAdapter = new PlatoAddListAdapter(new PlatoAddListAdapter.PlatoDiff(), platoViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        allPlatos = new ArrayList<>();
        allPlatosLiveData.observe(this, platos -> {
            for (Plato plato : platos) {
                Pair<Plato, Integer> pair = new Pair<>(plato, 0);
                allPlatos.add(pair);
            }
            // Update the cached copy of the plates in the adapter.
            mAdapter.submitList(allPlatos);
        });
        mAdapter.setList(allPlatos);
    }

    private void filterList(String newText) {
        List<Pair<Plato, Integer>> filteredPlatos = filterPlatos(allPlatos, newText);

        // Update the adapter
        mAdapter.submitList(filteredPlatos);
    }

    private List<Pair<Plato, Integer>> filterPlatos(List<Pair<Plato, Integer>> allPlatos, String newText) {
        // Implement your filtering logic here based on newText
        // For example, you can filter platos whose name contains the newText
        List<Pair<Plato, Integer>> filteredPlatos = new ArrayList<>();
        for (Pair<Plato, Integer> pair : allPlatos) {
            if (pair.first.getNombre().toLowerCase().contains(newText.toLowerCase())) {
                filteredPlatos.add(pair);
            }
        }
        return filteredPlatos;
    }
}