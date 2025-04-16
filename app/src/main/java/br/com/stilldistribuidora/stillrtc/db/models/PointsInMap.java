package br.com.stilldistribuidora.stillrtc.db.models;

import java.util.List;

/**
 * Created by Still Technology and Development Team on 20/08/2018.
 */

public class PointsInMap {
    public String dispositivoId;
    public String dispositivoUltimaLocalizacao;
    public List<String> listDispositivo;
    public List<String> dispositivoOsiosos;
    public String operacao_status;

    public PointsInMap(String dispositivo, String ultPing, List<String> dispositivoOsiosos, List<String> listDispositivo, String operacao_status) {
        this.dispositivoId=dispositivo;
        this.dispositivoUltimaLocalizacao=ultPing;
        this.listDispositivo=listDispositivo;
        this.operacao_status=operacao_status;
        this.dispositivoOsiosos=dispositivoOsiosos;




    }
}
