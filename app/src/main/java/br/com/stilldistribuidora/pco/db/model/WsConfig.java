package br.com.stilldistribuidora.pco.db.model;


//key_, descricao, opcao
public class WsConfig {
    private  int key_;
    private String descricao;
    private  String opcao;

    public WsConfig() {
    }

    public WsConfig(String descricao, String opcao) {
        this.descricao = descricao;
        this.opcao = opcao;
    }

    public WsConfig(String descricao) {
        this.descricao=descricao;

    }

    public int getKey_() {
        return key_;
    }

    public void setKey_(int key_) {
        this.key_ = key_;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }
}
