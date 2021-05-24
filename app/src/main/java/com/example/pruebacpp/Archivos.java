package com.example.pruebacpp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Archivos extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    TextView txtpath;
    TextView txtclave;
    public String CLAVE="";
    Spinner spin;
    Spinner spmodo;
    Metricas mc = new Metricas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);
        txtpath = findViewById(R.id.txtpath);
        txtclave = findViewById(R.id.txtaclave);
        spin = findViewById(R.id.sptam);
        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.keysize)));
        spmodo = findViewById(R.id.spec);
        spmodo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.modo)));

        Button btnbuscar = findViewById(R.id.btnBuscar);
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //Verifica permisos para Android 6.0+
                    checkExternalStoragePermission();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File"), FILE_SELECT_CODE);
            }
        });

        Button btnaec = findViewById(R.id.btnaec);
        btnaec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLAVE = txtclave.getText().toString();
                int keysize = Integer.parseInt(spin.getSelectedItem().toString());
                int modo = spmodo.getSelectedItemPosition();
                File arch = new File(txtpath.getText().toString());
                try {
                    if(CLAVE.getBytes("UTF-8").length != (keysize/8)){
                        imprimir("La clave debe ser de " +(keysize/8)+ " bytes");
                        return;}

                    byte[] cl = CLAVE.getBytes("UTF-8");
                    byte[] temp = fullyReadFileToBytes(arch);


                    byte[] ed = AESCryptor.crypt(temp,modo,cl,keysize);
                    double [] metricas = AESCryptor.memory();

                    String[] listap = txtpath.getText().toString().split("\\.");
                    String end = (modo == 0)?" encriptado" : " desencriptado";
                    FileOutputStream fileOuputStream = new FileOutputStream(listap[0]+ end +"."+listap[1]);
                    fileOuputStream.write(ed);
                    fileOuputStream.close();

                    imprimir(((modo == 0)?"Encriptado" : "Desencriptado")+ " con exito \n "+listap[0]+end+"."+listap[1]);
                    mc.wcsv(1,temp.length,modo,keysize,metricas[1],metricas[0]);

                } catch (IOException e) {
                    imprimir(e.getMessage()+"");
                    e.printStackTrace();
                }
            }
        });

        txtclave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int keysize = Integer.parseInt(spin.getSelectedItem().toString());
                if( txtclave.getText().toString().length() == (keysize/8)) {
                    btnaec.setEnabled(true);
                }else {
                    txtclave.setError("La longitud debe ser: " + (keysize / 8) + " caracteres, tiene: "+count);
                    btnaec.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this.getBaseContext(), uri);
                    txtpath.setText(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void imprimir(String msg){ Toast.makeText(this," "+msg,Toast.LENGTH_SHORT).show(); }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            e.printStackTrace();
        } finally {
            fis.close();
        }

        return bytes;
    }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }

}