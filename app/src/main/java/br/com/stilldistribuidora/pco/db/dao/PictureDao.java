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
import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;

public class PictureDao extends BaseConexao {
    private Conecao conn;
    private SQLiteDatabase banco;
    private  Context self=null;


    public PictureDao(Context context) {
        super( context );
        this.self=context;
//        conn = new Conecao(context);

    }
    @Override
    public long insert(Object obj) {
        PictureImageGrafica imagen = (PictureImageGrafica) obj;
        long ist=0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }

            ContentValues _values = new ContentValues();
            _values.put(Constante.WS_PCO_GRF_MV_FT_CREATE_AT, imagen.getCreate_at());
            _values.put(Constante.WS_PCO_GRF_MV_FT_PATH_FILE, imagen.getPath_file());
            _values.put(Constante.WS_PCO_GRF_MV_FT_NAME_FILE, imagen.getName_file());
            _values.put(Constante.WS_PCO_GRF_MV_FT_TB_RETIRADA_GF_CODIGO, imagen.getTb_retirada_gf_codigo());
            _values.put(Constante.WS_PCO_GRF_MV_FT_DEVICE, imagen.getDevice());
            _values.put(Constante.WS_PCO_GRF_MV_FT_USER, imagen.getUser());
            _values.put(Constante.WS_PCO_GRF_MV_FT_LOC, imagen.getLoc());
            _values.put(Constante.WS_PCO_GRF_MV_FT_SINCRONIZADA, imagen.getSincronizado());
            ist = stmt.insert(Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO, null, _values);
            stmt.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ist;
    }


    @Override
    public long update(Object obj) {
        ContentValues _values = new ContentValues();
        long ist=0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }
            PictureImageGrafica imagen = (PictureImageGrafica) obj;
            _values.put(Constante.WS_PCO_GRF_MV_FT_SINCRONIZADA, imagen.getSincronizado());
             ist = stmt.update(Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO, _values,
                    Constante.WS_PCO_GRF_MV_FT_CODIGO + "=" + imagen.getCodigo(), null);
            stmt.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ist;
    }


    @Override
    public long delete(Object obj) {
        long ist=0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }

            PictureImageGrafica imagen = (PictureImageGrafica) obj;
            ist = stmt.delete(Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO, Constante.WS_PCO_GRF_MV_FT_CODIGO + "=?", new String[]{imagen.getCodigo()});
            stmt.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ist;
    }
    public Cursor getCursor(String orderby, String where) {
        Cursor dados=null;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getReadableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getReadableDatabase();
            }

            dados = stmt.query(Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO, columns, where, null, null, null, orderby);

        } catch (Exception e) {
            throw new RuntimeException( e.getMessage() );
        }
        return   dados;
    }

    public ArrayList<PictureImageGrafica> getAll(String s) {

        ArrayList<PictureImageGrafica> _lista = new ArrayList<>();
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getReadableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getReadableDatabase();
            }

            Cursor dados = stmt.query(Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO, columns,
                    Constante.WS_PCO_GRF_MV_FT_TB_RETIRADA_GF_CODIGO+"=?",
                    new String[]{s}, null, null, null);

            Cursor _c = dados;
            if (_c.moveToFirst()) {
                do {
                    PictureImageGrafica imagens = new PictureImageGrafica();
                    imagens.setCodigo( _c.getString( 0 ) );
                    imagens.setCreate_at( _c.getString( 1 ) );
                    imagens.setPath_file( _c.getString( 2 ) );
                    imagens.setName_file( _c.getString( 3 ) );
                    imagens.setTb_retirada_gf_codigo( _c.getString( 4 ) );
                    imagens.setDevice( _c.getString( 5 ) );
                    imagens.setUser( _c.getString( 6 ) );
                    imagens.setLoc( _c.getString( 7 ) );
                    imagens.setSincronizado( _c.getString( 8 ) );
                    _lista.add( imagens );
                } while (_c.moveToNext());
            }
            _c.close();
            stmt.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _lista;
    }


    @Override
    public ArrayList<PictureImageGrafica> getAll() {

        ArrayList<PictureImageGrafica> _lista = new ArrayList<>();
        try {

            Cursor _c = getCursor( "", "" );
            if (_c.moveToFirst()) {
                do {
                    PictureImageGrafica imagens = new PictureImageGrafica();
                    imagens.setCodigo( _c.getString( 0 ) );
                    imagens.setCreate_at( _c.getString( 1 ) );
                    imagens.setPath_file( _c.getString( 2 ) );
                    imagens.setName_file( _c.getString( 3 ) );
                    imagens.setTb_retirada_gf_codigo( _c.getString( 4 ) );
                    imagens.setDevice( _c.getString( 5 ) );
                    imagens.setUser( _c.getString( 6 ) );
                    imagens.setLoc( _c.getString( 7 ) );
                    imagens.setSincronizado( _c.getString( 8 ) );
                    _lista.add( imagens );
                } while (_c.moveToNext());
            }
            _c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return _lista;
    }


    private String[] columns = new String[]{
            Constante.WS_PCO_GRF_MV_FT_CODIGO,
            Constante.WS_PCO_GRF_MV_FT_CREATE_AT,
            Constante.WS_PCO_GRF_MV_FT_PATH_FILE,
            Constante.WS_PCO_GRF_MV_FT_NAME_FILE,
            Constante.WS_PCO_GRF_MV_FT_TB_RETIRADA_GF_CODIGO,
            Constante.WS_PCO_GRF_MV_FT_DEVICE,
            Constante.WS_PCO_GRF_MV_FT_USER,
            Constante.WS_PCO_GRF_MV_FT_LOC,
            Constante.WS_PCO_GRF_MV_FT_SINCRONIZADA
    };


    public Object getById(String s) {


        Object _obj = null;
        List<Object> _lista = new ArrayList<Object>();


        try {
            _obj = getCursor("",s);
            Cursor _c = (Cursor) _obj;
            if (_c.moveToFirst()) {
                do {
                    PictureImageGrafica imagens = new PictureImageGrafica();
                    imagens.setCodigo( _c.getString( 0 ) );
                    imagens.setCreate_at( _c.getString( 1 ) );
                    imagens.setPath_file( _c.getString( 2 ) );
                    imagens.setName_file( _c.getString( 3 ) );
                    imagens.setTb_retirada_gf_codigo( _c.getString( 4 ) );
                    imagens.setDevice( _c.getString( 5 ) );
                    imagens.setUser( _c.getString( 6 ) );
                    imagens.setLoc( _c.getString( 7 ) );
                    imagens.setSincronizado( _c.getString( 8 ) );
                    _lista.add( imagens );
                } while (_c.moveToNext());
            }
            _c.close();

            return _lista.get( 0 );
        } catch (Exception e) {
            return _obj;
        }


    }
}
