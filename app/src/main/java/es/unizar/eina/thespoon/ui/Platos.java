package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.thespoon.database.CategoriaPlato;
import es.unizar.eina.thespoon.database.Plato;

/** Pantalla principal de la aplicación Notepad */
public class Platos extends AppCompatActivity {
    private PlatoViewModel mNoteViewModel;

    public static final int ACTIVITY_CREATE = 1;

    public static final int ACTIVITY_EDIT = 2;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;

    RecyclerView mRecyclerView;

    PlatoListAdapter mAdapter;

    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_platos);

        mRecyclerView = findViewById(id.recyclerview);
        mAdapter = new PlatoListAdapter(new PlatoListAdapter.PlatoDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);

        mNoteViewModel.getAllPlatos().observe(this, notes -> {
            // Update the cached copy of the notes in the adapter.
            mAdapter.submitList(notes);
        });

        mFab = findViewById(id.fab);
        mFab.setOnClickListener(view -> {
            createPlato();
        });

        // It doesn't affect if we comment the following instruction
        registerForContextMenu(mRecyclerView);

    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.add_note);
        return result;
    }*/

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
                    Plato newNote = new Plato(extras.getString(PlatoEdit.PLATO_TITLE)
                            , extras.getString(PlatoEdit.PLATO_BODY), CategoriaPlato.PRIMERO, 33);
                    mNoteViewModel.insert(newNote);
                    break;
                case ACTIVITY_EDIT:

                    int id = extras.getInt(PlatoEdit.PLATO_ID);
                    Plato updatedNote = new Plato(extras.getString(PlatoEdit.PLATO_TITLE)
                            , extras.getString(PlatoEdit.PLATO_BODY), CategoriaPlato.PRIMERO, 33);
                    updatedNote.setId(id);
                    mNoteViewModel.update(updatedNote);
                    break;
            }
        }
    }


    public boolean onContextItemSelected(MenuItem item) {
        Plato current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        "Deleting " + current.getNombre(),
                        Toast.LENGTH_LONG).show();
                mNoteViewModel.delete(current);
                return true;
            case EDIT_ID:
                editPlato(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createPlato() {
        Intent intent = new Intent(this, PlatoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }


    private void editPlato(Plato current) {
        Intent intent = new Intent(this, PlatoEdit.class);
        intent.putExtra(PlatoEdit.PLATO_TITLE, current.getNombre());
        intent.putExtra(PlatoEdit.PLATO_BODY, current.getDescripcion());
        intent.putExtra(PlatoEdit.PLATO_ID, current.getId());
        startActivityForResult(intent, ACTIVITY_EDIT);
    }

}