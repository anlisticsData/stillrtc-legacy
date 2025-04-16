package br.com.stilldistribuidora.partners.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.partners.Casts.AddressCast;
import br.com.stilldistribuidora.partners.Casts.ApiError;
import br.com.stilldistribuidora.partners.Casts.Conta;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCast;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.stillrtc.R;

public class MyDataActivity extends ActivityBaseApp {
    private static final Components components = new Components();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        components.contextActivity = this;
        setContentView(R.layout.activity_my_data);

        components.configApp = new ServerConfiguration(components.contextActivity);
        components.appOpeationsRepository = new AppOpeationsRepository(components.contextActivity);
        String userLogged = components.configApp.getUserLogged();
        components.btnUodate = (Button) findViewById(R.id.btnUpdate);

        components.userLoginCast = new Gson().fromJson(userLogged, UserLoginCast.class);
        components.code = (TextView) findViewById(R.id.code);
        components.name = (EditText) findViewById(R.id.name);
        components.sobrenome = (EditText) findViewById(R.id.sobrenome);
        components.telefone = (EditText) findViewById(R.id.telefone);
        components.email = (EditText) findViewById(R.id.email);
        components.cpf = (EditText) findViewById(R.id.cpf);
        components.address = (EditText) findViewById(R.id.address);
        components.typeAccount = (EditText) findViewById(R.id.typeAccount);
        components.nameCompleted = (EditText) findViewById(R.id.nameCompleted);
        components.bankname = (EditText) findViewById(R.id.bankname);
        components.agency = (EditText) findViewById(R.id.agency);
        components.account = (EditText) findViewById(R.id.account);
        components.name = (EditText) findViewById(R.id.name);
       // components.avatar = (ImageView) findViewById(R.id.avatar);
        components.radio_pix=(RadioButton)findViewById(R.id.radio_pix);
        components.radio_conta=(RadioButton)findViewById(R.id.radio_conta);
        components.keypix=(EditText)findViewById(R.id.keypix);
        components.cpfaccount=(EditText)findViewById(R.id.cpfaccount);
        components.typekeypix=(EditText)findViewById(R.id.typekeypix);
        components.avatar =  findViewById(R.id.avatar) ;




        OnResponseHttp CallBackOnResponseGlobalApi = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {

            }

            @Override
            public void onError(Object error) {

            }
        };
        components.servicesHttp = new ServicesHttp(this, components.configApp,CallBackOnResponseGlobalApi);


        loadMyDataUser(components.userLoginCast);
        loadEventsUi();
    }

    public void back(View v){
        finish();
    }

    public void onHome (View view) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void loadEventsUi() {
        components.radio_pix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadioButton("PIX");
            }
        });

        components.radio_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadioButton("CONTA");
            }
        });





        components.btnUodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Conta conta = new Conta();
                    AddressCast addressCast = new AddressCast();
                    addressCast.setAddress(components.address.getText().toString());
                    UserLoginCast updateUser= new UserLoginCast();
                    updateUser.setName(components.name.getText().toString());
                    updateUser.setUuid(components.code.getText().toString());
                    updateUser.setLastname(components.sobrenome.getText().toString());
                    updateUser.setMobilephone(components.telefone.getText().toString());
                    updateUser.setEmail(components.email.getText().toString());
                    updateUser.setCpf(components.cpf.getText().toString());
                    updateUser.setAddress(addressCast);
                    conta.setType(components.typeAccount.getText().toString());
                    conta.setName_completed(components.nameCompleted.getText().toString());
                    conta.setName_bank(components.bankname.getText().toString());
                    conta.setCpf(components.cpfaccount.getText().toString());
                    conta.setAgency(components.agency.getText().toString());
                    conta.setConta(components.account.getText().toString());
                    conta.setPix(components.keypix.getText().toString());
                    conta.setType_pix(components.typekeypix.getText().toString());
                    updateUser.setConta(conta);
                    ProgressDialog progressDialog = new ProgressDialog(components.contextActivity);
                    progressDialog.setTitle("Processando...");
                    progressDialog.setMessage("Realizando Atualizações...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    OnResponseHttp responseHttp=new OnResponseHttp() {
                        @Override
                        public void onResponse(Object data) {
                            ResponseBaseCast response = (ResponseBaseCast) data;
                            AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                            builder.setMessage(response.msn).setTitle(R.string.dialog_message);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(components.contextActivity, LoginAppActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            
                            
                            
                        }

                        @Override
                        public void onError(Object error) {
                            ApiError responseBaseCast = (ApiError)error;
                            Toast.makeText(components.contextActivity,responseBaseCast.getMessage(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    };
                    components.servicesHttp.updatePartnerInformations(updateUser,responseHttp);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void loadMyDataUser(UserLoginCast user) {
        components.code.setText(user.getUuid());
        components.name.setText(user.getName());
        components.sobrenome.setText(user.getLastname());
        components.telefone.setText(user.getMobilephone());
        components.email.setText(user.getEmail());
        components.cpf.setText(user.getCpf());
        components.address.setText(user.getAddress().getAddress());
        components.typeAccount.setText(user.getConta().getType());
        components.nameCompleted.setText(user.getConta().getName_completed());
        components.bankname.setText(user.getConta().getName_bank());
        components.agency.setText(user.getConta().getAgency());
        components.account.setText(user.getConta().getConta());
        components.cpfaccount.setText(user.getConta().getCpf());
        components.keypix.setText(user.getConta().getPix());
        components.typekeypix.setText(user.getConta().getType_pix());

        setRadioButton(user.getConta().getType());
    }

    private void setRadioButton(String type) {
        if(type.equals("PIX")){
            components.radio_pix.setChecked(true);
            components.radio_conta.setChecked(false);
            components.typeAccount.setText(type);

        }else{
            components.radio_pix.setChecked(false);
            components.radio_conta.setChecked(true);
            components.typeAccount.setText(type);
        }
    }

    private static class Components {
        public Context contextActivity;


        public Toolbar toolbar;
        public ServerConfiguration configApp;
        public AppOpeationsRepository appOpeationsRepository;
        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;

        public EditText name, sobrenome, telefone, email, cpf, address, typeAccount, nameCompleted, favorecido, agencia, conta;
        public TextView code;
        public EditText bankname;
        public EditText agency;
        public EditText account;
        public ImageView avatar;
        public Button btnUodate;
        public RadioButton radio_pix;
        public RadioButton radio_conta;
        public EditText pix,cpfaccount;
        public EditText keypix;
        public EditText typekeypix;
    }
}
