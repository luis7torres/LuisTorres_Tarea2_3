package com.example.luistorres_tarea2_3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luistorres_tarea2_3.configuracion.SQLiteConexion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SQLiteConexion conexion;

    EditText txt_descripcion;
    Button btn_foto, btn_salvar, btn_lista;
    ImageView imagenCapturada;
    static final  int REQUEST_IMAGE = 101;
    static final  int PETICION_ACCESS_CAM = 201;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_descripcion = (EditText) findViewById(R.id.txtDescripcion);
        btn_foto = (Button) findViewById(R.id.btnFoto);
        btn_salvar = (Button) findViewById(R.id.btnSalvar);
        btn_lista = (Button) findViewById(R.id.btnLista);
        imagenCapturada = (ImageView) findViewById(R.id.imagenCaputar);

        //Evento para el boton de Foto
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        //Evento para el boton de Salvar
        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String datoDescripcion = txt_descripcion.getText().toString();
                if(datoDescripcion.isEmpty()){
                    txt_descripcion.setError("Ingrese la descripcion de la imagen");
                }else{
                    guardarFotos();
                }
            }
        });

        //Evento para el boton de Lista
        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);

            }
        });
    }

    private void permisos(){
        // METODO PARA LOS PERMISOS DE LA APLICACION
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},PETICION_ACCESS_CAM);
        }
        else
        {
            dispatchTakePictureIntent();
            //TomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==  PETICION_ACCESS_CAM){
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }{
                Toast.makeText(getApplicationContext(),"Se necesita permiso de la camara",Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE){
              //Bundle extras = data.getExtras();
              //Bitmap imagen = (Bitmap) extras.get("data");
              //Objetoimagen.setImageBitmap(imagen);
            try {
                File foto = new File(currentPhotoPath);
                imagenCapturada.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.luistorres_tarea2_3",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use  with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String convertImage64(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imagearray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagearray,Base64.DEFAULT);
    }



    public void guardarFotos() {
        conexion = new SQLiteConexion(this,null);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteConexion.columnphoto, convertImage64(currentPhotoPath));
        values.put(SQLiteConexion.columndescription, txt_descripcion.getText().toString());
        db.insert(SQLiteConexion.tableName, null, values);
        Toast.makeText(getApplicationContext(), "Registro ingresado",Toast.LENGTH_LONG).show();
        CleanScreen();
        db.close();
    }
    private void CleanScreen() {
        imagenCapturada.setImageResource(R.drawable.profiledefault);
        txt_descripcion.setText("");
    }
}