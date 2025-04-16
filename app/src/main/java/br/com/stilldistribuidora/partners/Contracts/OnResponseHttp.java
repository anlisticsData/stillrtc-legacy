package br.com.stilldistribuidora.partners.Contracts;

public interface OnResponseHttp {
    void onResponse(Object data);
    void onError(Object error);
}
