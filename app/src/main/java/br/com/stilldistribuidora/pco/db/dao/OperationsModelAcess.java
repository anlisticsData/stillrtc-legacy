package br.com.stilldistribuidora.pco.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import br.com.stilldistribuidora.Libs.Libs;
import br.com.stilldistribuidora.pco.Interface.MongoBaseAction;
import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.Conecao;
import br.com.stilldistribuidora.pco.db.model.Movimentos;
import br.com.stilldistribuidora.stillrtc.utils.Const;
public class OperationsModelAcess  {
    private Conecao conn;
    private SQLiteDatabase banco;
    private  Context self=null;
    public OperationsModelAcess(Context context){
        this.conn = new Conecao(context);
        this.self=context;

    }
    public Object insert(Object object) {
        long ist=0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getReadableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getReadableDatabase();
            }

            Cursor dados = null;
            Map<String, String> data = (Map) object;
            dados=stmt.rawQuery( "select * from  "+Constante.DOC_OPERACOES+" WHERE  "+Constante.DOC_OPERACOES_UUID_LANCAMENTO+"=trim('"+data.get("uuid")+"')", null );
            if(dados.getCount()==0){
                stmt = db.getWritableDatabase();
                ContentValues paramets = new ContentValues();
                paramets.put(Constante.DOC_OPERACOES_UUID, Libs.createTransactionID());
                paramets.put(Constante.DOC_OPERACOES_UUID_LANCAMENTO,data.get("uuid"));
                paramets.put(Constante.DOC_OPERACOES_STATUS,data.get("status"));
                paramets.put(Constante.DOC_OPERACOES_DATA, data.get("dt_busca"));
                paramets.put(Constante.DOC_OPERACOES_STRUCTS, data.get("structs"));
                paramets.put(Constante.DOC_OPERACOES_SINCRONED,"S");
                ist = stmt.insert(Constante.DOC_OPERACOES, null, paramets);
            }
            stmt.close();
            db.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ist;
    }
    public Object findBy(String uuid) {
        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getReadableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getReadableDatabase();
        }


        Cursor operations=null;
        List<Movimentos> movimentos = new ArrayList<>();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(String.format("select * from  %s ", Constante.DOC_OPERACOES));
            sql.append(String.format(" where %s='%s'", Constante.DOC_OPERACOES_UUID_LANCAMENTO,uuid));
            operations=stmt.rawQuery( sql.toString(), null );
            if(operations.getCount() > 0) {
                operations.moveToFirst();
                do{
                    String json= operations.getString(operations.getColumnIndex(Constante.DOC_OPERACOES_STRUCTS));
                    try{
                        JSONObject c = new JSONObject(json);
                        Movimentos mv = new Movimentos();
                        mv.setMv_codigo(c.getString(Constante.WS_PCO_GRF_MV_KEY));
                        mv.setMt_codido(c.getString(Constante.WS_PCO_GRF_MV_MT_KEY));
                        mv.setMt_nome(c.getString(Constante.WS_PCO_GRF_MV_MT_NOME));
                        mv.setMt_formato(c.getString(Constante.WS_PCO_GRF_MV_MT_FORMATO));
                        mv.setVm_versao(c.getString(Constante.WS_PCO_GRF_MV_VM_VERSAO));
                        mv.setVm_codigo_versao(c.getString(Constante.WS_PCO_GRF_VM_VERSION_CODE));
                        mv.setEm_nome(c.getString(Constante.WS_PCO_GRF_MV_EM_NOME));
                        mv.setEm_img(c.getString(Constante.WS_PCO_GRF_MV_EM_IMG));
                        mv.setCl_codigo(c.getString(Constante.WS_PCO_GRF_MV_CL_KEY));
                        mv.setCl_nome(c.getString(Constante.WS_PCO_GRF_MV_CL_NOME));
                        mv.setMv_qt_retirar(c.getString(Constante.WS_PCO_GRF_MV_QT_RT));
                        mv.setMv_create_at(c.getString(Constante.WS_PCO_GRF_MV_DT_CREATE));
                        mv.setMv_startus(c.getString(Constante.WS_PCO_GRF_MV_MV_STATUS));
                        mv.setMv_dt_retirada(c.getString(Constante.WS_PCO_GRF_MV_DT_RT));
                        mv.setMv_qt_retirada(c.getString(Constante.WS_PCO_GRF_MV_MV_QT_RT));
                        mv.setMv_dt_entrega(c.getString(Constante.WS_PCO_GRF_MV_DT_ENTREGA));
                        mv.setMv_qt_entregue(c.getString(Constante.WS_PCO_GRF_MV_QT_ENTREGE));
                        mv.setMv_time_process(c.getString(Constante.WS_PCO_GRF_MV_MT_TIME));
                        movimentos.add(mv);
                    }catch (Exception e){}
                } while (operations.moveToNext());
                operations.close();
            }

        }catch (Exception e){}
        stmt.close();
        db.close();

        return movimentos;
    }
    public Object findAll(Object object) {
        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getReadableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getReadableDatabase();
        }


        Cursor dados=null;
        int rowsFectch=0;
        Map<String,String> data=(Map)object;
        try {
             //dados = banco.query(Constante.DOC_OPERACOES, null,null, null, null, null, null);
            dados=stmt.rawQuery( "select * from "+Constante.DOC_OPERACOES+" WHERE data ='"+data.get("data")+"'", null );
            rowsFectch=dados.getCount();
        } catch (Exception e) {
            throw new RuntimeException( e.getMessage() );
        }
        stmt.close();
        db.close();
        return rowsFectch;
    }
    public Object operationsPaused(String uuid,String status,String time){
        List<Movimentos> mv=(List<Movimentos>) this.findBy(uuid);
        if(mv.size() > 0){
            mv.get(0).setMv_startus(status);
        }


        StringBuffer json = new StringBuffer();
        json.append("{");
        json.append(String.format("\"mv_codigo\":\"%s\"",mv.get(0).getMv_codigo()));
        json.append(",");
        json.append(String.format("\"mt_codido\":\"%s\"",mv.get(0).getMt_codido()));
        json.append(",");
        json.append(String.format("\"mt_nome\":\"%s\"",mv.get(0).getMt_nome()));
        json.append(",");
        json.append(String.format("\"mt_formato\":\"%s\"",mv.get(0).getMt_formato()));
        json.append(",");
        json.append(String.format("\"vm_versao\":\"%s\"",mv.get(0).getVm_versao()));
        json.append(",");
        json.append(String.format("\"vm_codigo\":\"%s\"",mv.get(0).getVm_codigo_versao()));
        json.append(",");
        json.append(String.format("\"em_codigo\":\"%s\"",mv.get(0).getEm_codigo()));
        json.append(",");
        json.append(String.format("\"em_nome\":\"%s\"",mv.get(0).getEm_nome()));
        json.append(",");
        json.append(String.format("\"em_img\":\"%s\"",mv.get(0).getEm_img()));
        json.append(",");
        json.append(String.format("\"cl_codigo\":\"%s\"",mv.get(0).getCl_codigo()));
        json.append(",");
        json.append(String.format("\"cl_nome\":\"%s\"",mv.get(0).getCl_nome()));
        json.append(",");
        json.append(String.format("\"mv_qt_retirar\":\"%s\"",mv.get(0).getMv_qt_retirar()));
        json.append(",");
        json.append(String.format("\"mv_create_at\":\"%s\"",mv.get(0).getMv_create_at()));
        json.append(",");
        json.append(String.format("\"mv_startus\":\"%s\"",mv.get(0).getMv_startus()));
        json.append(",");
        json.append(String.format("\"mv_dt_retirada\":\"%s\"",mv.get(0).getMv_dt_retirada()));
        json.append(",");
        json.append(String.format("\"mv_qt_retirada\":\"%s\"",mv.get(0).getMv_qt_retirada()));
        json.append(",");
        json.append(String.format("\"mv_dt_entrega\":\"%s\"",mv.get(0).getMv_dt_entrega()));
        json.append(",");
        json.append(String.format("\"mv_qt_entregue\":\"%s\"",mv.get(0).getMv_qt_entregue()));
        json.append(",");
        json.append(String.format("\"mv_sincronizado\":\"%s\"",mv.get(0).getMv_sincronizado()));
        json.append(",");
        json.append(String.format("\"mv_time\":\"%s\"",time));
        json.append("}");
        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getWritableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getWritableDatabase();
        }


        ContentValues paramets = new ContentValues();
        paramets.put(Constante.DOC_OPERACOES_STATUS,status);
        paramets.put(Constante.DOC_OPERACOES_STRUCTS, json.toString());
       // paramets.put(Constante.DOC_OPERACOES_SINCRONED,"N");
        int upd = stmt.update(Constante.DOC_OPERACOES, paramets,
                Constante.DOC_OPERACOES_UUID_LANCAMENTO + "=?", new String[]{uuid});
        stmt.close();
        db.close();
        return upd;
    }
    public Object operationsStartOperations(String uuid,String status,String quantidade,String dataFormatada){
        List<Movimentos> mv=(List<Movimentos>) this.findBy(uuid);
        if(mv.size() > 0){
            mv.get(0).setMv_startus(status);
        }


        StringBuffer json = new StringBuffer();
        json.append("{");
        json.append(String.format("\"mv_codigo\":\"%s\"",mv.get(0).getMv_codigo()));
        json.append(",");
        json.append(String.format("\"mt_codido\":\"%s\"",mv.get(0).getMt_codido()));
        json.append(",");
        json.append(String.format("\"mt_nome\":\"%s\"",mv.get(0).getMt_nome()));
        json.append(",");
        json.append(String.format("\"mt_formato\":\"%s\"",mv.get(0).getMt_formato()));
        json.append(",");
        json.append(String.format("\"vm_versao\":\"%s\"",mv.get(0).getVm_versao()));
        json.append(",");
        json.append(String.format("\"vm_codigo\":\"%s\"",mv.get(0).getVm_codigo_versao()));
        json.append(",");
        json.append(String.format("\"em_codigo\":\"%s\"",mv.get(0).getEm_codigo()));
        json.append(",");
        json.append(String.format("\"em_nome\":\"%s\"",mv.get(0).getEm_nome()));
        json.append(",");
        json.append(String.format("\"em_img\":\"%s\"",mv.get(0).getEm_img()));
        json.append(",");
        json.append(String.format("\"cl_codigo\":\"%s\"",mv.get(0).getCl_codigo()));
        json.append(",");
        json.append(String.format("\"cl_nome\":\"%s\"",mv.get(0).getCl_nome()));
        json.append(",");
        json.append(String.format("\"mv_qt_retirar\":\"%s\"",mv.get(0).getMv_qt_retirar()));
        json.append(",");
        json.append(String.format("\"mv_create_at\":\"%s\"",mv.get(0).getMv_create_at()));
        json.append(",");
        json.append(String.format("\"mv_startus\":\"%s\"",status));
        json.append(",");
        json.append(String.format("\"mv_dt_retirada\":\"%s\"",dataFormatada));
        json.append(",");
        json.append(String.format("\"mv_qt_retirada\":\"%s\"",quantidade));
        json.append(",");
        json.append(String.format("\"mv_dt_entrega\":\"%s\"",mv.get(0).getMv_dt_entrega()));
        json.append(",");
        json.append(String.format("\"mv_qt_entregue\":\"%s\"",mv.get(0).getMv_qt_entregue()));
        json.append(",");
        json.append(String.format("\"mv_sincronizado\":\"%s\"","1"));
        json.append(",");
        json.append(String.format("\"mv_time\":\"%s\"",mv.get(0).getMv_time_process()));
        json.append("}");
        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getWritableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getWritableDatabase();
        }

        ContentValues paramets = new ContentValues();
        paramets.put(Constante.DOC_OPERACOES_STATUS,status);
        paramets.put(Constante.DOC_OPERACOES_STRUCTS, json.toString());
      //  paramets.put(Constante.DOC_OPERACOES_SINCRONED,"N");
        int upd = stmt.update(Constante.DOC_OPERACOES, paramets,
                Constante.DOC_OPERACOES_UUID_LANCAMENTO + "=?", new String[]{uuid});

        stmt.close();
        db.close();
        return upd;
    }
    public Object operationsFinishOperations(String uuid,String status,String dataFinish){
        List<Movimentos> mv=(List<Movimentos>) this.findBy(uuid);
        if(mv.size() > 0){
            mv.get(0).setMv_startus(status);
        }


        StringBuffer json = new StringBuffer();
        json.append("{");
        json.append(String.format("\"mv_codigo\":\"%s\"",mv.get(0).getMv_codigo()));
        json.append(",");
        json.append(String.format("\"mt_codido\":\"%s\"",mv.get(0).getMt_codido()));
        json.append(",");
        json.append(String.format("\"mt_nome\":\"%s\"",mv.get(0).getMt_nome()));
        json.append(",");
        json.append(String.format("\"mt_formato\":\"%s\"",mv.get(0).getMt_formato()));
        json.append(",");
        json.append(String.format("\"vm_versao\":\"%s\"",mv.get(0).getVm_versao()));
        json.append(",");
        json.append(String.format("\"vm_codigo\":\"%s\"",mv.get(0).getVm_codigo_versao()));
        json.append(",");
        json.append(String.format("\"em_codigo\":\"%s\"",mv.get(0).getEm_codigo()));
        json.append(",");
        json.append(String.format("\"em_nome\":\"%s\"",mv.get(0).getEm_nome()));
        json.append(",");
        json.append(String.format("\"em_img\":\"%s\"",mv.get(0).getEm_img()));
        json.append(",");
        json.append(String.format("\"cl_codigo\":\"%s\"",mv.get(0).getCl_codigo()));
        json.append(",");
        json.append(String.format("\"cl_nome\":\"%s\"",mv.get(0).getCl_nome()));
        json.append(",");
        json.append(String.format("\"mv_qt_retirar\":\"%s\"",mv.get(0).getMv_qt_retirar()));
        json.append(",");
        json.append(String.format("\"mv_create_at\":\"%s\"",mv.get(0).getMv_create_at()));
        json.append(",");
        json.append(String.format("\"mv_startus\":\"%s\"",status));
        json.append(",");
        json.append(String.format("\"mv_dt_retirada\":\"%s\"",mv.get(0).getMv_dt_retirada()));
        json.append(",");
        json.append(String.format("\"mv_qt_retirada\":\"%s\"",mv.get(0).getMv_qt_retirada()));
        json.append(",");
        json.append(String.format("\"mv_dt_entrega\":\"%s\"",dataFinish));
        json.append(",");
        json.append(String.format("\"mv_qt_entregue\":\"%s\"",mv.get(0).getMv_qt_retirada()));
        json.append(",");
        json.append(String.format("\"mv_sincronizado\":\"%s\"","1"));
        json.append(",");
        json.append(String.format("\"mv_time\":\"%s\"",mv.get(0).getMv_time_process()));
        json.append("}");

        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getWritableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getWritableDatabase();
        }

        ContentValues paramets = new ContentValues();
        paramets.put(Constante.DOC_OPERACOES_STATUS,status);
        paramets.put(Constante.DOC_OPERACOES_STRUCTS, json.toString());
        paramets.put(Constante.DOC_OPERACOES_SINCRONED,"N");
        int upd = stmt.update(Constante.DOC_OPERACOES, paramets,
                Constante.DOC_OPERACOES_UUID_LANCAMENTO + "=?", new String[]{uuid});
        stmt.close();
        db.close();
        return upd;
    }
    public Object listOperationByStatusAndDate(String dataSelecionada,String statusSelecionado){

        Conecao db = new Conecao(self);
        SQLiteDatabase  stmt=db.getReadableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getReadableDatabase();
        }


        Cursor operations=null;
        List<Movimentos> movimentos = new ArrayList<>();
        try {
            StringBuffer sql= new StringBuffer();
            sql.append(String.format("select * from  %s ", Constante.DOC_OPERACOES));
            sql.append(String.format(" where %s='%s'",Constante.DOC_OPERACOES_DATA,dataSelecionada));
            if(!statusSelecionado.equals("G")){
                sql.append(String.format(" and %s='%s'" ,Constante.DEVICE_STATUS,statusSelecionado));
            }

            operations=stmt.rawQuery( sql.toString(), null );
            if(operations.getCount() > 0){
                operations.moveToFirst();
                do {
                    String json= operations.getString(operations.getColumnIndex(Constante.DOC_OPERACOES_STRUCTS));
                    try{
                        JSONObject c = new JSONObject(json);
                        Movimentos mv = new Movimentos();
                        mv.setMv_codigo(c.getString(Constante.WS_PCO_GRF_MV_KEY));
                        mv.setMt_codido(c.getString(Constante.WS_PCO_GRF_MV_MT_KEY));
                        mv.setMt_nome(c.getString(Constante.WS_PCO_GRF_MV_MT_NOME));
                        mv.setMt_formato(c.getString(Constante.WS_PCO_GRF_MV_MT_FORMATO));
                        mv.setVm_versao(c.getString(Constante.WS_PCO_GRF_MV_VM_VERSAO));
                        mv.setEm_nome(c.getString(Constante.WS_PCO_GRF_MV_EM_NOME));
                        mv.setEm_img(c.getString(Constante.WS_PCO_GRF_MV_EM_IMG));
                        mv.setCl_codigo(c.getString(Constante.WS_PCO_GRF_MV_CL_KEY));
                        mv.setCl_nome(c.getString(Constante.WS_PCO_GRF_MV_CL_NOME));
                        mv.setMv_qt_retirar(c.getString(Constante.WS_PCO_GRF_MV_QT_RT));
                        mv.setMv_create_at(c.getString(Constante.WS_PCO_GRF_MV_DT_CREATE));
                        mv.setMv_startus(c.getString(Constante.WS_PCO_GRF_MV_MV_STATUS));
                        mv.setMv_dt_retirada(c.getString(Constante.WS_PCO_GRF_MV_DT_RT));
                        mv.setMv_qt_retirada(c.getString(Constante.WS_PCO_GRF_MV_MV_QT_RT));
                        mv.setMv_dt_entrega(c.getString(Constante.WS_PCO_GRF_MV_DT_ENTREGA));
                        mv.setMv_qt_entregue(c.getString(Constante.WS_PCO_GRF_MV_QT_ENTREGE));
                        mv.setMv_time_process(c.getString(Constante.WS_PCO_GRF_MV_MT_TIME));
                        mv.setMv_sincronizado(c.getString(Constante.WS_PCO_GRF_MV_MT_SINCRONIZADO));

                        movimentos.add(mv);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (operations.moveToNext());
                operations.close();
                stmt.close();
                db.close();


            }

        } catch (Exception e) {
            throw new RuntimeException( e.getMessage() );

        }
        return movimentos;
    }
    public Object delete(String uuid) {
        return null;
    }



    public Object nonSynchronizedOperations(String status) {
        Conecao db = new Conecao(self);
        SQLiteDatabase stmt = db.getReadableDatabase();
        if(!stmt.isOpen()){
            db = new Conecao(self);
            stmt = db.getReadableDatabase();
        }


        Cursor operations=null;
        List<Movimentos> movimentos = new ArrayList<>();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(String.format("select * from  %s ", Constante.DOC_OPERACOES));
            sql.append(String.format(" where %s='%s'", Constante.DOC_OPERACOES_SINCRONED,status));
            operations=stmt.rawQuery( sql.toString(), null );
            if(operations.getCount() > 0) {
                operations.moveToFirst();
                do{
                    String json= operations.getString(operations.getColumnIndex(Constante.DOC_OPERACOES_STRUCTS));
                    try{
                        JSONObject c = new JSONObject(json);
                        Movimentos mv = new Movimentos();
                        mv.setMv_codigo(c.getString(Constante.WS_PCO_GRF_MV_KEY));
                        mv.setMt_codido(c.getString(Constante.WS_PCO_GRF_MV_MT_KEY));
                        mv.setMt_nome(c.getString(Constante.WS_PCO_GRF_MV_MT_NOME));
                        mv.setMt_formato(c.getString(Constante.WS_PCO_GRF_MV_MT_FORMATO));
                        mv.setVm_versao(c.getString(Constante.WS_PCO_GRF_MV_VM_VERSAO));
                        mv.setVm_codigo_versao(c.getString(Constante.WS_PCO_GRF_VM_VERSION_CODE));
                        mv.setEm_nome(c.getString(Constante.WS_PCO_GRF_MV_EM_NOME));
                        mv.setEm_img(c.getString(Constante.WS_PCO_GRF_MV_EM_IMG));
                        mv.setCl_codigo(c.getString(Constante.WS_PCO_GRF_MV_CL_KEY));
                        mv.setCl_nome(c.getString(Constante.WS_PCO_GRF_MV_CL_NOME));
                        mv.setMv_qt_retirar(c.getString(Constante.WS_PCO_GRF_MV_QT_RT));
                        mv.setMv_create_at(c.getString(Constante.WS_PCO_GRF_MV_DT_CREATE));
                        mv.setMv_startus(c.getString(Constante.WS_PCO_GRF_MV_MV_STATUS));
                        mv.setMv_dt_retirada(c.getString(Constante.WS_PCO_GRF_MV_DT_RT));
                        mv.setMv_qt_retirada(c.getString(Constante.WS_PCO_GRF_MV_MV_QT_RT));
                        mv.setMv_dt_entrega(c.getString(Constante.WS_PCO_GRF_MV_DT_ENTREGA));
                        mv.setMv_qt_entregue(c.getString(Constante.WS_PCO_GRF_MV_QT_ENTREGE));
                        mv.setMv_time_process(c.getString(Constante.WS_PCO_GRF_MV_MT_TIME));
                        movimentos.add(mv);
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("d");
                    }
                } while (operations.moveToNext());
            //    operations.close();
              //  stmt.close();
               // db.close();

            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("d");
        }

        return movimentos;
    }
    public Object operationsUpdateSynchronized(String uuid){
        int upd=0;
        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }


            ContentValues paramets = new ContentValues();
            paramets.put(Constante.DOC_OPERACOES_SINCRONED, "S");
            upd = stmt.update(Constante.DOC_OPERACOES, paramets,
                    Constante.DOC_OPERACOES_UUID_LANCAMENTO + "=?", new String[]{uuid});
            stmt.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return upd;
    }

    public Object operationsUpdateNotSynchronized(String uuid){
        int upd=0;

        try {
            Conecao db = new Conecao(self);
            SQLiteDatabase stmt = db.getWritableDatabase();
            if(!stmt.isOpen()){
                db = new Conecao(self);
                stmt = db.getWritableDatabase();
            }


            ContentValues paramets = new ContentValues();
            paramets.put(Constante.DOC_OPERACOES_SINCRONED, "N");
            upd = stmt.update(Constante.DOC_OPERACOES, paramets,
                    Constante.DOC_OPERACOES_UUID_LANCAMENTO + "=?", new String[]{uuid});
            stmt.close();
            db.close();

        }catch (Exception e){
            e.printStackTrace();
        }


        return upd;
    }







}
