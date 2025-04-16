package br.com.stilldistribuidora.partners.Casts;

public class UserLoginCast {

    private String uuid;
    private String name;
    private String email;
    private String token;
    private String active;
    private AddressCast address;
    private String photo;
    private Niveis nivel;
    private String nivelId;
    private String lastname;
    private String cpf;
    private String mobilephone;
    private Conta conta;
    private Experiences experiences;

    public Experiences getExperiences() {
        return experiences;
    }

    public void setExperiences(Experiences experiences) {
        this.experiences = experiences;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNivelId() {
        return nivelId;
    }

    public void setNivelId(String nivelId) {
        this.nivelId = nivelId;
    }

    public Niveis getNivel() {
        return nivel;
    }

    public void setNivel(Niveis nivel) {
        this.nivel = nivel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public AddressCast getAddress() {
        return address;
    }

    public void setAddress(AddressCast address) {
        this.address = address;
    }
}
