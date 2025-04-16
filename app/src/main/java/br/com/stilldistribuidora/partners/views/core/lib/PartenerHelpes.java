package br.com.stilldistribuidora.partners.views.core.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.stilldistribuidora.Libs.Libs;

public class PartenerHelpes {

    private void Libs(){}

    public static String getDate(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy HHmmss");
        return formataData.format(new Date());

    }

    public static String createTransactionID(){
        try {
            return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }catch (Exception e){}
        return "";
    }


    public static boolean validateEmail(String email){
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }



    public static boolean isCPF(String CPF) {

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }




    public static Object parseJsonToObject(String json){
        try{
            return new JSONObject(json.trim());
        }catch (Exception e){
            try{
                return new JSONArray(json.trim());
            }catch (Exception e1){}
        }
        return null;
    }



    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File (Environment.getExternalStorageDirectory(),
                    "Notes" );
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File (root, sFileName);
            FileWriter writer = new FileWriter (gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }

        return false;
    }


    public static boolean     checkconnection(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
    public static boolean isOnlineApplication(Context context) {

        return   Libs.isNetworkConnected(context);


    }


    public static double getLatency(String ipAddress) {

        int NUMBER_OF_PACKTETS=5;
        String pingCommand = "/system/bin/ping -c " + NUMBER_OF_PACKTETS + " " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;

        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
                    break;
                }
                inputLine = bufferedReader.readLine();
            }

            if(inputLine !=null){
                // Extracting the average round trip time from the inputLine string
                String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
                String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
                String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
                avgRtt = Double.valueOf(strAvgRtt);
            }
        } catch (Exception e){

            e.printStackTrace();
        }


        return avgRtt;
    }


    public static double getLatencyGoogle() {
        return PartenerHelpes.getLatency("www.google.com.br");
    }
}
