package br.com.stilldistribuidora.partners.resources;

import br.com.stilldistribuidora.stillrtc.db.Constants;

public class Resources {

    public static final String DEBUG ="#4- " ;
    public static final String DEFAULT_IMAGE ="imagen/notphoto.png" ;
    public static final String UPDATED_CAR ="Carrinho Atualizado.." ;
    public static final String PIPE = "|";
    public static final String NOTIFICATION_CHANNEL_ID ="10002" ;
    public static final int CHANNEL_ID =201913 ;
    public static final int CHANNEL_ID_2 =201913784;

    public static final String _SYSTEM_LAST_POINT_ ="LastPoint" ;
    public static final String NAVEGATIONS_MAPS="http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s";
    public   static final  String STARTING_POINT_NOT_FOUND="CMD_START_NOT_FOUND";
    public   static final  String ERROR_POINT_NOT_FOUND="CMD_ERROR_NOT_FOUND";
    public static final Object LAST_ID_AND_POSSITION_OPERATION_CURRENTR = "LAST_ID_AND_POSSITION_OPERATION_CURRENTR";
    public static  String RC_API_TOKEN_TO_BASE=String.format("%s='%s'", "descricao", Constants.API_TOKEN_JWT);
    public static  String RC_API_TOKEN_STATIC_TO_BASE=String.format("%s='%s'", "descricao", Constants.API_TOKEN);

    public static  String RC_USER_LOGGED_TO_BASE=String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED);
    public static  String RC_API_URL_BASE=String.format("%s='%s'", "descricao", Constants.API_URL);
    public static  String RC_API_URL_IME_BASE=String.format("%s='%s'", "descricao", Constants.APP_IME_STILL);
    public static  String RC_USER_LOGGED_STILL_CONIS=String.format("%s='%s'", "descricao", Constants.API_USER_LOGGED_STILL_COINS);
    public static  String STATE_OPERATION_PARTNER_OPEN="STATE_OPERATION_PARTNER_OPEN";











}
