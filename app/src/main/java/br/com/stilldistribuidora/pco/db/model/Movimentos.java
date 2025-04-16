package br.com.stilldistribuidora.pco.db.model;


import java.io.Serializable;

public class Movimentos implements Serializable {

    private String mv_codigo;
    private String mt_codido;
    private String mt_nome;
    private String mt_formato;
    private String vm_codigo_versao;
    private String vm_versao;
    private String em_codigo;
    private String em_nome;
    private String em_img;
    private String cl_codigo;
    private String cl_nome;
    private String mv_qt_retirar;
    private String mv_create_at;
    private String mv_startus;
    private String mv_dt_retirada;
    private String mv_qt_retirada;
    private String mv_dt_entrega;
    private String mv_qt_entregue;
    private String mv_sincronizado;
    private String time;


    public String getVm_codigo_versao() {
        return vm_codigo_versao;
    }

    public void setVm_codigo_versao(String vm_codigo_versao) {
        this.vm_codigo_versao = vm_codigo_versao;
    }

    public String getMv_codigo() {
        return mv_codigo;
    }

    public void setMv_codigo(String mv_codigo) {
        this.mv_codigo = mv_codigo;
    }

    public String getMt_codido() {
        return mt_codido;
    }

    public void setMt_codido(String mt_codido) {
        this.mt_codido = mt_codido;
    }

    public String getMt_nome() {
        return mt_nome;
    }

    public void setMt_nome(String mt_nome) {
        this.mt_nome = mt_nome;
    }

    public String getMt_formato() {
        return mt_formato;
    }

    public void setMt_formato(String mt_formato) {
        this.mt_formato = mt_formato;
    }

    public String getVm_versao() {
        return vm_versao;
    }

    public void setVm_versao(String vm_versao) {
        this.vm_versao = vm_versao;
    }

    public String getEm_codigo() {
        return em_codigo;
    }

    public void setEm_codigo(String em_codigo) {
        this.em_codigo = em_codigo;
    }

    public String getEm_nome() {
        return em_nome;
    }

    public void setEm_nome(String em_nome) {
        this.em_nome = em_nome;
    }

    public String getEm_img() {
        return em_img;
    }

    public void setEm_img(String em_img) {
        this.em_img = em_img;
    }

    public String getCl_codigo() {
        return cl_codigo;
    }

    public void setCl_codigo(String cl_codigo) {
        this.cl_codigo = cl_codigo;
    }

    public String getCl_nome() {
        return cl_nome;
    }

    public void setCl_nome(String cl_nome) {
        this.cl_nome = cl_nome;
    }

    public String getMv_qt_retirar() {
        return mv_qt_retirar;
    }

    public void setMv_qt_retirar(String mv_qt_retirar) {
        this.mv_qt_retirar = mv_qt_retirar;
    }

    public String getMv_create_at() {
        return mv_create_at;
    }

    public void setMv_create_at(String mv_create_at) {
        this.mv_create_at = mv_create_at;
    }

    public String getMv_startus() {
        return mv_startus;
    }

    public void setMv_startus(String mv_startus) {
        this.mv_startus = mv_startus;
    }

    public String getMv_dt_retirada() {
        return mv_dt_retirada;
    }

    public void setMv_dt_retirada(String mv_dt_retirada) {
        this.mv_dt_retirada = mv_dt_retirada;
    }

    public String getMv_qt_retirada() {
        return mv_qt_retirada;
    }

    public void setMv_qt_retirada(String mv_qt_retirada) {
        this.mv_qt_retirada = mv_qt_retirada;
    }

    public String getMv_dt_entrega() {
        return mv_dt_entrega;
    }

    public void setMv_dt_entrega(String mv_dt_entrega) {
        this.mv_dt_entrega = mv_dt_entrega;
    }

    public String getMv_qt_entregue() {
        return mv_qt_entregue;
    }

    public void setMv_qt_entregue(String mv_qt_entregue) {
        this.mv_qt_entregue = mv_qt_entregue;
    }

    public String getMv_sincronizado() {
        return mv_sincronizado;
    }

    public void setMv_sincronizado(String mv_sincronizado) {
        this.mv_sincronizado = mv_sincronizado;
    }

    public void setMv_time_process(String time) {
        this.time=time;
    }

    public String getMv_time_process() {
        return this.time;
    }
}
