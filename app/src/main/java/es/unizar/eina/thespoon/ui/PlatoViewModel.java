package es.unizar.eina.thespoon.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.thespoon.database.Plato;
import es.unizar.eina.thespoon.database.PlatoRepository;

public class PlatoViewModel extends AndroidViewModel {

    private PlatoRepository mRepository;

    private final LiveData<List<Plato>> mAllPlatos; // Platos ordenados por nombre
    private final LiveData<List<Plato>> mAllPlatosPorCategoria; // Platos ordenados por categoria

    private final LiveData<List<Plato>> mAllPlatosPorNombreYCategoria; // Platos ordenados por nombre y categoría

    public PlatoViewModel(Application application) {
        super(application);
        mRepository = new PlatoRepository(application);
        mAllPlatos = mRepository.getAllPlatos();
        mAllPlatosPorCategoria = mRepository.getAllPlatosPorCategoria();
        mAllPlatosPorNombreYCategoria = mRepository.getAllPlatosPorNombreYCategoria();
    }

    LiveData<List<Plato>> getAllPlatos() { return mAllPlatos; }
    LiveData<List<Plato>> getAllPlatosPorCategoria() { return mAllPlatosPorCategoria; }

    LiveData<List<Plato>> getAllPlatosPorNombreYCategoria() { return mAllPlatosPorNombreYCategoria; }

    public void insert(Plato plato) { mRepository.insert(plato); }
    public void update(Plato plato) { mRepository.update(plato); }
    public void delete(Plato plato) { mRepository.delete(plato); }
}
