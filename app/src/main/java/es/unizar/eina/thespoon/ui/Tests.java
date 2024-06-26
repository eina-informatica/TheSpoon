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
                ejecutarPlatosCajaNegra();
                ejecutarPedidosCajaNegra();
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
        for (int i = 1; i <= 101; i++) {
            Plato plato = new Plato("Plato " + i, "Descripción" + i, CategoriaPlato.PRIMERO, 10.0 + i);
            mPlatoViewModel.insert(plato);
        }

        // Se vacía la tabla de pedidos
        mPedidoViewModel.deleteAll();

        // Se crean 2000 pedidos
        for (int i = 1; i <= 2001; i++) {
            Pedido pedido = new Pedido("Cliente " + i, "123456789" + i, "14/02/2024 20:00:00", EstadoPedido.SOLICITADO);
            mPedidoViewModel.insert(pedido);
        }
    }

    public void ejecutarTestsSobrecarga() {
        // Se vacía la tabla de platos
        mPlatoViewModel.deleteAll();

        // Creamos un plato inicial
        Plato plato = new Plato("Plato 0", "Cero", CategoriaPlato.PRIMERO, 10.0);
        int idPlato = (int) mPlatoViewModel.insert(plato);

        // Intentar insertar platos con descripciones de longitud creciente
        for (int i = 1; i <= 100000; i++) {
            try {
                String descripcion = generarDescripcionConLongitud(i);
                plato = new Plato("Plato" + i, descripcion, CategoriaPlato.PRIMERO, 10.0);
                plato.setId(idPlato);

                // Registrar el tamaño del texto insertado
                Log.d("PruebaSobrecarga", "Tamaño del texto insertado: " + i);

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

    public void testPlatoNombreCategoriaDescripcionPrecioSuccess() {
        // Test case 1
        // Expected result: Success
        // Classes covered: 1, 2, 3, 5, 6
        Plato plato = new Plato("Pizza", "Descripción1", CategoriaPlato.PRIMERO, 15.99);
        long id = mPlatoViewModel.insert(plato);
    }


    public void testPlatoNombreNullCategoriaDescripcionPrecio() {
        // Test case 2
        // Expected result: Error
        // Classes covered: 8, 9
        Plato plato = new Plato(null, "Descripción2", CategoriaPlato.POSTRE, 5.00);

        long id = mPlatoViewModel.insert(plato);
    }

    public void testPlatoNombreCategoriaNullDescripcionPrecio() {
        // Test case 3
        // Expected result: Error
        // Classes covered: 10
        Plato plato = new Plato("Sushi", null, CategoriaPlato.SEGUNDO, 30.00);

        long id = mPlatoViewModel.insert(plato);
    }

   /*
    public void testPlatoNombreCategoriaDescripcionPrecioFastFood() {
        // Test case 4
        // Expected result: Error
        // Classes covered: 11
        Plato plato = new Plato("Burger", "Descripción4", "Fast Food", 15.00);

    }*/


    public void testPlatoNombreCategoriaDescripcionPrecioInvalidCharacters() {
        // Test case 5
        // Expected result: Error
        // Classes covered: 12
        Plato plato = new Plato("Nombre5",  "Descripción4",CategoriaPlato.SEGUNDO, 15.00);

        long id = mPlatoViewModel.insert(plato);
    }


    public void testPlatoNombreCategoriaDescripcionPrecioNegative() {
        // Test case 6
        // Expected result: Error
        // Classes covered: 13
        Plato plato = new Plato("Nombre5",  "Descripción4",CategoriaPlato.SEGUNDO, -2.5);

        long id = mPlatoViewModel.insert(plato);
    }
    public void testPlatoNombreCategoriaDescripcionPrecioNonNumeric() {
        // Test case 7
        // Expected result: Error
        // Classes covered: 14
        Plato plato = new Plato("Nombre5",  "Descripción4", CategoriaPlato.SEGUNDO, Double.parseDouble("abc"));

        long id = mPlatoViewModel.insert(plato);
    }
    public void ejecutarPlatosCajaNegra() {
        try {
            testPlatoNombreCategoriaDescripcionPrecioSuccess();
            testPlatoNombreNullCategoriaDescripcionPrecio();
            testPlatoNombreCategoriaNullDescripcionPrecio();
            //testPlatoNombreCategoriaDescripcionPrecioFastFood()
            testPlatoNombreCategoriaDescripcionPrecioInvalidCharacters();
            testPlatoNombreCategoriaDescripcionPrecioNegative();
            testPlatoNombreCategoriaDescripcionPrecioNonNumeric();
        }catch (Exception e){}

    }

    public void testPedidoNombreClienteTelefonoFechaHoraEstadoSuccess() {
        // Test case 1
        // Expected result: Success
        // Classes covered: 1, 2, 3, 5, 6, 7, 8
        Pedido pedido = new Pedido("Pedro", "697697697", "20/01/2024 20:00:00", EstadoPedido.SOLICITADO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreClienteNullFechaHoraEstado() {
        // Test case 2
        // Expected result: Error
        // Classes covered: 9
        Pedido pedido = new Pedido(null, "555555555", "20/01/2024 20:00:00", EstadoPedido.SOLICITADO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreClienteVacioTelefonoFechaHoraPreparado() {
        // Test case 3
        // Expected result: Error
        // Classes covered: 10
        Pedido pedido = new Pedido("", "555555555", "20/01/2024 21:00:00", EstadoPedido.PREPARADO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreCliente3TelefonoFechaHoraRecogido() {
        // Test case 4
        // Expected result: Error
        // Classes covered: 11
        Pedido pedido = new Pedido("Cliente3", "abec", "20/01/2024 22:00:00", EstadoPedido.RECOGIDO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreCliente3TelefonoNullFechaHoraSolicitado() {
        // Test case 5
        // Expected result: Error
        // Classes covered: 12
        Pedido pedido = new Pedido("Cliente3", "", "20/01/2024 23:00:00", EstadoPedido.SOLICITADO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreCliente5TelefonoFechaHoraPreparado() {
        // Test case 6
        // Expected result: Error
        // Classes covered: 13
        Pedido pedido = new Pedido("Cliente5", "111111111", "30/11/2023 20:00:00", EstadoPedido.PREPARADO);
        long id = mPedidoViewModel.insert(pedido);
    }

    public void testPedidoNombreCliente5TelefonoSegundoFechaHoraPreparado() {
        // Test case 7
        // Expected result: Error
        // Classes covered: 14
        Pedido pedido = new Pedido("Cliente5", "111111111", "20/01/2024 19:00:00", EstadoPedido.PREPARADO);
        long id = mPedidoViewModel.insert(pedido);
    }
    public void testPedidoNombreCliente5TelefonoSegundoFechaHoraPreparado2() {
        // Test case 7
        // Expected result: Error
        // Classes covered: 14
        Pedido pedido = new Pedido("Cliente5", "111111111", "21/01/2024 00:00:03", EstadoPedido.PREPARADO);
        long id = mPedidoViewModel.insert(pedido);
    }
    /* public void testPedidoNombreCliente5TelefonoFechaHoraEnCamino() {
        // Test case 8
        // Expected result: Error
        // Classes covered: 15
        Pedido pedido = new Pedido("Cliente5", "111111111", "05/12/2023 22:30:00", "EN_CAMINO");
    }*/

    public void testPedidoNombreCliente5TelefonoFechaHoraEstadoNull() {
        // Test case 9
        // Expected result: Error
        // Classes covered: 16
        Pedido pedido = new Pedido("Cliente5", "111111111", "20/01/2024 22:30:00", null);
        long id = mPedidoViewModel.insert(pedido);

    }
        public void ejecutarPedidosCajaNegra(){
            try {
                testPedidoNombreClienteTelefonoFechaHoraEstadoSuccess();
                testPedidoNombreClienteNullFechaHoraEstado();
                testPedidoNombreClienteVacioTelefonoFechaHoraPreparado();
                testPedidoNombreCliente3TelefonoFechaHoraRecogido();
                testPedidoNombreCliente3TelefonoNullFechaHoraSolicitado();
                testPedidoNombreCliente5TelefonoFechaHoraPreparado();
                testPedidoNombreCliente5TelefonoSegundoFechaHoraPreparado();
                //testPedidoNombreCliente5TelefonoFechaHoraEnCamino()
                testPedidoNombreCliente5TelefonoSegundoFechaHoraPreparado2();
                testPedidoNombreCliente5TelefonoFechaHoraEstadoNull();
            }catch (Exception e){}

        }
}
