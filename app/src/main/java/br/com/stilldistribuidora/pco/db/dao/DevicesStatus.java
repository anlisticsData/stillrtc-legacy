package br.com.stilldistribuidora.pco.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.BaseConexao;
import br.com.stilldistribuidora.pco.db.Conecao;
import br.com.stilldistribuidora.pco.db.model.DevicesModel;
import br.com.stilldistribuidora.pco.db.model.TblSincronizar;

public class DevicesStatus extends BaseConexao {
    private Conecao conn;
    private SQLiteDatabase banco;
    private Context self=null;

    public DevicesStatus(Context context) {
        super(context);
        conn = new Conecao(context);
        self=context;
    }

    @Override
    public long insert(Object obj) {
        DevicesModel sincroniza = (DevicesModel) obj;
        Conecao db = new Conecao(self);
        SQLiteDatabase stmt = db.getWritableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getWritableDatabase();
        }


        ContentValues parametros = new ContentValues();
        parametros.put(Constante.DEVICE_CODE_MOV_GRAFICA, sincroniza.getCod_mov());
        parametros.put(Constante.DEVICE_LAT_LON, sincroniza.getLat_log());
        parametros.put(Constante.DEVICE_DEVICE_ID, sincroniza.getDevice());
        parametros.put(Constante.DEVICE_USER, sincroniza.getUser());
        parametros.put(Constante.DEVICE_CREATE_AT, sincroniza.getCreated_at());
        parametros.put(Constante.DEVICE_STATUS, sincroniza.getStatus());
        long out = stmt.insert(Constante.DEVICE_TABLE_NAME, null, parametros);
        stmt.close();
        db.close();
        return out;
    }


    public List<String> getDevicesStatus(String status) {
        List<String> statusDevices = new ArrayList<>();
        try {

            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getReadableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getReadableDatabase();
            }

            String opcao = "";
            Cursor c = stmt.query(Constante.DEVICE_TABLE_NAME, null,Constante.DEVICE_STATUS.trim()+"=?",
                    new String[]{status.trim()},
                    null,
                    null,
                    null,
                    null);
            if (c.moveToFirst()) {
                do {
                    String key_ = String.valueOf(c.getInt(0));
                    String codigo = c.getString(1);
                    String lat_lon = c.getString(2);
                    String device = c.getString(3);
                    String user = c.getString(4);
                    opcao =key_+"|"+codigo+"|"+lat_lon+"|"+device+"|"+user;
                    statusDevices.add(opcao);
                } while (c.moveToNext());
            }
            c.close();
            stmt.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       return statusDevices;
    }

    public Boolean removeStatusDevices(String deviceStatus) {
        int upd = 0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }

            upd = stmt.delete(Constante.DEVICE_TABLE_NAME, Constante.DEVICE_STATUS_KEY + "=" + deviceStatus, null);
            stmt.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upd > 0;


    }


}


