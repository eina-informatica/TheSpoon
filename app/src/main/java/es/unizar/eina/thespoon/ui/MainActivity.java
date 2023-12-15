package es.unizar.eina.thespoon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.unizar.eina.thespoon.R;

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

        Button goToPedidosButton = findViewById(R.id.goToPedidos);
        goToPedidosButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                // Código para iniciar la actividad "Pedidos"
                Intent intent = new Intent(MainActivity.this, Pedidos.class);
                startActivity(intent);
            }
        });

        TextView goToTestsButton = findViewById(R.id.goToTests);
        goToTestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Código para iniciar la actividad "Tests"
                Intent intent = new Intent(MainActivity.this, Tests.class);
                startActivity(intent);
            }
        });
    }
}