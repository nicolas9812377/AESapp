package com.example.pruebacpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Texto extends AppCompatActivity {
    public String TESTDATA="";
    public String CLAVE="";
    Metricas mc = new Metricas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);

        TextView txtres = findViewById(R.id.txtres);
        TextView txttexto = findViewById(R.id.txttexto);
        TextView txtclave = findViewById(R.id.txtclave);

        Spinner spin = findViewById(R.id.spinner);
        spin.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.keysize)));
        Spinner spmodo = findViewById(R.id.spmodo);
        spmodo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.modo)));

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TESTDATA = txttexto.getText().toString();
                CLAVE = txtclave.getText().toString();

                byte[] data = new byte[0];
                byte[] cl = new byte[0];
                int keysize = Integer.parseInt(spin.getSelectedItem().toString());
                int modo = spmodo.getSelectedItemPosition();

                try {
                    if(CLAVE.getBytes("UTF-8").length != (keysize/8)){
                        imprimir("La clave debe ser de " +(keysize/8)+ " bytes");
                        return;}

                    if(modo == 0) {
                        data = TESTDATA.getBytes("UTF-8");
                        cl = CLAVE.getBytes("UTF-8");

                        byte [] temp = AESCryptor.crypt(data,modo,cl,keysize);
                        double [] metricas = AESCryptor.memory();

                        txtres.setText(AESCryptor.bytes2HexStr(temp));
                        imprimir("Acabo");
                        mc.wcsv(0,data.length,modo,keysize,metricas[1],metricas[0]);
                    }else{
                        data = AESCryptor.hexStr2Bytes(TESTDATA);
                        cl = CLAVE.getBytes("UTF-8");

                        byte [] temp = AESCryptor.crypt(data,modo,cl,keysize);
                        double [] metricas = AESCryptor.memory();

                        txtres.setText(new String(temp,"UTF-8"));
                        imprimir("Acabo ");
                        mc.wcsv(0,data.length,modo,keysize,metricas[1],metricas[0]);
                    }
                } catch (UnsupportedEncodingException e) {
                    imprimir(e.getMessage()+"");
                    e.printStackTrace();
                }
                System.gc();
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
                    btn.setEnabled(true);
                }else {
                    txtclave.setError("La longitud debe ser: " + (keysize / 8) + " caracteres, tiene: "+count);
                    btn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void imprimir(String msg){ Toast.makeText(this," "+msg,Toast.LENGTH_LONG).show(); }

}