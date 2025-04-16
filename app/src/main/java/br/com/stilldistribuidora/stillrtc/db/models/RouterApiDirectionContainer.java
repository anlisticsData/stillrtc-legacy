package br.com.stilldistribuidora.stillrtc.db.models;

import java.util.List;

public class RouterApiDirectionContainer {
    private String duracaoText;
    private String duracaoValue;
    private String distanciaText;
    private String distanciaValue;
    private List<RouterApiDirection> routerApiDirection;

    public String getDuracaoText() {
        return duracaoText;
    }

    public void setDuracaoText(String duracaoText) {
        this.duracaoText = duracaoText;
    }

    public String getDuracaoValue() {
        return duracaoValue;
    }

    public void setDuracaoValue(String duracaoValue) {
        this.duracaoValue = duracaoValue;
    }

    public String getDistanciaText() {
        return distanciaText;
    }

    public void setDistanciaText(String distanciaText) {
        this.distanciaText = distanciaText;
    }

    public String getDistanciaValue() {
        return distanciaValue;
    }

    public void setDistanciaValue(String distanciaValue) {
        this.distanciaValue = distanciaValue;
    }

    public List<RouterApiDirection> getRouterApiDirection() {
        return routerApiDirection;
    }

    public void setRouterApiDirection(List<RouterApiDirection> routerApiDirection) {
        this.routerApiDirection = routerApiDirection;
    }
}
