package br.com.stilldistribuidora.stillrtc.db;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    public static final boolean Debug = true;
    private Context context;
    private SQLiteDatabase db;
    private String dbName;

    public void CreateDataBase(String script_createTb) throws IOException, ParserConfigurationException, SAXException {
        String XMLCriacao = script_createTb;
        String ScriptCriacao[] = XMLCriacao.split(";");

        File filedb = new File(
                Environment.getDataDirectory().toString() + "/data/com.acklay.frazy/databases/" + dbName);
        if (filedb.exists()) {
            try {
                filedb.delete();
            } catch (Exception e) {

            }
        }

        try {
            int qtdeScripts = ScriptCriacao.length;
            for (int i = 0; i < qtdeScripts; i++) {
                String sql = ScriptCriacao[i];
                db.execSQL(sql);
            }

            db = this.getWritableDatabase();
        } catch (Exception ex) {
            String g = ex.getMessage();
        }
    }

    public boolean verificarTabelaExiste(String NomeTabela) {
        boolean retorno = false;

        Cursor c = db.rawQuery(
                "select count(*) from sqlite_master where type='table' and name='" + NomeTabela.trim() + "'", null);
        c.moveToNext();
        if (c.getInt(0) > 0) {
            retorno = true;
        } else {
            retorno = false;
        }

        c.close();

        return retorno;
    }

    public DBHelper(Context context, String dbName) {
        //super(context, dbName, null, SmartCommand.getVersionCode(context));
        super(context, dbName, null, 1);
        this.context = context;
        this.dbName = dbName;
        if (db == null)
            db = context.openOrCreateDatabase(context.getDatabasePath(dbName).getPath(), Context.MODE_PRIVATE, null);
    }

    // Usado na camado de DataAccess
    public SQLiteDatabase getDb() {
        return db;
    }

    public Cursor queryWithCursor(String query) {

        try {

            Cursor cursor = db.rawQuery(query, null);
            if (Debug) {
                Log.d(TAG, "Executing Query: " + query);
            }

            cursor.close();

            return cursor;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        return null;
    }

    public void BeginTran() {
        db.beginTransaction();
    }

    public void CommitTran() {
        db.setTransactionSuccessful();
    }

    public void EndTran() {
        db.endTransaction();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}