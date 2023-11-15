package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import es.unizar.eina.thespoon.R;

/** Pantalla utilizada para la creación o edición de una nota */
public class PlatoEdit extends AppCompatActivity {

    public static final String NOTE_TITLE = "title";
    public static final String NOTE_BODY = "body";
    public static final String NOTE_ID = "id";

    private EditText mTitleText;

    private EditText mBodyText;

    private Integer mRowId;

    Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_plato);

        mTitleText = findViewById(R.id.title);
        mBodyText = findViewById(R.id.body);

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mTitleText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(PlatoEdit.NOTE_TITLE, mTitleText.getText().toString());
                replyIntent.putExtra(PlatoEdit.NOTE_BODY, mBodyText.getText().toString());
                if (mRowId!=null) {
                    replyIntent.putExtra(PlatoEdit.NOTE_ID, mRowId.intValue());
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
            mTitleText.setText(extras.getString(PlatoEdit.NOTE_TITLE));
            mBodyText.setText(extras.getString(PlatoEdit.NOTE_BODY));
            mRowId = extras.getInt(PlatoEdit.NOTE_ID);
        }
    }

}
