package br.com.stilldistribuidora.pco.config;

public class Constante {
    public static final int BASE_PCO_VERSAO = 1;
    public static final  String AppPcoVersion="Versão : 1.0";
    public static final String BASE_PCO_TABLE_AUTO_LOGIN ="pco_save_login" ;
    public static final String BASE_PCO_COL_SAVE_LOGIN = "user_login";
    public static final String BASE_PCO_COL_SAVE_LOGIN_SENHA = "user_senha";
    public static final String BASE_PCO_TABLE_WS_CONFIG ="ws_Config" ;
    public static final String BASE_PCO_TABLE_WS_MOVIMENTO ="movimento" ;
    public static final String BASE_PCO_TABLE_WS_MOVIMENTO_FOTO ="tbl_picture_grafica" ;
    public static final String ULT_DATA_SELECIONADA ="ULT_DATA_SELECIONADA" ;
    public static final  String TOKEN_JWT="JWT_TOKEN";
    public static  final  String DEVICE_ACESS="DEVICE_ACESS";
    public static  final  String BASE_PCO_TABLE_WS_SINCRONIZAR="tbl_sincronizar";
    public static  final  String DEVICE_ACESS_USER_LOGIN="DEVICE_ACESS_USER_LOGIN_PCO";





    public static final String API_ACTIVE_URL = "API_ACTIVE_URL";

    public static final String USER_ATIVO = "USER_ATIVO";
    public static final String ULT_STATUS_SELECIONADO ="ULT_STATUS_SELECIONADO" ;
    public static final String WS_PCO_GRF_MV_FT_SINC_OPERACAO ="codigo_mov" ;
    public static final String WS_PCO_GRF_MV_FT_SINC_CODIGO = "id";
    public static final String WS_PCO_GRF_MV_FT_SINC_DT_CREATE = "dt_create";
    public static final String WS_PCO_GRF_MV_FT_SINC_DT_FINISH = "dt_finish";
    public static final String WS_PCO_GRF_MV_FT_SINC_STATUS = "status";
    public static final String SERVICE_IS_ACTIVE = "SERVICE_IS_ACTIVE";




    //Tabela de Status gravacao de GPS/
    //device_status

    public  static final String DEVICE_TABLE_NAME="device_status";
    public  static final String DEVICE_STATUS_KEY="key_";
    public  static final  String DEVICE_CODE_MOV_GRAFICA="cod_mov";
    public  static final String DEVICE_LAT_LON="lat_lon";
    public  static final  String DEVICE_DEVICE_ID="device";
    public  static final  String DEVICE_USER="user";
    public  static final String DEVICE_CREATE_AT="create_at";
    public  static final String DEVICE_STATUS="status";

    //Operacao Documents
    public  static final String DOC_OPERACOES="operacoes";
    public  static final String DOC_OPERACOES_UUID="ObjectId";
    public  static final String DOC_OPERACOES_UUID_LANCAMENTO="uuid";
    public  static final String DOC_OPERACOES_STATUS="status";
    public  static final String DOC_OPERACOES_DATA="data";
    public  static final String DOC_OPERACOES_STRUCTS="structs";
    public  static final String DOC_OPERACOES_SINCRONED="sincronizado";















    //Tabela de Status gravacao de GPS/






    public static String BASE_PCO="stillpco.db";



    //Configuração WS
    public static  final String WS_KEY="key_";
    public static  final String WS_DESCRICAO="descricao";
    public static  final String WS_OPCAO="opcao";
    //Configuração WS


    //Usuarios grafica
    public  static  final  String WS_PCO_GRF_USER_KEY="us_codigo";
    public  static  final  String WS_PCO_GRF_USER_perfil="us_perfil";
    public  static  final  String WS_PCO_GRF_USER_data="us_update_at";
    public  static  final  String WS_PCO_GRF_USER_qt_retirar="qt_a_retirar";
    //Usuarios grafica


    /*Movimentos */
    public  static  final  String WS_PCO_GRF_MV_KEY="mv_codigo";
    public  static  final  String WS_PCO_GRF_MV_MT_KEY="mt_codido";
    public  static  final  String WS_PCO_GRF_MV_MT_NOME="mt_nome";
    public  static  final  String WS_PCO_GRF_MV_MT_FORMATO="mt_formato";
    public  static  final  String WS_PCO_GRF_MV_MT_TIME="mv_time";
    public  static  final  String WS_PCO_GRF_MV_MT_SINCRONIZADO="mv_sincronizado";







    public  static  final  String WS_PCO_GRF_MV_VM_VERSAO="vm_versao";
    public  static  final  String WS_PCO_GRF_MV_EM_NOME="em_nome";
    public  static  final  String WS_PCO_GRF_MV_EM_IMG="em_img";
    public  static  final  String WS_PCO_GRF_MV_CL_KEY="cl_codigo";
    public  static  final  String WS_PCO_GRF_MV_CL_NOME="cl_nome";
    public  static  final  String WS_PCO_GRF_VM_VERSION_CODE="vm_codigo";
    public  static  final  String WS_PCO_GRF_MV_QT_RT="mv_qt_retirar";
    public  static  final  String WS_PCO_GRF_MV_DT_CREATE="mv_create_at";
    public  static  final  String WS_PCO_GRF_MV_MV_STATUS="mv_startus";
    public  static  final  String WS_PCO_GRF_MV_DT_RT="mv_dt_retirada";
    public  static  final  String WS_PCO_GRF_MV_MV_QT_RT="mv_qt_retirada";
    public  static  final  String WS_PCO_GRF_MV_DT_ENTREGA="mv_dt_entrega";
    public  static  final  String WS_PCO_GRF_MV_QT_ENTREGE="mv_qt_entregue";
    public  static  final  String WS_PCO_GRF_MV_SINCRONIZADO="mv_as_sincronizado";
    /*Movimentos */


    /*Fotos da Grafica*/

    public  static  final  String WS_PCO_GRF_MV_FT_CODIGO="codigo";
    public  static  final  String WS_PCO_GRF_MV_FT_CREATE_AT="create_at";
    public  static  final  String WS_PCO_GRF_MV_FT_PATH_FILE="path_file";
    public  static  final  String WS_PCO_GRF_MV_FT_NAME_FILE="name_file";
    public  static  final  String WS_PCO_GRF_MV_FT_TB_RETIRADA_GF_CODIGO="tb_retirada_gf_codigo";
    public  static  final  String WS_PCO_GRF_MV_FT_DEVICE="device";
    public  static  final  String WS_PCO_GRF_MV_FT_USER="user";
    public  static  final  String WS_PCO_GRF_MV_FT_LOC="loc";
    public  static  final  String WS_PCO_GRF_MV_FT_SINCRONIZADA="sincronizado";










    /*Fotos da Grafica*/


}
