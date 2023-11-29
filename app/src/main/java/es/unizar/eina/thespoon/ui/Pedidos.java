package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.R.id;
import static es.unizar.eina.thespoon.R.layout;
import static es.unizar.eina.thespoon.R.string;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.thespoon.database.CategoriaPedido;
import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;


/** Pantalla principal de la aplicación Notepad */
public class Pedidos extends AppCompatActivity {
    private PedidoViewModel mPedidoViewModel;

    /*public static final String RESULT_CODE = "resultCode";

    public static final int RESULT_OK = 1;

    public static final int RESULT_CANCELED = 2;

    public static final String REQUEST_CODE = "requestCode";*/

    public static final int ACTIVITY_CREATE = 1;

    public static final int ACTIVITY_EDIT = 2;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;

    RecyclerView mRecyclerView;

    PedidoListAdapter mAdapter;

    FloatingActionButton mFab;

    public ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_pedidos);

        mRecyclerView = findViewById(id.recyclerview);
        PedidoViewModel PedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
        mAdapter = new PedidoListAdapter(new PedidoListAdapter.PedidoDiff(),PedidoViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        mPedidoViewModel.getAllPedidos().observe(this, pedidos -> {
            // Update the cached copy of the notes in the adapter.
            mAdapter.submitList(pedidos);
        });

        mFab = findViewById(id.fab);
        mFab.setOnClickListener(view -> {
            createPedido();
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
                createPedido();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // Handle the case where the Intent data is null (possibly canceled operation)
            Toast.makeText(
                    getApplicationContext(),
                    string.empty_pedido,
                    Toast.LENGTH_LONG).show();
            return;
        }

        Bundle extras = data.getExtras();

        if (resultCode != RESULT_OK) {
            Toast.makeText(
                    getApplicationContext(),
                    string.empty_pedido,
                    Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    if (resultCode == RESULT_OK) {
                       /* String cliente = extras.getString(PedidoEdit.PEDIDO_NOMBRE_CLIENTE);
                        String fechaHora = extras.getString(PedidoEdit.PEDIDO_FECHAHORA);
                        String nombre = cliente + " - " + fechaHora;*/
                        Pedido newPedido = new Pedido(
                            extras.getString(PedidoEdit.PEDIDO_NOMBRE_CLIENTE),
                            extras.getString(PedidoEdit.PEDIDO_TELEFONO),
                            extras.getString(PedidoEdit.PEDIDO_FECHAHORA),
                            EstadoPedido.values()[extras.getInt(PedidoEdit.PEDIDO_ESTADO)]
                        );
                        mPedidoViewModel.insert(newPedido);
                    }
                    break;
                case ACTIVITY_EDIT:
                    if (resultCode == RESULT_OK) {
                        int id = extras.getInt(PedidoEdit.PEDIDO_ID);
                        String cliente = extras.getString(PedidoEdit.PEDIDO_NOMBRE_CLIENTE);
                        String fechaHora = extras.getString(PedidoEdit.PEDIDO_FECHAHORA);
                        String nombre = cliente + " - " + fechaHora;

                        Pedido updatedPedido = new Pedido(
                                cliente,
                                extras.getString(PedidoEdit.PEDIDO_TELEFONO),
                                fechaHora,
                                EstadoPedido.values()[extras.getInt(PedidoEdit.PEDIDO_ESTADO)]
                              //  extras.getDouble(PedidoEdit.PEDIDO_PRECIO)
                        );
                        updatedPedido.setId(id);
                        mPedidoViewModel.update(updatedPedido);
                    }
                    break;
            }
        }
    }

    /*public boolean onContextItemSelected(MenuItem item) {
        Pedido current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        "Deleting " + current.getNombre(),
                        Toast.LENGTH_LONG).show();
                mPedidoViewModel.delete(current);
                return true;
            case EDIT_ID:
                editPedido(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }*/

    private void createPedido() {
        Intent intent = new Intent(this, PedidoEdit.class);
        /*intent.putExtra(REQUEST_CODE, ACTIVITY_CREATE);
        activityResultLauncher.launch(intent);*/
        //startActivity(intent);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }
}