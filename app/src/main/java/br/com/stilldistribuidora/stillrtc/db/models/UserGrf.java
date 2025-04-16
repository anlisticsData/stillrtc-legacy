package br.com.stilldistribuidora.stillrtc.db.models;

public class UserGrf {
    private static final long serialVersionUID = -2161110911377686463L;
    //key_, descricao, opcao
    private String tblName = "tb_usuario";

    private  String us_codigo;
    private  int us_perfil;
    private  String us_data;
    private  int us_retirada;

    public UserGrf(String us_codigo, int us_perfil, String us_data, int us_retirada) {
        this.us_codigo = us_codigo;
        this.us_perfil = us_perfil;
        this.us_data = us_data;
        this.us_retirada = us_retirada;
    }

    public UserGrf() {
    }

    public String getUs_codigo() {
        return us_codigo;
    }

    public void setUs_codigo(String us_codigo) {
        this.us_codigo = us_codigo;
    }

    public int getUs_perfil() {
        return us_perfil;
    }

    public void setUs_perfil(int us_perfil) {
        this.us_perfil = us_perfil;
    }

    public String getUs_data() {
        return us_data;
    }

    public void setUs_data(String us_data) {
        this.us_data = us_data;
    }

    public int getUs_retirada() {
        return us_retirada;
    }

    public void setUs_retirada(int us_retirada) {
        this.us_retirada = us_retirada;
    }
}
