package br.com.stilldistribuidora.pco.db.model;

public class PictureImageGrafica {
    private String create_at;
    private String path_file;
    private String name_file;
    private String tb_retirada_gf_codigo;
    private String device;
    private String user;
    private String loc;
    private String sincronizado;


    private String codigo;

    public String getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }


    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public PictureImageGrafica(String path_file, String tb_retirada_gf_codigo, String device, String user, String loc) {
        this.path_file = path_file;
        this.tb_retirada_gf_codigo = tb_retirada_gf_codigo;
        this.device = device;
        this.user = user;
        this.loc = loc;
    }

    public PictureImageGrafica() {

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getPath_file() {
        return path_file;
    }

    public void setPath_file(String path_file) {
        this.path_file = path_file;
    }

    public String getTb_retirada_gf_codigo() {
        return tb_retirada_gf_codigo;
    }

    public void setTb_retirada_gf_codigo(String tb_retirada_gf_codigo) {
        this.tb_retirada_gf_codigo = tb_retirada_gf_codigo;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}