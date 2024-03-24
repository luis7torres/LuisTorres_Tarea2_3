package com.example.luistorres_tarea2_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ListView;

import com.example.luistorres_tarea2_3.configuracion.Fotografia;
import com.example.luistorres_tarea2_3.configuracion.ListAdapter;
import com.example.luistorres_tarea2_3.configuracion.SQLiteConexion;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    List<Fotografia> mData = new ArrayList<>();
    ListAdapter mAdapter;
    SQLiteConexion conexion;

    Button btn_atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        conexion = new SQLiteConexion(this, null);

        listView = (ListView) findViewById(R.id.listView);
        obtenerTabla();
        mAdapter = new ListAdapter(ListActivity.this,mData);
        listView.setAdapter(mAdapter);
        btn_atras = (Button) findViewById(R.id.btnAtras);

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerTabla() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        mData.clear(); // Limpiar la lista antes de agregar nuevos elementos
        Cursor cursor = db.rawQuery(SQLiteConexion.SelectTablePhotos, null);

        //Recorremos el cursor
        while (cursor.moveToNext()) {
            Fotografia photograh = new Fotografia();
            photograh.setId(cursor.getString(0));
            photograh.setDescripcion(cursor.getString(2));
            mData.add(photograh);

            // Agregar logs para verificar los valores obtenidos
            Log.d("ListActivity", "ID: " + cursor.getString(0));
            Log.d("ListActivity", "Description: " + cursor.getString(2));
        }

        cursor.close(); // Cerrar el cursor después de usarlo

        // Notificar al adaptador que los datos han cambiado
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}