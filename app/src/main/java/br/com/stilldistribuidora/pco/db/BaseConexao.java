package br.com.stilldistribuidora.pco.db;

import android.content.Context;

import java.util.ArrayList;

import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;

public class BaseConexao implements IActionBase {
   /*
    protected Conecao conn;
    protected SQLiteDatabase banco;
*/

    public BaseConexao(Context context){

    }


    @Override
    public long insert(Object obj) {
        return 0;
    }

    @Override
    public long update(Object obj) {
        return 0;
    }

    @Override
    public long delete(Object obj) {
        return 0;
    }

    @Override
    public ArrayList<PictureImageGrafica> getAll() {
        return null;
    }

    @Override
    public Object getBy(Object obj) {
        return null;
    }
}
