package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;

public class PaternsModel implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMobliePhone() {
        return mobliePhone;
    }

    public void setMobliePhone(String mobliePhone) {
        this.mobliePhone = mobliePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String name;
    private String lastName;
    private String cpf;
    private String mobliePhone;
    private String email;
    private String password;
    private String address;
    private String pixJson;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPixJson() {
        return pixJson;
    }

    public void setPixJson(String pixJson) {
        this.pixJson = pixJson;
    }
}
