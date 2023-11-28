package es.unizar.eina.thespoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.thespoon.R;

/** Pantalla utilizada para la creación o edición de un PEDIDO */
public class PedidoEdit extends AppCompatActivity {

    public static final String PEDIDO_NOMBRE = "nombre";
    public static final String PEDIDO_TELEFONO = "telefono";
    public static final String PEDIDO_FECHAHORA = "fechaHora";
    public static final String PEDIDO_ESTADO = "estado";
    public static final String PEDIDO_NOMBRE_CLIENTE = "cliente";
    public static final String PEDIDO_ID = "id";
    public static final String PEDIDO_PRECIO = "precio";

    private EditText mNombreText;
    private EditText mTelefonoText;
    private EditText mFechaHoraText;
    private EditText mClienteText;
    private EditText mPrecioText;

    private Integer mRowId;

    Button mSaveButton;

    // Selector de categoría
    String[] estado = {"SOLICITADO", "PREPARADO", "RECOGIDO"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private int categoriaSeleccionada = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        mTelefonoText = findViewById(R.id.telefonoPEDIDO);
       // mPrecioText = findViewById(R.id.precioPEDIDO);
        mFechaHoraText = findViewById(R.id.fechaHoraPEDIDO);
        mClienteText = findViewById(R.id.clientePEDIDO);
        String nombrePEDIDO = mClienteText.getText().toString()+" - "+mFechaHoraText.getText().toString();
        TextView nombrePedidoTextView = findViewById(R.id.nombrePEDIDO);
        nombrePedidoTextView.setText(nombrePEDIDO);
        // Selector de estado
        autoCompleteTextView = findViewById(R.id.autocompletePEDIDO);
        adapterItems = new ArrayAdapter<String>(this, R.layout.text_item, estado);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
                categoriaSeleccionada = position;
            }
        });

        mSaveButton = findViewById(R.id.button_save);

        populateFields();

        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mNombreText.getText()) ||
                    TextUtils.isEmpty(mPrecioText.getText()) ||
                    categoriaSeleccionada == -1) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PedidoEdit.PEDIDO_NOMBRE, mNombreText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_NOMBRE_CLIENTE,mClienteText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_TELEFONO, mTelefonoText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_FECHAHORA, mFechaHoraText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_ESTADO, estado[categoriaSeleccionada]);
                replyIntent.putExtra(PedidoEdit.PEDIDO_NOMBRE_CLIENTE, mClienteText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_PRECIO, Double.parseDouble(mPrecioText.getText().toString()));
                if (mRowId != null) {
                    replyIntent.putExtra(PedidoEdit.PEDIDO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNombreText.setText(extras.getString(PedidoEdit.PEDIDO_NOMBRE));
            mTelefonoText.setText(extras.getString(PedidoEdit.PEDIDO_TELEFONO));
            mFechaHoraText.setText(extras.getString(PedidoEdit.PEDIDO_FECHAHORA));
            mClienteText.setText(extras.getString(PedidoEdit.PEDIDO_NOMBRE_CLIENTE));
            categoriaSeleccionada = extras.getInt(PedidoEdit.PEDIDO_ESTADO);
            autoCompleteTextView.setText(estado[categoriaSeleccionada], false);
            mPrecioText.setText(String.valueOf(extras.getDouble(PedidoEdit.PEDIDO_PRECIO)));
            mRowId = extras.getInt(PedidoEdit.PEDIDO_ID);
        }
    }
}
