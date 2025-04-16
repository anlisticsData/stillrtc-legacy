package br.com.stilldistribuidora.stillrtc.db.models;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.Constants;

/**
 * Created by Still Technology and Development Team on 08/08/2018.
 */

public class SupervisorOperacao {
    public String operacao_id;
    public String avatar;
    public String loja;
    public String clienteLoja;
    public int quantideDispositivoOperacao;
    public String areaLimitOperation;
    public List<StatisticDispositivo> Dispositivos;
    public String statusOperacao;


    public SupervisorOperacao(String operacao_id, String avatar, String loja, String clinteLoja, int QtDispEmOperacao, String areaLimit, List<StatisticDispositivo> dispositivoOperation, String statusOperacao) {

        this.avatar = avatar;
        this.loja = loja;
        this.clienteLoja = clinteLoja;
        this.quantideDispositivoOperacao = QtDispEmOperacao;
        this.operacao_id = operacao_id;
        this.areaLimitOperation = areaLimit;
        this.Dispositivos = dispositivoOperation;
        this.statusOperacao = statusOperacao;

    }

    public String getToStringDispositivo() {
        String strDispositivo = "";
        int Total = 0;
        int next = 0;
        for (StatisticDispositivo dis : Dispositivos) {
            if (dis.operation.trim().equals(this.operacao_id)) {
                Total++;
            }
        }
        Total--;
        for (StatisticDispositivo dis : Dispositivos) {
            if (dis.operation.trim().equals(this.operacao_id)) {
                strDispositivo += dis.dispositivo;
                if ((next) < Total) {
                    strDispositivo += ",";
                }
                next++;
            }
        }
        return strDispositivo;
    }

    public String getDispositivoOsiosos() {
        String osiosoDispositivo = "";
        String[] horaOciosa = new String[3];
        int Total = 0;
        int next = 0;
        for (StatisticDispositivo dis : Dispositivos) {
            if (dis.operation.trim().equals(this.operacao_id) && Integer.valueOf(statusOperacao) == 1) {
                //Se Tive Ocioso..
                horaOciosa = dis.ociosidade.trim().split(":");
                if ((Integer.parseInt(horaOciosa[0]) - Constants.FUSO_HORA) > 0) {
                    osiosoDispositivo+=dis.dispositivo;
                    osiosoDispositivo+=",";
                } else if ((Integer.parseInt(horaOciosa[1])) >Constants.TEMPO_DISP_OSIOSO) {
                    osiosoDispositivo+=dis.dispositivo;
                    osiosoDispositivo+=",";
                }
                Total++;
            }
        }
        return osiosoDispositivo;
    }


}
