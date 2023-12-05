package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.R.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.CategoriaPlato;
import es.unizar.eina.thespoon.database.Plato;

/** Pantalla principal de la aplicación Notepad */
public class Platos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private PlatoViewModel mPlatoViewModel;

    public static final int ACTIVITY_CREATE = 1;

    public static final int ACTIVITY_EDIT = 2;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;

    RecyclerView mRecyclerView;

    PlatoListAdapter mAdapter;

    FloatingActionButton mFab;

    public ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_platos);

        mRecyclerView = findViewById(id.recyclerview);
        PlatoViewModel platoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mAdapter = new PlatoListAdapter(new PlatoListAdapter.PlatoDiff(), platoViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);

        mPlatoViewModel.getAllPlatos().observe(this, platos -> {
            // Update the cached copy of the plates in the adapter.
            mAdapter.submitList(platos);
        });

        mFab = findViewById(id.fab);
        mFab.setOnClickListener(view -> {
            createPlato();
        });

        // It doesn't affect if we comment the following instruction
        registerForContextMenu(mRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createPlato();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        if (resultCode != RESULT_OK) {
            Toast.makeText(
                    getApplicationContext(),
                    string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    Plato newPlato = new Plato(
                        extras.getString(PlatoEdit.PLATO_NOMBRE),
                        extras.getString(PlatoEdit.PLATO_DESCRIPCION),
                        CategoriaPlato.values()[extras.getInt(PlatoEdit.PLATO_CATEGORIA)],
                        extras.getDouble(PlatoEdit.PLATO_PRECIO));
                    mPlatoViewModel.insert(newPlato);
                    break;
                case ACTIVITY_EDIT:
                    int id = extras.getInt(PlatoEdit.PLATO_ID);
                    Plato updatedPlato = new Plato(
                        extras.getString(PlatoEdit.PLATO_NOMBRE),
                        extras.getString(PlatoEdit.PLATO_DESCRIPCION),
                        CategoriaPlato.values()[extras.getInt(PlatoEdit.PLATO_CATEGORIA)],
                        extras.getDouble(PlatoEdit.PLATO_PRECIO));
                    updatedPlato.setId(id);
                    mPlatoViewModel.update(updatedPlato);
                    break;
            }
        }
    }

    private void createPlato() {
        Intent intent = new Intent(this, PlatoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    public void showPopupOrdenar(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(menu.popup_menu_plato);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Ordenar por: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        int itemId = item.getItemId();
        if (itemId == R.id.ordenarPorNombre) {
            mPlatoViewModel.getAllPlatos().observe(this, platos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(platos);
            });
            return true;
        } else if (itemId == id.ordenarPorCategoria) {
            mPlatoViewModel.getAllPlatosPorCategoria().observe(this, platos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(platos);
            });
            return true;
        } else if (itemId == id.ordenarPorAmbos) {
            mPlatoViewModel.getAllPlatosPorNombreYCategoria().observe(this, platos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(platos);
            });
            return true;
        }
        return false;
    }
}