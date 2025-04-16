package br.com.stilldistribuidora.partners.Base;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.views.RouteGuidanceActivity;
import br.com.stilldistribuidora.stillrtc.R;

public class Playler {
    private static final Components components = new Components();

    public Playler(Context contextActivity){
        components.contextActivity = contextActivity;
   }


    public void restPlayer(){
        if (components.mplayer != null) {
            components.mplayer.release();
            components.mplayer = null;
        }
    }


    public void statePlayer(){
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.bubble);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void startRouter() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.icr);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void turnRight() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vd);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gpsNotFound() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.falhagps);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnLeft() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.ve);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnFromTheOtherSide() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vc);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnFromTheOtherSideRigth() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vvdd);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void VESVD() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vesvd);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void FFF() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.fff);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void NRPQS() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.nrpqs);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void NRPTS() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.nrpts);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void NRPSS() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.nrpss);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void NRPPS() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.nrpps);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void VDSVE() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vdsve);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void NEROLRNQSF() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.nerolrnqsf);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void direcaoPontoAzul() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.sda);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnFromTheOtherSideLeft() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vvee);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goAhead() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.sf);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void thereIsNoCommand() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.notc);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void farFromTheBeginning() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.vdii);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  followTowardsThePointGreem() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.iniciosentido);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void   returnAcrossTheStreet() {
        try {
            if (components.mplayer != null) {
                components.mplayer.release();
                components.mplayer = null;
            }
            components.mplayer = MediaPlayer.create(components.contextActivity, R.raw.rtoldr);
            components.mplayer.start();
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    private static class Components {
        public ActivityResultLauncher<Intent> receiveAnswerFromChildren;
        public Context contextActivity;
        public Toolbar toolbar;
        public OperationModelRepository operationModelRepository;
        public List<RouterMap> instructions;
        public List<Zonas> zonas;
        public GoogleMap mMap;
        public Thread exec;
        public MediaPlayer mplayer;

    }


}
