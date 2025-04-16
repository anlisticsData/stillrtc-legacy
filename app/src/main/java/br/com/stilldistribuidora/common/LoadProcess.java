package br.com.stilldistribuidora.common;

import android.app.ProgressDialog;
import android.content.Context;

import br.com.stilldistribuidora.stillrtc.db.Constants;

public class LoadProcess {


    private ProgressDialog pd;

    public LoadProcess(Context context, String s, boolean b) {
        try{

            if(pd!=null){
                if(pd.isShowing()){
                    pd.dismiss();
                }
            }
            pd= new ProgressDialog(context);
            pd.setMessage(s);
            pd.setCancelable(b);
            pd.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadActivityShow(Context context, String mensagen, Boolean isClose) {
      try{
          if(pd!=null){
              if(pd.isShowing()){
                  pd.dismiss();
              }
          }
          pd = new ProgressDialog(context);
          pd.setMessage(mensagen);
          pd.setCancelable(isClose);
          pd.show();
          new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                      Thread.sleep(Constants.PROCESS_LOAD_TIME);
                      if (pd.isShowing()) {
                          pd.dismiss();
                      }

                  } catch (Exception e) {
                  }
              }
          }).start();


      }catch (Exception e){
          e.printStackTrace();
      }



    }

    public void loadActivityClose() {
        try{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Constants.PROCESS_LOAD_TIME);
                        if (pd!=null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();



        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
