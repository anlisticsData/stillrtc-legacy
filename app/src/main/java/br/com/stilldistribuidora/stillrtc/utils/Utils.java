package br.com.stilldistribuidora.stillrtc.utils;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.BuildConfig;
import br.com.stilldistribuidora.stillrtc.R;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/10/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class Utils {

    public static int getVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionCode;
    } //Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT)

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(source);
        }
    }

    private double calculaDistancia(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
    }

    /**
     * Este metodo retorna o percentual atual da bateria
     *
     * @param context
     * @return
     */
    public static float getBatteryPercentage(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        return (level / (float) scale) * 100;
    }

    /**
     * Este metodo retorna o percentual atual da bateria
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {

        String strVersion = context.getResources().getString(R.string.str_version);
        String strDvl = context.getResources().getString(R.string.str_version_dvl);

        String[] str = BuildConfig.VERSION_NAME.split(" ");

        return BuildConfig.VERSION_NAME.contains("dvl") ?  strVersion + str[0] + strDvl: strVersion + str[0];
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return "noip";
        } // for now eat exceptions
        return "noip";
    }

    /**
     * "wlan0" or "eth0"
     * <p>
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public void getCurrentIP () {
        //ip.setText("Please wait...");
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httpget = new HttpGet("http://ifcfg.me/ip");
//            // HttpGet httpget = new HttpGet("http://ipecho.net/plain");
//            HttpResponse response;
//
//            response = httpclient.execute(httpget);
//
//            //Log.i("externalip",response.getStatusLine().toString());
//
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                long len = entity.getContentLength();
//                if (len != -1 && len < 1024) {
//                    String str=EntityUtils.toString(entity);
//                    //Log.i("externalip",str);
//                    ip.setText(str);
//                } else {
//                    ip.setText("Response too long or error.");
//                    //debug
//                    //ip.setText("Response too long or error: "+EntityUtils.toString(entity));
//                    //Log.i("externalip",EntityUtils.toString(entity));
//                }
//            } else {
//                ip.setText("Null:"+response.getStatusLine().toString());
//            }
//
//        }
//        catch (Exception e)
//        {
//            ip.setText("Error");
//        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        return true;

        /*
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            return (returnVal==0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;*/
    }





}