package es.unizar.eina.thespoon.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.PedidoPlato;
import es.unizar.eina.thespoon.database.PedidoPlatoRepository;

public class AddPlatoViewModel extends AndroidViewModel {

    private PedidoPlatoRepository mRepository;

    private final LiveData<List<PedidoPlato>> mAllPedidoPlatos;

    public AddPlatoViewModel(Application application) {
        super(application);
        mRepository = new PedidoPlatoRepository(application);
        mAllPedidoPlatos = mRepository.getAllPedidoPlatos();
    }

    LiveData<List<PedidoPlato>> getAllPedidoPlatos() { return mAllPedidoPlatos; }

    public long insert(PedidoPlato pedidoPlato) { return mRepository.insert(pedidoPlato); }

    public void update(PedidoPlato pedidoPlato) { mRepository.update(pedidoPlato); }

    public void delete(PedidoPlato pedidoPlato) { mRepository.delete(pedidoPlato); }

}
