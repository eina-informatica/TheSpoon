package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.R.id;
import static es.unizar.eina.thespoon.R.layout;
import static es.unizar.eina.thespoon.R.string;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.PedidoPlato;
import es.unizar.eina.thespoon.database.Plato;


/** Pantalla principal de la aplicación Notepad */
public class Pedidos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private PedidoViewModel mPedidoViewModel;

    private PedidoPlatoViewModel mPedidoPlatoViewModel;

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

        mPedidoPlatoViewModel = new ViewModelProvider(this).get(PedidoPlatoViewModel.class);

        mFab = findViewById(id.fab);
        mFab.setOnClickListener(view -> {
            createPedido();
        });

        // It doesn't affect if we comment the following instruction
        registerForContextMenu(mRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createPedido();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void OrdenarPopUpPedido(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_pedido);
        popup.show();
    }
    public void FiltrarPopUp(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu_filtrar_pedido);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Ordenar por: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        int itemId = item.getItemId();
        if (itemId == R.id.ordenarPorNombre) {
            mPedidoViewModel.getAllPedidos().observe(this, pedidos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(pedidos);
            });
            return true;
        } else if (itemId == id.ordenarPorEstado) {
            mPedidoViewModel.getAllPedidosPorEstado().observe(this, pedidos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(pedidos);
            });
            return true;
        } else if (itemId == id.ordenarPorAmbos) {
            mPedidoViewModel.getAllPedidosPorNombreYEstado().observe(this, pedidos -> {
                // Update the cached copy of the plates in the adapter.
                mAdapter.submitList(pedidos);
            });
            return true;
        } else if (itemId == R.id.filtrarSolicitados) {
        // Filtrar por estado "Solicitado"
        mPedidoViewModel.filtrarPedidosPorEstado(EstadoPedido.SOLICITADO);
        mPedidoViewModel.getPedidosFiltrados().observe(this, pedidos -> {
            mAdapter.submitList(pedidos);
        });
        return true;
    } else if (itemId == R.id.filtrarPreparados) {
        // Filtrar por estado "Preparado"
        mPedidoViewModel.filtrarPedidosPorEstado(EstadoPedido.PREPARADO);
        mPedidoViewModel.getPedidosFiltrados().observe(this, pedidos -> {
            mAdapter.submitList(pedidos);
        });
        return true;
    } else if (itemId == R.id.filtrarRecogidos) {
        // Filtrar por estado "Recogido"
        mPedidoViewModel.filtrarPedidosPorEstado(EstadoPedido.RECOGIDO);
        mPedidoViewModel.getPedidosFiltrados().observe(this, pedidos -> {
            mAdapter.submitList(pedidos);
        });
        return true;
    }
    else if (itemId == R.id.filtrarTodos){
        mPedidoViewModel.getAllPedidos().observe(this, pedidos -> {
            // Update the cached copy of the plates in the adapter.
            mAdapter.submitList(pedidos);
        });
        return true;
    }
    return false;
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
                        Pedido newPedido = new Pedido(
                                extras.getString(PedidoEdit.PEDIDO_CLIENTE),
                                extras.getString(PedidoEdit.PEDIDO_TELEFONO),
                                extras.getString(PedidoEdit.PEDIDO_FECHA_HORA),
                                EstadoPedido.values()[extras.getInt(PedidoEdit.PEDIDO_ESTADO)]
                        );
                        long pedidoId = mPedidoViewModel.insert(newPedido);
                        List<Pair<Plato, Integer>> platoList = AddPlatoSerializer.deserialize(
                                extras.getString(PedidoEdit.PEDIDO_PLATOS));
                        for (Pair<Plato, Integer> pair : platoList) {
                            PedidoPlato pp = new PedidoPlato((int) pedidoId, pair.first.getId(), pair.second, pair.first.getPrecio());
                            mPedidoPlatoViewModel.insert(pp);
                        }
                    }
                    break;
                case ACTIVITY_EDIT:
                    if (resultCode == RESULT_OK) {
                        int id = extras.getInt(PedidoEdit.PEDIDO_ID);
                        Pedido updatedPedido = new Pedido(
                                extras.getString(PedidoEdit.PEDIDO_CLIENTE),
                                extras.getString(PedidoEdit.PEDIDO_TELEFONO),
                                extras.getString(PedidoEdit.PEDIDO_FECHA_HORA),
                                EstadoPedido.values()[extras.getInt(PedidoEdit.PEDIDO_ESTADO)]
                        );
                        updatedPedido.setId(id);
                        mPedidoViewModel.update(updatedPedido);
                        mPedidoPlatoViewModel.deleteAllFromPedido(id);
                        List<Pair<Plato, Integer>> platoList = AddPlatoSerializer.deserialize(
                                extras.getString(PedidoEdit.PEDIDO_PLATOS));
                        for (Pair<Plato, Integer> pair : platoList) {
                            PedidoPlato pp = new PedidoPlato((int) id, pair.first.getId(), pair.second, pair.first.getPrecio());
                            mPedidoPlatoViewModel.insert(pp);
                        }
                    }
                    break;
            }
        }
    }

    private void createPedido() {
        Intent intent = new Intent(this, PedidoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }
}