package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;
import es.unizar.eina.thespoon.database.SDF;

public class AddPlatoToPedido extends AppCompatActivity {

    private PlatoViewModel mPlatoViewModel;

    public static final String PLATOS = "platos";

    //SearchView mSearchView;

    RecyclerView mRecyclerView;

    Button mAddPlatos;

    AddPlatoListAdapter mAdapter;

    LiveData<List<Plato>> allPlatosLiveData;
    List<Pair<Plato, Integer>> allPlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plato_to_pedido);

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        allPlatosLiveData = mPlatoViewModel.getAllPlatos();

        /*mSearchView = findViewById(R.id.searchView);
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
        });*/

        mRecyclerView = findViewById(R.id.recyclerview);
        PlatoViewModel platoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mAdapter = new AddPlatoListAdapter(new AddPlatoListAdapter.PlatoDiff(), platoViewModel);
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

        mAddPlatos = findViewById(R.id.addPlatos);
        mAddPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String platos = "";
                for (Pair<Plato, Integer> pair : allPlatos) {
                    if (pair.second > 0) {
                        platos += String.valueOf(pair.first.getId()) + "-" + String.valueOf(pair.second) + ",";
                    }
                }
                Log.d("Platos añadidos", platos);
                // Pasar la información
                Intent replyIntent = new Intent();
                replyIntent.putExtra(PLATOS, platos);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    /*private void filterList(String newText) {
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
    }*/
}