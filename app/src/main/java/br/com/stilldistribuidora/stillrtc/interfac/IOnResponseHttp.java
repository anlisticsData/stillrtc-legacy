package br.com.stilldistribuidora.stillrtc.interfac;

/**
 * Created by Still Technology and Development Team on 23/08/2018.
 */

public interface IOnResponseHttp {

    public void onResponseHttp(String response);
    public void onErrorResponseHttp(Throwable e);

}
