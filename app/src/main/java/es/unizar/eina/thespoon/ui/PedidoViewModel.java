package es.unizar.eina.thespoon.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.PedidoRepository;
import es.unizar.eina.thespoon.database.Plato;

public class PedidoViewModel extends AndroidViewModel {

    private PedidoRepository mRepository;
    private LiveData<List<Pedido>> mPedidosFiltrados;

    public PedidoViewModel(Application application) {
        super(application);
        mRepository = new PedidoRepository(application);
    }

    LiveData<List<Pedido>> getAllPedidos(EstadoPedido estadoSeleccionado) { return  mRepository.getAllPedidos(estadoSeleccionado); }
    LiveData<List<Pedido>> getAllPedidosPorEstado(EstadoPedido estadoSeleccionado) { return mRepository.getAllPedidosPorEstado(estadoSeleccionado); }

    LiveData<List<Pedido>> getAllPedidosPorNombreYEstado(EstadoPedido estadoSeleccionado) { return mRepository.getAllPedidosPorNombreYEstado(estadoSeleccionado); }
    /*LiveData<List<Pedido>> getPedidosFiltrados() {
        return mPedidosFiltrados;
    }

    void filtrarPedidosPorEstado(EstadoPedido estadoSeleccionado) {
        mPedidosFiltrados = mRepository.getPedidosPorEstado(estadoSeleccionado);
    }*/

    public long insert(Pedido pedido) {
        return mRepository.insert(pedido);
    }

    public void update(Pedido pedido) { mRepository.update(pedido); }

    public void delete(Pedido pedido) { mRepository.delete(pedido); }

    public void deleteAll() { mRepository.deleteAll(); }

}

