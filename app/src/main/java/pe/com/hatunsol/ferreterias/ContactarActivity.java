package pe.com.hatunsol.ferreterias;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.guna.libmultispinner.MultiSelectionSpinner;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import pe.com.hatunsol.ferreterias.dialogframent.AceptarDialogfragment;
import pe.com.hatunsol.ferreterias.dialogframent.ProgressDialogFragment;
import pe.com.hatunsol.ferreterias.entity.BE_Constantes;
import pe.com.hatunsol.ferreterias.entity.BE_Servicio;
import pe.com.hatunsol.ferreterias.entity.BE_Tarea;
import pe.com.hatunsol.ferreterias.entity.Parametro;
import pe.com.hatunsol.ferreterias.entity.RestResult;
import pe.com.hatunsol.ferreterias.model.Distrito;
import pe.com.hatunsol.ferreterias.rest.RestClient;

import static pe.com.hatunsol.ferreterias.utilitario.Util.BE_DatosUsuario;
import static pe.com.hatunsol.ferreterias.utilitario.Util.MensajeInicialSeleccione;

/**
 * Created by Sistemas on 07/09/2016.
 */
public class ContactarActivity extends ActionBarActivity implements AceptarDialogfragment.AceptarDialogfragmentListener {
    private ProgressDialogFragment mProgressDialogFragment;
    /*private List<Distrito> lstDistrito;
    private Spinner spDistrito;*/
    private TextView tvNombre, tvEspecialidad;
    private Button btContactar;
    private EditText etTexto;
    private ImageView ivFoto;
    private Spinner spServicio;
    private List<BE_Tarea> lstServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactar);
        //Instanciar
        // spDistrito = (Spinner) findViewById(R.id.spDistrito);
        btContactar = (Button) findViewById(R.id.btContactar);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        spServicio = (Spinner) findViewById(R.id.spServicio);
        tvEspecialidad = (TextView) findViewById(R.id.tvEspecialidad);
        etTexto = (EditText) findViewById(R.id.etTexto);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        btContactar.setOnClickListener(btRegistrarOnClickListener);
        new CargarServicios().execute();


        Bundle extras = getIntent().getExtras();

        tvNombre.setText(extras.getString("Nombre", ""));
        tvEspecialidad.setText(extras.getString("Especialidad", ""));
        Picasso.with(this)
                .load(extras.getString("Foto", ""))
                .placeholder(R.drawable.camera_preview)
                .into(ivFoto);

    }

    public class CargarServicios extends AsyncTask<Void, Void, RestResult> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialogFragment == null) {
                mProgressDialogFragment = new ProgressDialogFragment();
                mProgressDialogFragment.setMensaje("Cargando..");
                mProgressDialogFragment.show(ContactarActivity.this.getFragmentManager(), ProgressDialogFragment.TAG);
            }

        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);
            mProgressDialogFragment.dismissAllowingStateLoss();

            try {

                JSONArray jarray = new JSONArray(restResult.getResult());
                BE_Tarea provloc = new BE_Tarea();
                lstServicio = new ArrayList<BE_Tarea>();
                JSONObject jsonobj;
                List<String> myResArrayList = new ArrayList<String>();

                lstServicio.add(0, new BE_Tarea(0, MensajeInicialSeleccione));

                for (int i = 0; i < jarray.length(); i++) {
                    jsonobj = jarray.getJSONObject(i);
                    provloc = new BE_Tarea();
                    provloc.setIdTarea(jsonobj.getInt("IdTarea"));
                    provloc.setNombre_Tarea(jsonobj.getString("Nombre_Tarea"));

                    lstServicio.add(provloc);
                    myResArrayList.add(i, provloc.getNombre_Tarea());
                }

                ArrayAdapter<BE_Tarea> dataAdapter = new ArrayAdapter<BE_Tarea>
                        (ContactarActivity.this, R.layout.spinner_item, lstServicio);
                dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                spServicio.setAdapter(dataAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;
            Bundle extras = getIntent().getExtras();
            //int ProveedorLocalId = extras.getInt("ProveedorLocalId");
            //markeroptions.getPosition().latitude + "&Longitud=" + markeroptions.getPosition().longitude
            return new RestClient().get("WSTarea.svc/Listar?idservicio=" + extras.getInt("IdServicio", 0), stringEntity, 30000);

        }
    }

    @Override
    public void onConfirmacionOK(int Operacion) {
        if (Operacion == BE_Constantes.Operacion.Salir) {
            finish();
        }
    }

    View.OnClickListener btRegistrarOnClickListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            if (validarDatos()) {
                new RegistrarseAsyncTask().execute();

            } else {
                AceptarDialogfragment confirmacionDialogfragment = new AceptarDialogfragment();
                confirmacionDialogfragment.setmConfirmacionDialogfragmentListener(ContactarActivity.this);
                confirmacionDialogfragment.setMensaje("Por favor Verifique los Datos");
                confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);
            }


        }
    };


    private boolean validarDatos() {

        boolean result = true;


        if (etTexto.getText().toString().equals("")) {
            etTexto.setError("Ingrese una Descripción");
            result = false;
        }

        if (spServicio.getSelectedItemPosition() == 0) {
            ((TextView) spServicio.getSelectedView()).setError("Ingrese Servicio");
            result = false;
        }

        return result;
    }


    /* public class CargarDistritos extends AsyncTask<Void, Void, RestResult> {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             if (mProgressDialogFragment == null) {
                 mProgressDialogFragment = new ProgressDialogFragment();
                 mProgressDialogFragment.setMensaje("Cargando..");
                 mProgressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.TAG);
             }
         }

         @Override
         protected void onPostExecute(RestResult restResult) {
             super.onPostExecute(restResult);


             try {
                 JSONObject jsonobj = new JSONObject(restResult.getResult());
                 JSONArray jarray = jsonobj.getJSONArray("ListarResult");
                 Distrito provloc = new Distrito();
                 lstDistrito = new ArrayList<Distrito>();
                 for (int i = 0; i < jarray.length(); i++) {
                     jsonobj = jarray.getJSONObject(i);
                     provloc = new Distrito();
                     provloc.setCodDepartament(jsonobj.getString("_CodDpto"));
                     provloc.setCodDistrito(jsonobj.getString("_CodDist"));
                     provloc.setCodProvincia(jsonobj.getString("_CodProv"));
                     provloc.setCodUbigeo(jsonobj.getString("_CodUbigeo"));
                     provloc.setNombre(jsonobj.getString("_Nombre"));
                     lstDistrito.add(provloc);
                 }
                 lstDistrito.add(0, new Distrito("", MensajeInicialSeleccione));
                 //lstProveedorLocal = new Gson().fromJson(restResult, ListProveedorLocal.class);
                 ArrayAdapter<Distrito> dataAdapter = new ArrayAdapter<Distrito>
                         (RegistrarseActivity.this, R.layout.spinner_item, lstDistrito);
                 dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                 spDistrito.setAdapter(dataAdapter);


                 mProgressDialogFragment.dismissAllowingStateLoss();


             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

         @Override
         protected RestResult doInBackground(Void... params) {
             StringEntity stringEntity = null;
             return new RestClient().get("Ubigeo.svc/Ubigeo?coddepa=15&codprov=00", stringEntity, 30000);
         }
     }
 */
    public class RegistrarseAsyncTask extends AsyncTask<Void, Void, RestResult> {

        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);
            mProgressDialogFragment.dismissAllowingStateLoss();
            //Log.d("statusCode", restResult.getStatusCode() + "");
            //Log.d("result",restResult.getResult()+"");
            Bundle args = new Bundle();

            AceptarDialogfragment confirmacionDialogfragment = new AceptarDialogfragment();

            confirmacionDialogfragment.setmConfirmacionDialogfragmentListener(ContactarActivity.this);

            try {
                String UserId = restResult.getResult();


                if (UserId.equals("0") || UserId.equals("")) {
                    confirmacionDialogfragment.setMensaje("Hubo un Problema al Contactar, vuelva a intentarlo.");
                    confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);

                } else {
                    confirmacionDialogfragment.setMensaje("Se ha Enviado un Correo al Operario para que tome contacto con usted.");
                    args.putInt("Operacion", BE_Constantes.Operacion.Salir);
                    confirmacionDialogfragment.setArguments(args);
                    confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);
                }
            } catch (Exception ex) {
                confirmacionDialogfragment.setMensaje("Error al Registrar: " + ex.getMessage());
                //confirmacionDialogfragment.show(getActivity(), AceptarDialogfragment.TAG);
                ex.printStackTrace();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialogFragment = new ProgressDialogFragment();
            mProgressDialogFragment.setMensaje("Registrando..");
            mProgressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.TAG);
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;

            //JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {

                Bundle extras = getIntent().getExtras();
                //Enviar en Vacio los Campos que no siempre se llenan (solo titular o Prospecto)
                //jsonObject.put("IdCliente", BE_DatosUsuario.getEmpleadoId());
                jsonObject.put("IdCliente", 1);
                jsonObject.put("IdEmpleado", 1);
                jsonObject.put("UserIdCrea", 1);
                jsonObject.put("IdTarea", ((BE_Tarea) spServicio.getSelectedItem()).getIdTarea());
                jsonObject.put("Tarea", ((BE_Tarea) spServicio.getSelectedItem()).getNombre_Tarea());
                jsonObject.put("Texto", etTexto.getText());

                stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return new RestClient().post("WSSolicitud.svc/Registrar", stringEntity, 30000);
        }
    }
}
