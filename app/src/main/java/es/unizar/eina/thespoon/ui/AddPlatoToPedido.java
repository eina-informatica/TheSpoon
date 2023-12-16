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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;
import es.unizar.eina.thespoon.database.PlatoPedido;
import es.unizar.eina.thespoon.database.SDF;

public class AddPlatoToPedido extends AppCompatActivity implements AddPlatoListAdapter.PriceChangeListener {

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
        mAdapter = new AddPlatoListAdapter(new AddPlatoListAdapter.PlatoDiff(), mPlatoViewModel, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateFields();

        mAddPlatos = findViewById(R.id.addPlatos);
        mAddPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String platos = AddPlatoSerializer.serialize(allPlatos);
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

    @Override
    public void onQuantityChanged(List<Pair<Plato, Integer>> platoList) {}

    private void populateFields() {
        List<Pair<Plato, Integer>> platoList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String platosString = extras.getString(PLATOS);
            if (!TextUtils.isEmpty(platosString)) {
                platoList = AddPlatoSerializer.deserialize(platosString);
            }
        }

        allPlatos = new ArrayList<>();
        List<Pair<Plato, Integer>> finalPlatoList = platoList;
        allPlatosLiveData.observe(this, platos -> {
            for (Plato plato : platos) {
                boolean encontrado = false; // Inicialmente suponemos que no se ha añadido este plato anteriormente
                Pair<Plato, Integer> newPair = null;
                for (Pair<Plato, Integer> pair : finalPlatoList) {
                    if (pair.first.getId() == plato.getId()) {
                        encontrado = true;
                        newPair = new Pair<>(plato, pair.second);
                    }
                }
                if (!encontrado) {
                    newPair = new Pair<>(plato, 0);
                }
                if (newPair != null) {
                    allPlatos.add(newPair);
                }
            }
            // Update the cached copy of the plates in the adapter.
            mAdapter.submitList(allPlatos);
        });
        mAdapter.setList(allPlatos);
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String platosString = extras.getString(PLATOS);
            if (!TextUtils.isEmpty(platosString)) {
                List<Pair<Plato, Integer>> platoList = AddPlatoSerializer.deserialize(platosString);
                ListIterator<Pair<Plato, Integer>> iterator = allPlatos.listIterator();
                while (iterator.hasNext()) {
                    Pair<Plato, Integer> pair1 = iterator.next();
                    // Si las IDs coinciden, cambiamos la cantidad del plato
                    for (Pair<Plato, Integer> pair2 : platoList) {
                        if (pair1.first.getId() == pair2.first.getId()) {
                            Pair<Plato, Integer> updatedPair = new Pair<>(pair1.first, pair2.second);
                            iterator.set(updatedPair);
                        }
                    }
                }
                mAdapter.submitList(allPlatos);
                mAdapter.setList(allPlatos);
            }
        }*/
    }
}