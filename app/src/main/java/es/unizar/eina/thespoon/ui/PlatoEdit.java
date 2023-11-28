package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.thespoon.R;
import es.unizar.eina.thespoon.database.Plato;

/** Pantalla utilizada para la creación o edición de un plato */
public class PlatoEdit extends AppCompatActivity {

    public static final String PLATO_NOMBRE = "nombre";
    public static final String PLATO_DESCRIPCION = "descripcion";

    public static final String PLATO_CATEGORIA = "categoria";

    public static final String PLATO_PRECIO = "precio";
    public static final String PLATO_ID = "id";

    private EditText mNombreText;

    private EditText mDescripcionText;

    private EditText mPrecioText;

    private Integer mRowId;

    Button mSaveButton;

    // Selector de categoría
    String[] categoria = {"PRIMERO", "SEGUNDO", "POSTRE"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    private int categoriaSeleccionada = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_plato);

        mNombreText = findViewById(R.id.nombrePlato);
        mDescripcionText = findViewById(R.id.descripcionPlato);
        mPrecioText = findViewById(R.id.precioPlato);

        // Selector de categoría
        autoCompleteTextView = findViewById(R.id.autocompletePlato);
        adapterItems = new ArrayAdapter<String>(this, R.layout.text_item, categoria);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
                categoriaSeleccionada = position;
            }
        });

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mNombreText.getText()) ||
                    TextUtils.isEmpty(mPrecioText.getText()) ||
                    categoriaSeleccionada == -1) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PlatoEdit.PLATO_NOMBRE, mNombreText.getText().toString());
                replyIntent.putExtra(PlatoEdit.PLATO_DESCRIPCION, mDescripcionText.getText().toString());
                replyIntent.putExtra(PlatoEdit.PLATO_CATEGORIA, categoriaSeleccionada);
                replyIntent.putExtra(PlatoEdit.PLATO_PRECIO, Double.parseDouble(mPrecioText.getText().toString()));
                if (mRowId!=null) {
                    replyIntent.putExtra(PlatoEdit.PLATO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        populateFields();
    }

    private void populateFields () {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreText.setText(extras.getString(PlatoEdit.PLATO_NOMBRE));
            mDescripcionText.setText(extras.getString(PlatoEdit.PLATO_DESCRIPCION));
            categoriaSeleccionada = extras.getInt(PlatoEdit.PLATO_CATEGORIA);
            autoCompleteTextView.setText(categoria[categoriaSeleccionada], false);
            mPrecioText.setText(String.valueOf(extras.getDouble(PlatoEdit.PLATO_PRECIO)));
            mRowId = extras.getInt(PlatoEdit.PLATO_ID);
        }
    }
}
