package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.CategoriaPlato;
import es.unizar.eina.thespoon.database.EstadoPedido;
import es.unizar.eina.thespoon.database.Pedido;
import es.unizar.eina.thespoon.database.Plato;

public class Tests extends AppCompatActivity {

    private PlatoViewModel mPlatoViewModel;
    private PedidoViewModel mPedidoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mPedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        // Tests de caja negra
        Button testsCajaNegraButton = findViewById(R.id.testsCajaNegra);
        testsCajaNegraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tests de caja negra ejecutados", Toast.LENGTH_SHORT).show();
            }
        });

        // Tests de volumen
        Button testsVolumenButton = findViewById(R.id.testsVolumen);
        testsVolumenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarTestsVolumen();
                Toast.makeText(getApplicationContext(), "Tests de volumen ejecutados", Toast.LENGTH_SHORT).show();
            }
        });

        // Tests de sobrecarga
        Button testsSobrecargaButton = findViewById(R.id.testsSobrecarga);
        testsSobrecargaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarTestsSobrecarga();
                Toast.makeText(getApplicationContext(), "Tests de sobrecarga ejecutados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ejecutarTestsVolumen() {
        // Se vacía la tabla de platos
        mPlatoViewModel.deleteAll();

        // Se crean 100 platos
        for (int i = 1; i <= 100; i++) {
            Plato plato = new Plato("Plato " + i, "Descripción" + i, CategoriaPlato.PRIMERO, 10.0 + i);
            mPlatoViewModel.insert(plato);
        }

        // Clase de equivalencia no válida (Intentar cargar más de 100 platos)
                /*Plato platoInvalido = new Plato("PlatoInvalido", "DescripciónInvalida", CategoriaPlato.SEGUNDO, -5.5);
                mPlatoViewModel.insert(platoInvalido); // Esto debería manejar el error apropiadamente*/

        // Se vacía la tabla de pedidos
        mPedidoViewModel.deleteAll();

        // Se crean 2000 pedidos
        for (int i = 1; i <= 2000; i++) {
            Pedido pedido = new Pedido("Cliente " + i, "123456789" + i, "18/11/2023 09:33:27", EstadoPedido.SOLICITADO);
            mPedidoViewModel.insert(pedido);
        }

        // Clase de equivalencia no válida (Intentar cargar más de 2000 pedidos)
                /*Pedido pedidoInvalido = new Pedido("ClienteInvalido", "987654321", "2023-12-01 21:00:00", EstadoPedido.PREPARADO);
                mPedidoViewModel.insert(pedidoInvalido); // Esto debería manejar el error apropiadamente*/
    }

    public void ejecutarTestsSobrecarga() {
        // Se vacía la tabla de platos
        mPlatoViewModel.deleteAll();

        // Creamos un plato inicial
        Plato plato = new Plato("Plato 0", "Cero", CategoriaPlato.PRIMERO, 10.0);
        int idPlato = (int) mPlatoViewModel.insert(plato);

        // Intentar insertar platos con descripciones de longitud creciente
        for (int i = 1; i <= 50000; i++) {
            String descripcion = generarDescripcionConLongitud(i);
            plato = new Plato("Plato" + i, descripcion, CategoriaPlato.PRIMERO, 10.0);
            plato.setId(idPlato);

            // Registrar el tamaño del texto insertado
            Log.d("PruebaSobrecarga", "Tamaño del texto insertado: " + i);

            try {
                // Intentar insertar el plato
                mPlatoViewModel.update(plato);
            } catch (Exception e) {
                // La inserción fallará cuando se alcance el límite máximo
                Log.e("PruebaSobrecarga", "Error al insertar plato: " + e.getMessage());
                break;
            }
        }
    }

    private static String generarDescripcionConLongitud(int longitud) {
        // Generar una cadena de longitud específica para la descripción del plato
        StringBuilder descripcion = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            descripcion.append("A");
        }
        return descripcion.toString();
    }
}