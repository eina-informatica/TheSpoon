package es.unizar.eina.thespoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.unizar.eina.thespoon.ui.Platos;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToPlatosButton = findViewById(R.id.goToPlatos);
        goToPlatosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Código para iniciar la actividad "Platos"
                Intent intent = new Intent(MainActivity.this, Platos.class);
                startActivity(intent);
            }
        });
    }
}