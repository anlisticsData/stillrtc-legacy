package br.com.stilldistribuidora.pco.Interface;

import android.content.Context;

public interface iGui {
    public void processando(Context context,String title, String msn);
    public void messagenBox(Context context ,String Titulo, String menssagen, boolean cancelar) ;

}
