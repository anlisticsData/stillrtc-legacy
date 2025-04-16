package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;

public class RouterApiDirection implements Serializable {

    private String distancetext;
    private String distancevalue;
    private String durationtext;
    private String durationvalue;
    private String end_locationlat;
    private String end_locationlng;
    private String html_instructions;

    public RouterApiDirection(String distancetext, String distancevalue, String durationtext, String durationvalue, String end_locationlat, String end_locationlng, String html_instructions) {
        this.distancetext = distancetext;
        this.distancevalue = distancevalue;
        this.durationtext = durationtext;
        this.durationvalue = durationvalue;
        this.end_locationlat = end_locationlat;
        this.end_locationlng = end_locationlng;
        this.html_instructions = html_instructions;
    }

    public RouterApiDirection(String latitude, String logitude, String tipoInstrucao) {
        end_locationlat=latitude;
        end_locationlng=logitude;
        html_instructions=tipoInstrucao;
    }

    public String getDistancetext() {
        return distancetext;
    }

    public void setDistancetext(String distancetext) {
        this.distancetext = distancetext;
    }

    public String getDistancevalue() {
        return distancevalue;
    }

    public void setDistancevalue(String distancevalue) {
        this.distancevalue = distancevalue;
    }

    public String getDurationtext() {
        return durationtext;
    }

    public void setDurationtext(String durationtext) {
        this.durationtext = durationtext;
    }

    public String getDurationvalue() {
        return durationvalue;
    }

    public void setDurationvalue(String durationvalue) {
        this.durationvalue = durationvalue;
    }

    public String getEnd_locationlat() {
        return end_locationlat;
    }

    public void setEnd_locationlat(String end_locationlat) {
        this.end_locationlat = end_locationlat;
    }

    public String getEnd_locationlng() {
        return end_locationlng;
    }

    public void setEnd_locationlng(String end_locationlng) {
        this.end_locationlng = end_locationlng;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }
}