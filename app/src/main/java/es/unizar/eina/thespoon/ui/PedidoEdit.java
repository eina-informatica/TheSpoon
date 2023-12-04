package es.unizar.eina.thespoon.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

import es.unizar.eina.thespoon.R;

/** Pantalla utilizada para la creación o edición de un PEDIDO */
public class PedidoEdit extends AppCompatActivity {

    public static final String PEDIDO_CLIENTE = "cliente";
    public static final String PEDIDO_TELEFONO = "telefono";
    public static final String PEDIDO_FECHA = "fecha";

    public static final String PEDIDO_HORA = "hora";
    public static final String PEDIDO_ESTADO = "estado";
    public static final String PEDIDO_ID = "id";
    public static final String PEDIDO_PRECIO = "precio";

    private EditText mTelefonoText;
    private EditText mClienteText;

    private Button mButtonFecha;

    private Button mButtonHora;

    private Integer mRowId;

    Button mSaveButton;

    // Selector de categoría
    String[] estado = {"SOLICITADO", "PREPARADO", "RECOGIDO"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private int estadoSeleccionado = -1;

    Calendar date = Calendar.getInstance();

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

        mSaveButton = findViewById(R.id.button_save);

        populateFields();

        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mClienteText.getText()) ||
                TextUtils.isEmpty(mTelefonoText.getText()) ||
                estadoSeleccionado == -1) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PedidoEdit.PEDIDO_CLIENTE,mClienteText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_TELEFONO, mTelefonoText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_ESTADO, estadoSeleccionado);
                if (mRowId != null) {
                    replyIntent.putExtra(PedidoEdit.PEDIDO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    private void openFechaDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mButtonFecha.setText(String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        dayOfMonth,
                        month+1,
                        year));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void openHoraDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                mButtonHora.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
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
            /*mButtonFecha.setText(extras.getString(PedidoEdit.PEDIDO_FECHA));
            mButtonHora.setText(extras.getString(PedidoEdit.PEDIDO_HORA));*/
            estadoSeleccionado = extras.getInt(PedidoEdit.PEDIDO_ESTADO);
            autoCompleteTextView.setText(estado[estadoSeleccionado], false);
            mRowId = extras.getInt(PedidoEdit.PEDIDO_ID);
        }
    }
}