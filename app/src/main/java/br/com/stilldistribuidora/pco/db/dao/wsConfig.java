package br.com.stilldistribuidora.pco.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.BaseConexao;
import br.com.stilldistribuidora.pco.db.Conecao;
import br.com.stilldistribuidora.pco.db.model.WsConfig;

public class wsConfig extends BaseConexao {
    private Conecao conn;
    private SQLiteDatabase banco;

    public wsConfig(Context context) {
       super( context );
       conn = new Conecao(context);


    }



    @Override
    public long insert(Object obj) {
        WsConfig config =(WsConfig)obj;
        banco = conn.getWritableDatabase();
        ContentValues  parametros=  new ContentValues(  );
        parametros.put( Constante.WS_DESCRICAO,config.getDescricao());
        parametros.put( Constante.WS_OPCAO,config.getOpcao() );
        long out= banco.insert(Constante.BASE_PCO_TABLE_WS_CONFIG, null, parametros);
        conn.close();

        return out;
    }

    @Override
    public long update(Object obj) {
        WsConfig wsconfig=(WsConfig)obj;
        banco = conn.getWritableDatabase();
        ContentValues parametros=new ContentValues(  );
        parametros.put( Constante.WS_OPCAO,wsconfig.getOpcao() );
        int upd = banco.update(Constante.BASE_PCO_TABLE_WS_CONFIG, parametros, Constante.WS_DESCRICAO + "=?", new String[]{wsconfig.getDescricao()});
        conn.close();


        return upd;

    }

    @Override
    public long delete(Object obj) {
        banco = conn.getWritableDatabase();
        WsConfig config = (WsConfig)obj;
        int dlt = banco.delete(Constante.BASE_PCO_TABLE_WS_CONFIG, Constante.WS_KEY + "=?", new String[]{String.valueOf(config.getKey_())});
        conn.close();
        return dlt;
    }

    @Override
    public Object getBy(Object obj) {
        WsConfig config =(WsConfig)obj;
        banco = conn.getReadableDatabase();
        String opcao="";
        Cursor c = banco.query( Constante.BASE_PCO_TABLE_WS_CONFIG,
                new String[]{Constante.WS_KEY,Constante.WS_OPCAO.trim()},Constante.WS_DESCRICAO.trim()+"=?",
                new String[]{config.getDescricao().trim()},
                null,
                null,
                null,
                null);
        if (c.moveToFirst()) {

            opcao = c.getString(1);
        }
        c.close();
        conn.close();
        return opcao;
    }


    public Object getAll(Object obj) {
        WsConfig config =(WsConfig)obj;
        banco = conn.getWritableDatabase();
        String opcao="";
        Cursor c = banco.query( Constante.BASE_PCO_TABLE_WS_CONFIG,null,null,null,
                null,
                null,
                null,
                null);



        c.close();
        conn.close();

        return opcao;
    }

}
