package br.com.stilldistribuidora.partners.Base;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInformations {
    private final Context context;

    public PhoneInformations(Context context) {
        this.context = context;

    }


    public String getPhoneId() {
        try {
            final TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
            final String imei = telephonyManager.getDeviceId();
            return imei;

        } catch (Exception e) {
            return "**";
        }
    }
}
