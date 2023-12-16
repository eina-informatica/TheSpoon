package es.unizar.eina.thespoon.ui;

import static es.unizar.eina.thespoon.ui.AddPlatoToPedido.PLATOS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;
import es.unizar.eina.thespoon.database.PlatoPedido;
import es.unizar.eina.thespoon.database.SDF;

/** Pantalla utilizada para la creación o edición de un PEDIDO */
public class PedidoEdit extends AppCompatActivity implements AddPlatoListAdapter.PriceChangeListener {

    public static final int ACTIVITY_PLATOS_ADD = 1;

    public static final int ACTIVITY_PLATOS_EDIT = 2;

    public static final String PEDIDO_CLIENTE = "cliente";
    public static final String PEDIDO_TELEFONO = "telefono";
    public static final String PEDIDO_FECHA_HORA = "fechaHora";

    public static final String PEDIDO_PLATOS = "platos";
    public static final String PEDIDO_ESTADO = "estado";
    public static final String PEDIDO_ID = "id";
    public static final String PEDIDO_PRECIO = "precio";

    private EditText mTelefonoText;
    private EditText mClienteText;

    private Button mButtonFecha;

    private Button mButtonHora;

    private Integer mRowId;

    Button mAddPlatos;

    Button mSaveButton;


    // Selector de categoría
    String[] estado = {"SOLICITADO", "PREPARADO", "RECOGIDO"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private int estadoSeleccionado = -1;

    // Fecha y hora de recogida
    Calendar date = Calendar.getInstance();

    // Inicialmente supones que no se ha elegido ni fecha ni hora
    boolean fechaEscogida = false;
    boolean horaEscogida = false;

    // Platos añadidos al pedido
    private PedidoPlatoViewModel mPedidoPlatoViewModel;

    String platos;

    public List<Pair<Plato, Integer>> platoList;

    RecyclerView mRecyclerView;
    AddPlatoListAdapter mAdapter;

    // Precio del pedido
    TextView mTextViewPrecio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        // Datos del cliente
        mClienteText = findViewById(R.id.clientePedido);
        mTelefonoText = findViewById(R.id.telefonoPedido);

        // Datos de recogida
        mButtonFecha = findViewById(R.id.buttonFecha);
        mButtonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFechaDialog();
            }
        });
        mButtonHora = findViewById(R.id.buttonHora);
        mButtonHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHoraDialog();
            }
        });

        // Añadir platos al pedido
        mAddPlatos = findViewById(R.id.button_add_platos);
        mAddPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PedidoEdit.this, AddPlatoToPedido.class);
                intent.putExtra(PLATOS, AddPlatoSerializer.serialize(platoList));
                startActivityForResult(intent, ACTIVITY_PLATOS_ADD);
            }
        });

        platoList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerview);
        PlatoViewModel platoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mAdapter = new AddPlatoListAdapter(new AddPlatoListAdapter.PlatoDiff(), platoViewModel, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPedidoPlatoViewModel = new ViewModelProvider(this).get(PedidoPlatoViewModel.class);

        // Selector de estado
        autoCompleteTextView = findViewById(R.id.autocompletePedido);
        adapterItems = new ArrayAdapter<String>(this, R.layout.text_item, estado);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
                estadoSeleccionado = position;
            }
        });

        // Precio del pedido
        mTextViewPrecio = findViewById(R.id.textviewPrecio);

        mSaveButton = findViewById(R.id.button_save);

        populateFields();

        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mClienteText.getText()) ||
                TextUtils.isEmpty(mTelefonoText.getText()) ||
                !fechaEscogida ||
                !horaEscogida ||
                estadoSeleccionado == -1) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PedidoEdit.PEDIDO_CLIENTE, mClienteText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_TELEFONO, mTelefonoText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_FECHA_HORA, SDF.format(date.getTime()));
                replyIntent.putExtra(PedidoEdit.PEDIDO_PLATOS, AddPlatoSerializer.serialize(platoList));
                replyIntent.putExtra(PedidoEdit.PEDIDO_ESTADO, estadoSeleccionado);
                if (mRowId != null) {
                    replyIntent.putExtra(PedidoEdit.PEDIDO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // Handle the case where the Intent data is null (possibly canceled operation)
            Toast.makeText(
                    getApplicationContext(),
                    R.string.ningun_plato,
                    Toast.LENGTH_LONG).show();
            return;
        }

        Bundle extras = data.getExtras();

        if (resultCode != RESULT_OK) {
            Toast.makeText(
                getApplicationContext(),
                R.string.error,
                Toast.LENGTH_LONG).show();
        } else {
            switch (requestCode) {
                case ACTIVITY_PLATOS_ADD:
                    if (resultCode == RESULT_OK) {
                        platos = extras.getString(PLATOS);
                        platoList.clear();
                        mAdapter.notifyDataSetChanged();
                        platoList = AddPlatoSerializer.deserialize(platos);
                        // Actualizar recyclerView
                        mAdapter.submitList(platoList);
                        mAdapter.setList(platoList);
                        // Actualizar precio del pedido
                        onQuantityChanged(platoList);
                    }
                    break;
            }
        }
    }

    private void openFechaDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.set(year, month, dayOfMonth);
                mButtonFecha.setText(SDF.getDate(SDF.format(date.getTime())));
                fechaEscogida = true;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void openHoraDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                date.set(
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH),
                    selectedHour,
                    selectedMinute
                );
                mButtonHora.setText(SDF.getTime(SDF.format(date.getTime())));
                horaEscogida = true;
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        dialog.show();
    }

    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mClienteText.setText(extras.getString(PedidoEdit.PEDIDO_CLIENTE));
            mTelefonoText.setText(extras.getString(PedidoEdit.PEDIDO_TELEFONO));
            String fechaHora = extras.getString(PedidoEdit.PEDIDO_FECHA_HORA);
            mButtonFecha.setText(SDF.getDate(fechaHora));
            mButtonHora.setText(SDF.getTime(fechaHora));
            date.setTime(SDF.parse(fechaHora));
            fechaEscogida = true;
            horaEscogida = true;
            estadoSeleccionado = extras.getInt(PedidoEdit.PEDIDO_ESTADO);
            autoCompleteTextView.setText(estado[estadoSeleccionado], false);
            mRowId = extras.getInt(PedidoEdit.PEDIDO_ID);
            mPedidoPlatoViewModel.getPlatosPorPedidoId(mRowId).observe(this, pedidoPlatos -> {
                for (PlatoPedido pp : pedidoPlatos) {
                    Pair<Plato, Integer> pair = new Pair<>(pp.plato, pp.pedidoPlato.getCantidad());
                    pair.first.setPrecio(pp.pedidoPlato.getPrecio());
                    platoList.add(pair);
                }
                mAdapter.submitList(platoList);
                mAdapter.setList(platoList);
                // Actualizar precio del pedido
                onQuantityChanged(platoList);
            });
        }
    }

    @Override
    public void onQuantityChanged(List<Pair<Plato, Integer>> platoList) {
        // Actualizar precio del pedido
        double precioTotal = AddPlatoSerializer.calcularPrecio(platoList);
        mTextViewPrecio.setText(String.valueOf(precioTotal) + " €");
    }
}
