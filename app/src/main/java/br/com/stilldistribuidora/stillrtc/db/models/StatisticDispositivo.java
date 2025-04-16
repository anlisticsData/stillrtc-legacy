package br.com.stilldistribuidora.stillrtc.db.models;

/**
 * Created by Still Technology and Development Team on 15/08/2018.
 */

public class StatisticDispositivo {
    public String dispositivo;
    public String ociosidade;
    public String operation;


    public StatisticDispositivo(String dispositivo, String ociosidade,String operation) {
        this.dispositivo = dispositivo;
        this.ociosidade = ociosidade;
        this.operation=operation;

    }
}
