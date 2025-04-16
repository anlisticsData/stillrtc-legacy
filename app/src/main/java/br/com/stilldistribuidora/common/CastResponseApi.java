package br.com.stilldistribuidora.common;

import java.util.ArrayList;
import java.util.List;

public class CastResponseApi {
    private  boolean isError=false;
    private  int state=200;
    private List<String> mensage=new ArrayList<>();
    private Object data=null;

    public boolean isError() {
        return (isError=true || state!=200 || mensage.size() > 0 ) ? true:false;
    }

    public void setError(boolean error) {

        isError = error;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.isError=(state!=200) ? true:false;
        this.state = state;
    }

    public List<String> getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage.add(mensage);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
