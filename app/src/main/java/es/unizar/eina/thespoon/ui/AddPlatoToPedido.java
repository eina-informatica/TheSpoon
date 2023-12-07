package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;

public class AddPlatoToPedido extends AppCompatActivity {

    private PlatoViewModel mPlatoViewModel;

    SearchView mSearchView;

    RecyclerView mRecyclerView;

    PlatoAddListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plato_to_pedido);

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

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);

        mPlatoViewModel.getAllPlatos().observe(this, platos -> {
            // Update the cached copy of the plates in the adapter.
            mAdapter.submitList(platos);
        });
    }

    private void filterList(String newText) {
        LiveData<List<Plato>> allPlatosLiveData = mPlatoViewModel.getAllPlatos();

        MediatorLiveData<List<Plato>> filteredPlatosLiveData = new MediatorLiveData<>();
        filteredPlatosLiveData.addSource(allPlatosLiveData, platos -> {
            // Apply your filtering logic here based on newText
            List<Plato> filteredPlatos = filterPlatos(platos, newText);
            filteredPlatosLiveData.setValue(filteredPlatos);
        });

        // Observe the filtered LiveData and update the adapter
        filteredPlatosLiveData.observe(this, platos -> {
            mAdapter.submitList(platos);
        });
    }

    private List<Plato> filterPlatos(List<Plato> allPlatos, String newText) {
        // Implement your filtering logic here based on newText
        // For example, you can filter platos whose name contains the newText
        List<Plato> filteredPlatos = new ArrayList<>();
        for (Plato plato : allPlatos) {
            if (plato.getNombre().toLowerCase().contains(newText.toLowerCase())) {
                filteredPlatos.add(plato);
            }
        }
        return filteredPlatos;
    }
}