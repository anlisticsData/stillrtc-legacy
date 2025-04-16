package br.com.stilldistribuidora.stillrtc.db.models

/**
* Created by Still Technology and Development Team on 20/04/2017.
*/

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.ArrayList

class Zone : Serializable {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("content")
    var content: String? = null

    class Result : Serializable {

        @SerializedName("zones")
        val ar: ArrayList<Zone>? = null

        companion object {

            private const val serialVersionUID = -2643991617962935718L
        }
    }
    companion object {

        private const val serialVersionUID = -2161110911377686463L
    }

}