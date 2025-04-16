package br.com.stilldistribuidora.partners.Base.navegador;

import android.provider.BaseColumns;

public class Navegation {
    private Navegation(){}

    public static class NavegationEntry implements BaseColumns {
        public static final String TABLE_NAME = "navegations";
        public static final String COLUMN_NAME_ID= "id";
        public static final String COLUMN_NAME_PROPS = "props";
        public static final String COLUMN_NAME_CONTENT = "content";

    }
}

