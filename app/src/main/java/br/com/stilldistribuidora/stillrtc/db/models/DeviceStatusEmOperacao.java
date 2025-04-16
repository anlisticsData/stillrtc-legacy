package br.com.stilldistribuidora.stillrtc.db.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Still Technology and Development Team on 05/07/2018.
 */

public class DeviceStatusEmOperacao implements Serializable{
    private static final long serialVersionUID = -2161110911377686463L;
    @SerializedName("delivery_id")
    private int deliveryId;

    @SerializedName("status")
    private int status;



    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Result implements Serializable {

        private static final long serialVersionUID = -2643991617962935718L;

        @SerializedName("checkoutDevice")
        public ArrayList<DeviceStatusEmOperacao> ar;

    }
}
