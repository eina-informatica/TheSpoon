package es.unizar.eina.thespoon.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.PedidoRepository;

public class PedidoViewModel extends AndroidViewModel {

    private PedidoRepository mRepository;

    private final LiveData<List<Pedido>> mAllPedidos;

    public PedidoViewModel(Application application) {
        super(application);
        mRepository = new PedidoRepository(application);
        mAllPedidos = mRepository.getAllPedidos();
    }

    LiveData<List<Pedido>> getAllPedidos() { return mAllPedidos; }

    public void insert(Pedido pedido) { mRepository.insert(pedido); }

    public void update(Pedido pedido) { mRepository.update(pedido); }
    public void delete(Pedido pedido) { mRepository.delete(pedido); }
}
