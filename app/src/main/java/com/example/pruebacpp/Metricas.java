 package com.example.pruebacpp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

 public class Metricas {
    public static String nombre = "";
    Runtime r=Runtime.getRuntime();
    public void wcsv(int id,long tamArch,int ende, int tamcla, double tiempo, double memoria ){
        File file = new File("/storage/emulated/0/Download/datos-"+((id == 0)?"texto":"archivos")+".csv");

        try {
                        // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nombre+","+tamArch+","+ ende+","+tamcla+","+tiempo+","+memoria);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            new Texto().imprimir(e.getMessage()+"");
            e.printStackTrace();
        }

    }

    public static float leer(int id, int col, int keysize, int ende){
        File file = new File("/storage/emulated/0/Download/datos-"+((id == 0)?"texto":"archivos")+".csv");
        int cont = 0;
        try
        {
            BufferedReader fin = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line = fin.readLine();
            float acum = 0;
            while (line != null){
                String[] linea = line.split(",");
                if (keysize == Integer.parseInt(linea[3]) && ende == Integer.parseInt(linea[2])) {
                    acum += Float.parseFloat(linea[col]);
                    cont++;
                }
                line = fin.readLine();
            }

            fin.close();
            return (cont != 0)?acum/cont:0;
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna "+ ex.getMessage());
        }
        return -1;
    }





}
