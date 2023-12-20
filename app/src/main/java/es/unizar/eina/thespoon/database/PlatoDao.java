package es.unizar.eina.thespoon.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para los platos */
@Dao
public interface PlatoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Plato plato);

    @Update
    int update(Plato plato);

    @Delete
    int delete(Plato plato);

    @Query("DELETE FROM Plato")
    void deleteAll();

    @Query("SELECT * FROM Plato ORDER BY nombre ASC")
    LiveData<List<Plato>> getOrderedPlatos();

    @Query("SELECT * FROM Plato ORDER BY " +
            "CASE WHEN categoria = 'PRIMERO' THEN 1 " +
            "WHEN categoria = 'SEGUNDO' THEN 2 " +
            "WHEN categoria = 'POSTRE' THEN 3 ELSE 4 END")
    LiveData<List<Plato>> getOrderedPlatosPorCategoria();

    @Query("SELECT * FROM Plato ORDER BY nombre ASC, categoria ASC")
    LiveData<List<Plato>> getOrderedPlatosPorNombreYCategoria();

    @Query("SELECT COUNT(*) FROM Plato")
    int getPlatoCount();
}

