package es.unizar.eina.thespoon.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.PedidoRepository;
import es.unizar.eina.thespoon.database.Plato;

public class PedidoViewModel extends AndroidViewModel {

    private PedidoRepository mRepository;

    private final LiveData<List<Pedido>> mAllPedidos;
    private final LiveData<List<Pedido>> mAllPedidosPorEstado; // Pedidos ordenados por estado

    private final LiveData<List<Pedido>> mAllPedidosPorNombreYEstado; // Pedidos ordenados por nombre y estado
    private LiveData<List<Pedido>> mPedidosFiltrados;

    public PedidoViewModel(Application application) {
        super(application);
        mRepository = new PedidoRepository(application);
        mAllPedidos = mRepository.getAllPedidos();
        mAllPedidosPorEstado = mRepository.getAllPedidosPorEstado();
        mAllPedidosPorNombreYEstado = mRepository.getAllPedidosPorNombreYEstado();
        mPedidosFiltrados = mAllPedidos; // Inicialmente, muestra todos los pedidos
        //mPedidosEstado=mRepository.getPedidosPorEstado(EstadoPedido estadoSeleccionado);
    }

    LiveData<List<Pedido>> getAllPedidos() { return mAllPedidos; }
    LiveData<List<Pedido>> getAllPedidosPorEstado() { return mAllPedidosPorEstado; }

    LiveData<List<Pedido>> getAllPedidosPorNombreYEstado() { return mAllPedidosPorNombreYEstado; }
    LiveData<List<Pedido>> getPedidosFiltrados() {
        return mPedidosFiltrados;
    }

    void filtrarPedidosPorEstado(EstadoPedido estadoSeleccionado) {
        mPedidosFiltrados = mRepository.getPedidosPorEstado(estadoSeleccionado);
    }

    public void insert(Pedido pedido) { mRepository.insert(pedido); }

    public void update(Pedido pedido) { mRepository.update(pedido); }

    public void delete(Pedido pedido) { mRepository.delete(pedido); }

    }

