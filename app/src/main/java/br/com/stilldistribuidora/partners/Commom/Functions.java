package br.com.stilldistribuidora.partners.Commom;

public class Functions {

    public static Double workExperience(String kmsExperience) {
       try{
         return Double.parseDouble(kmsExperience);
       }catch (Exception e){}
       return 0.0;
    }

    public static Double floor(Double value) {
        return Math.floor(value);
    }

    public static String[] split(String delimiter, String value) {
        return value.split(delimiter);
    }
}
