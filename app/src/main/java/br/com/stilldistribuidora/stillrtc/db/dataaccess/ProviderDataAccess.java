package br.com.stilldistribuidora.stillrtc.db.dataaccess;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */


abstract class ProviderDataAccess {

    protected DBHelper db;
    protected Context context;

    public abstract long insert(Object obj);

    public abstract long update(Object obj, String where);

    public abstract void delete(String where);

    public abstract Cursor getCursor(String orderby, String where);

    public abstract Object getById(String where);

    public abstract List<?> getList(String where, String orderby);
}