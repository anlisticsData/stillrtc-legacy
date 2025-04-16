package br.com.stilldistribuidora.partners.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import br.com.stilldistribuidora.Libs.Libs;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.PaternsModel;
import br.com.stilldistribuidora.stillrtc.ui.activities.SearchActivity;

public class RegisterPartnersActivity extends AppCompatActivity {
    private  static Components ContextFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_partners);
        ContextFrame = new Components(this);
        initEvents();

        if(getIntent().hasExtra("EXTRA_INFO_ADDRESS")){
            ContextFrame.paternsModel=loadInfoToPaternsReceivedData();
            loadTypedInformation(ContextFrame.paternsModel);
        }
    }

    private void loadTypedInformation(PaternsModel patternsReceivedData) {
        ContextFrame.txtName.setText(patternsReceivedData.getName());
        ContextFrame.lastName.setText(patternsReceivedData.getLastName());
        ContextFrame.txtCpf.setText(patternsReceivedData.getCpf());
        ContextFrame.txtMobliePhone.setText(patternsReceivedData.getMobliePhone());
        ContextFrame.txtLogin.setText(patternsReceivedData.getEmail());
        ContextFrame.txtPassword.setText(patternsReceivedData.getPassword());
        ContextFrame.input_address.setText(patternsReceivedData.getAddress());
    }

    private void initEvents() {
        ContextFrame.txtName=(EditText)findViewById(R.id.input_name);
        ContextFrame.lastName=(EditText)findViewById(R.id.input_last_name);
        ContextFrame.txtCpf=(EditText)findViewById(R.id.input_cpf);
        ContextFrame.txtMobliePhone=(EditText)findViewById(R.id.input_moblie_phone);
        ContextFrame.txtLogin=(EditText)findViewById(R.id.input_email);
        ContextFrame.txtPassword=(EditText)findViewById(R.id.input_password);
        ContextFrame.input_address=(EditText)findViewById(R.id.input_address);

        ContextFrame. buttonRegistration=(Button)findViewById(R.id.btnadvence);
        ContextFrame.buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaternsModel paternsModel = loadInfoToPaternsData();
                if(validateInputs(paternsModel)){
                    ContextFrame.buttonRegistration.setText(R.string.processando_aguade);
                    Bundle bundle = new Bundle();
                    bundle.putString("name",paternsModel.getName());
                    bundle.putString("lastName",paternsModel.getLastName());
                    bundle.putString("cpf",paternsModel.getCpf());
                    bundle.putString("mobliePhone",paternsModel.getMobliePhone());
                    bundle.putString("email",paternsModel.getEmail());
                    bundle.putString("password",paternsModel.getPassword());
                    bundle.putString("address",paternsModel.getAddress());
                    bundle.putString("pix",paternsModel.getPixJson());
                    Intent intent = new Intent(ContextFrame.self, DadosBancariosActivity.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        ContextFrame.buttontogoback=(Button)findViewById(R.id.btngotoback);
        ContextFrame.buttontogoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContextFrame.self, LoginAppActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        ContextFrame.buttonSearch=(Button)findViewById(R.id.btnSearch);
        ContextFrame.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PaternsModel paternsModel = loadInfoToPaternsData();
                Bundle bundle = new Bundle();
                bundle.putString("name",paternsModel.getName());
                bundle.putString("lastName",paternsModel.getLastName());
                bundle.putString("cpf",paternsModel.getCpf());
                bundle.putString("mobliePhone",paternsModel.getMobliePhone());
                bundle.putString("email",paternsModel.getEmail());
                bundle.putString("password",paternsModel.getPassword());
                bundle.putString("address",paternsModel.getAddress());
                bundle.putString("pix",paternsModel.getPixJson());

                Intent intent = new Intent(ContextFrame.self, SearchActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs(PaternsModel paternsModel) {
        StringBuffer mensagens=new StringBuffer();
        if(paternsModel.getName().isEmpty()){
            mensagens.append(getString(R.string.str_obr_name));
        }
        if(paternsModel.getLastName().isEmpty()){
            mensagens.append(getString(R.string.str_obr_lastname));
        }

        if(paternsModel.getCpf().isEmpty()){
            mensagens.append(getString(R.string.str_obr_cpf));
        }
        if(!Libs.isCPF(paternsModel.getCpf())){
            mensagens.append(getString(R.string.str_obr_cpf_invalido));
        }

        if(paternsModel.getMobliePhone().isEmpty()){
            mensagens.append(getString(R.string.str_obr_mobliephone));
        }
        if(paternsModel.getEmail().isEmpty()){
            mensagens.append(getString(R.string.str_obr_email));
        }else{
            if(!Libs.validateEmail(paternsModel.getEmail())){
                mensagens.append(getString(R.string.str_obr_email_invalide));
            }
        }
        if(paternsModel.getPassword().isEmpty()){
            mensagens.append(getString(R.string.str_obr_password));
        }

        if(paternsModel.getAddress().isEmpty()){
            mensagens.append(getString(R.string.str_obr_address));
        }
        if(mensagens.length() > 0){
            new AlertDialog.Builder(ContextFrame.self)
                .setTitle(getString(R.string.obrigatorio))
                .setMessage(mensagens.toString())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContextFrame.txtName.requestFocus();
                        ContextFrame.buttonRegistration.setText(R.string.next);
                    }
                })
                .show();
        }

        return (mensagens.length() > 0) ? false : true;
    }

    private PaternsModel loadInfoToPaternsData() {
        ContextFrame.paternsModel.setName(ContextFrame.txtName.getText().toString());
        ContextFrame.paternsModel.setLastName(ContextFrame.lastName.getText().toString());
        ContextFrame.paternsModel.setCpf(ContextFrame.txtCpf.getText().toString());
        ContextFrame.paternsModel.setMobliePhone(ContextFrame.txtMobliePhone.getText().toString());
        ContextFrame.paternsModel.setEmail(ContextFrame.txtLogin.getText().toString());
        ContextFrame.paternsModel.setPassword(ContextFrame.txtPassword.getText().toString());
        ContextFrame.paternsModel.setAddress(ContextFrame.input_address.getText().toString());
        return ContextFrame.paternsModel;
    }

    private PaternsModel loadInfoToPaternsReceivedData() {
        PaternsModel paternsModel = new PaternsModel();
        Bundle extras = getIntent().getExtras();
        if (getIntent().hasExtra("name")) {
            paternsModel.setName(getIntent().getStringExtra("name"));
        }
        if (getIntent().hasExtra("lastName")) {
            paternsModel.setLastName(getIntent().getStringExtra("lastName"));
        }
        if (getIntent().hasExtra("cpf")) {
            paternsModel.setCpf(getIntent().getStringExtra("cpf"));
        }
        if (getIntent().hasExtra("mobliePhone")) {
            paternsModel.setMobliePhone(getIntent().getStringExtra("mobliePhone"));
        }

        if (getIntent().hasExtra("email")) {
            paternsModel.setEmail(getIntent().getStringExtra("email"));
        }

        if (getIntent().hasExtra("password")) {
            paternsModel.setPassword(getIntent().getStringExtra("password"));
        }

        if (getIntent().hasExtra("address")) {
            paternsModel.setAddress(getIntent().getStringExtra("address"));
        }

        if (getIntent().hasExtra("pix")) {
            paternsModel.setPixJson(getIntent().getStringExtra("pix"));
        }
        return paternsModel;
    }

    public class Components{
        private Context self;
        private PaternsModel paternsModel;
        private Boolean isValidate=false;
        private Button buttontogoback,buttonRegistration,buttonSearch;
        private EditText txtName,lastName,txtCpf , txtMobliePhone,txtLogin,txtPassword,input_address;
        private CheckBox keepConnected;
        public Components(Context self){
            this.self=self;
            paternsModel=new PaternsModel();
        }
    }
}
