package br.com.stilldistribuidora.pco.db.model;

public class TblSincronizar {
    private  int key_;
    private String codigo_mv;
    private  String dt_create;
    private  String dt_finish;
    private  String status;

    public int getKey_() {
        return key_;
    }

    public void setKey_(int key_) {
        this.key_ = key_;
    }

    public String getCodigo_mv() {
        return codigo_mv;
    }

    public void setCodigo_mv(String codigo_mv) {
        this.codigo_mv = codigo_mv;
    }

    public String getDt_create() {
        return dt_create;
    }

    public void setDt_create(String dt_create) {
        this.dt_create = dt_create;
    }

    public String getDt_finish() {
        return dt_finish;
    }

    public void setDt_finish(String dt_finish) {
        this.dt_finish = dt_finish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
