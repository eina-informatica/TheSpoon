package es.unizar.eina.thespoon.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.PedidoPlato;
import es.unizar.eina.thespoon.database.PedidoPlatoRepository;
import es.unizar.eina.thespoon.database.PlatoPedido;

public class PedidoPlatoViewModel extends AndroidViewModel {

    private PedidoPlatoRepository mRepository;

    //private final LiveData<List<PedidoPlato>> mAllPedidoPlatos;

    public PedidoPlatoViewModel(Application application) {
        super(application);
        mRepository = new PedidoPlatoRepository(application);
        //mAllPedidoPlatos = mRepository.getAllPedidoPlatos();
    }

    //LiveData<List<PedidoPlato>> getAllPedidoPlatos() { return mAllPedidoPlatos; }
    LiveData<List<PlatoPedido>> getPlatosPorPedidoId(int pedidoId) { return mRepository.getPlatosPorPedidoId(pedidoId); }

    public long insert(PedidoPlato pedidoPlato) { return mRepository.insert(pedidoPlato); }

    public void update(PedidoPlato pedidoPlato) { mRepository.update(pedidoPlato); }

    public void delete(PedidoPlato pedidoPlato) { mRepository.delete(pedidoPlato); }

}
