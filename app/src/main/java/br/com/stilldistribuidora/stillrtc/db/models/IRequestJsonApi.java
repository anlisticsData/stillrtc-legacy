package br.com.stilldistribuidora.stillrtc.db.models;

import androidx.annotation.NonNull;

/**
 * Created by Still Technology and Development Team on 14/08/2018.
 */

public interface IRequestJsonApi {

    void onSuccess(@NonNull String value);

    void onError(@NonNull Throwable throwable);
}
