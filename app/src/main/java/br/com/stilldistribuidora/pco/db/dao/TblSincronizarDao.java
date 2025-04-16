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
    import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
    import br.com.stilldistribuidora.pco.db.model.WsConfig;

    public class TblSincronizarDao extends BaseConexao {
        private Conecao conn;
        private SQLiteDatabase banco;
        private Context self=null;


        public TblSincronizarDao(Context context) {
            super(context);
            this.self=context;
           // conn=new Conecao(context);

        }

        @Override
        public long insert(Object obj) {
            long out=0;
            try {
                TblSincronizar sincroniza = (TblSincronizar) obj;
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getWritableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getWritableDatabase();
                }
                ContentValues parametros = new ContentValues();
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO, sincroniza.getCodigo_mv());
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_DT_CREATE, sincroniza.getDt_create());
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_STATUS, sincroniza.getStatus());
                 out = stmt.insert(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, null, parametros);
                stmt.close();
                db.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            return out;
        }

        @Override
        public Object getBy(Object obj) {
            TblSincronizar sincroniza =(TblSincronizar)obj;
            String opcao = "";
            try {
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getReadableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getReadableDatabase();
                }

                Cursor c = stmt.query(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, null, Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO.trim() + "=?",
                        new String[]{sincroniza.getCodigo_mv().trim()},
                        null,
                        null,
                        null,
                        null);
                if (c.moveToFirst()) {
                    opcao = c.getString(0) + "," + c.getString(1) + "," + c.getString(2) + "," + c.getString(4);
                }
                c.close();
                stmt.close();
                db.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return opcao;
        }


        public Object getByTime(Object obj) {
            TblSincronizar sincroniza =(TblSincronizar)obj;
            String opcao="";
            try {
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getReadableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getReadableDatabase();
                }

                Cursor c = stmt.query(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, null, Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO.trim() + "=?",
                        new String[]{sincroniza.getCodigo_mv().trim()},
                        null,
                        null,
                        null,
                        null);
                if (c.moveToFirst()) {
                    opcao = c.getString(2);
                }
                c.close();
                stmt.close();
                db.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return opcao;
        }


        public Object getByTimeCronometroUuid(Object obj) {
            String uuid =(String)obj;
            List<String> times=new ArrayList<>();
            String opcao = "";
            try {
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getReadableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getReadableDatabase();
                }
                Cursor c = stmt.query(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, null, Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO.trim() + "=?",
                        new String[]{uuid},
                        null,
                        null,
                        null,
                        null);

                if (c.moveToFirst()) {
                    do {
                        try{
                            times.add(c.getString(0) + "," + c.getString(1) + "," + c.getString(2));
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                    } while (c.moveToNext());
                }
                c.close();
                stmt.close();
                db.close();
            }catch (Exception e){}
            return times;
        }




        public Object getByTimeCronometro(Object obj) {
            TblSincronizar sincroniza =(TblSincronizar)obj;
            List<String> times=new ArrayList<>();
            try {
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getReadableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getReadableDatabase();
                }
                Cursor c = stmt.query(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, null, Constante.WS_PCO_GRF_MV_FT_SINC_STATUS.trim() + "=?",
                        new String[]{sincroniza.getStatus().trim()},
                        null,
                        null,
                        null,
                        null);

                if (c.moveToFirst()) {
                    do {
                        times.add(c.getString(0) + "," + c.getString(1) + "," + c.getString(2));
                    } while (c.moveToNext());
                }
                c.close();
                stmt.close();
                db.close();

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("");
            }

            return times;
        }


        @Override
        public long update(Object obj) {
            TblSincronizar sincronizar=(TblSincronizar)obj;
            long upd=0;
            try {
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getWritableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getWritableDatabase();
                }
                ContentValues parametros=new ContentValues(  );
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_DT_CREATE, sincronizar.getDt_create());
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_DT_FINISH, sincronizar.getDt_finish());
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_STATUS, sincronizar.getStatus());
                 upd = stmt.update(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, parametros,
                        Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO + "=?", new String[]{sincronizar.getCodigo_mv()});
                stmt.close();
                db.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return upd;

        }


        public long updateStatus(Object obj) {
            int upd=0;
            try {
                TblSincronizar sincronizar = (TblSincronizar) obj;
                Conecao db = new Conecao(self);
                SQLiteDatabase stmt = db.getWritableDatabase();
                if(!stmt.isOpen()){
                    db = new Conecao(self);
                    stmt = db.getWritableDatabase();
                }
                ContentValues parametros = new ContentValues();
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_STATUS, sincronizar.getStatus());
                parametros.put(Constante.WS_PCO_GRF_MV_FT_SINC_DT_CREATE, sincronizar.getDt_create());
                upd = stmt.update(Constante.BASE_PCO_TABLE_WS_SINCRONIZAR, parametros,
                        Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO + "=?", new String[]{sincronizar.getCodigo_mv()});

                stmt.close();
                db.close();
            }catch (Exception e){}
            return upd;
        }



    }
