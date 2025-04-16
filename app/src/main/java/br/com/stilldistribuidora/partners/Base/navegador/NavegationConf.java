package br.com.stilldistribuidora.partners.Base.navegador;

public class NavegationConf {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS  " + Navegation.NavegationEntry.TABLE_NAME + " (" +
                    Navegation.NavegationEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Navegation.NavegationEntry.COLUMN_NAME_PROPS + " TEXT," +
                    Navegation.NavegationEntry.COLUMN_NAME_CONTENT + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Navegation.NavegationEntry.TABLE_NAME;
}
