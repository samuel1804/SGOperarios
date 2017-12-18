package pe.com.hatunsol.ferreterias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.guna.libmultispinner.MultiSelectionSpinner;

import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pe.com.hatunsol.ferreterias.adapter.MainAdapterListaEstablecimiento;
import pe.com.hatunsol.ferreterias.adapter.MainAdapterListaOperador;
import pe.com.hatunsol.ferreterias.dialogframent.AceptarDialogfragment;
import pe.com.hatunsol.ferreterias.dialogframent.ProgressDialogFragment;
import pe.com.hatunsol.ferreterias.entity.BE_Empleado;
import pe.com.hatunsol.ferreterias.entity.BE_EncuestaRepuesta;
import pe.com.hatunsol.ferreterias.entity.BE_Servicio;
import pe.com.hatunsol.ferreterias.entity.Establecimiento;
import pe.com.hatunsol.ferreterias.entity.RestResult;
import pe.com.hatunsol.ferreterias.rest.RestClient;
import pe.com.hatunsol.ferreterias.utilitario.Base64;
import pe.com.hatunsol.ferreterias.utilitario.ImageLoader;
import pe.com.hatunsol.ferreterias.utilitario.Util;

/**
 * Created by Sistemas on 14/06/2016.
 */
public class BuscarOperadorActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, AceptarDialogfragment.AceptarDialogfragmentListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    private List<BE_Empleado> lsListadoEstablecimiento = null;
    private MainAdapterListaOperador mMainAdapterListaEstablecimiento;
    private ListView lvProveedores;
    // private FloatingActionButton fabNew;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialogFragment mProgressDialogFragment;
    Uri selectedImageUri = null;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    Bitmap bitmap;
    private ProgressDialog dialog;
    public int ProveedorLocaId = 0;
    private List<BE_Servicio> lstServicio;
    public Uri imageUri;
    //private RadioGroup rgFiltro;
    private String Filtro = "";
    private MultiSelectionSpinner spFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_operador);


        lvProveedores = (ListView) findViewById(R.id.lvProveedores);
        /*fabNew = (FloatingActionButton) findViewById(R.id.fabNew);
        fabNew.setOnClickListener(fabOnClickListener);*/
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.green);


        //rgFiltro = (RadioGroup) findViewById(R.id.rgFiltro);
        spFiltro = (MultiSelectionSpinner) findViewById(R.id.spFiltro);

        //rgFiltro.setOnCheckedChangeListener(rgFiltroOnCheckedChangeListener);

        new CargarServicios().execute();


      /*  if (lsListadoEstablecimiento == null) {
            onRefresh();
        } else {
            llenarListaProveedorLocal();
        }*/


    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Filtro = "";


        for (int i = 0; i < strings.size(); i++) {
            for (int j = 0; j < lstServicio.size(); j++) {
                if (lstServicio.get(j).getDesc_Serv().equals(strings.get(i))) {

                    if (Filtro.equals("")) {
                        Filtro += lstServicio.get(j).getIdServicio();
                    } else {
                        Filtro += "," + lstServicio.get(j).getIdServicio();
                    }
                }


            }
        }
        new BuscarProveedorAsyncTask().execute();
    }

    public class CargarServicios extends AsyncTask<Void, Void, RestResult> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialogFragment == null) {
                mProgressDialogFragment = new ProgressDialogFragment();
                mProgressDialogFragment.setMensaje("Cargando..");
                mProgressDialogFragment.show(BuscarOperadorActivity.this.getFragmentManager(), ProgressDialogFragment.TAG);
            }

        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);
            mProgressDialogFragment.dismissAllowingStateLoss();

            try {

                JSONArray jarray = new JSONArray(restResult.getResult());
                BE_Servicio provloc = new BE_Servicio();
                lstServicio = new ArrayList<BE_Servicio>();
                JSONObject jsonobj;
                List<String> myResArrayList = new ArrayList<String>();


                for (int i = 0; i < jarray.length(); i++) {
                    jsonobj = jarray.getJSONObject(i);
                    provloc = new BE_Servicio();
                    provloc.setIdServicio(jsonobj.getInt("IdServicio"));
                    provloc.setDesc_Serv(jsonobj.getString("Desc_Serv"));

                    lstServicio.add(provloc);
                    myResArrayList.add(i, provloc.getDesc_Serv());
                }


                Spinner spinner = (Spinner) findViewById(R.id.spFiltro);


                MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.spFiltro);
                multiSelectionSpinner.setItems(myResArrayList);


                int[] arra = new int[jarray.length()];
                for (int i = 0; i < jarray.length(); i++) {
                    arra[i] = i;
                    if (i == 0) {
                        Filtro += lstServicio.get(i).getIdServicio();
                    } else {
                        Filtro += "," + lstServicio.get(i).getIdServicio();
                    }

                }
                multiSelectionSpinner.setSelection(arra);
                multiSelectionSpinner.setListener(BuscarOperadorActivity.this);

                //supportBarFragment.getMapAsync(BuscarOperadorActivity.this);

                //supportBarFragment.getMapAsync(BuscarOperarioActivity.this);
                //LlenarMapa();
                //lstEstablecimiento = new Gson().fromJson(restResult, ListProveedorLocal.class);
                //atEstablecimiento = (AutoCompleteTextView) findViewById(R.id.atEstablecimiento);

                /*ArrayAdapter<Establecimiento> dataAdapter = new ArrayAdapter<Establecimiento>
                        (getActivity(), android.R.layout.simple_spinner_item, lstEstablecimiento);*/



               /* atEstablecimiento.setThreshold(1);
                atEstablecimiento.setAdapter(dataAdapter);

                atEstablecimiento.setOnItemClickListener(atEstablecimientosOnItemClickListener);*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;
            //Bundle extras = getActivity().getIntent().getExtras();
            //int ProveedorLocalId = extras.getInt("ProveedorLocalId");
            //markeroptions.getPosition().latitude + "&Longitud=" + markeroptions.getPosition().longitude
            return new RestClient().get("WSServicio.svc/Listar", stringEntity, 30000);

        }
    }


    View.OnClickListener btnBuscarOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };
    View.OnClickListener fabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          /*  Intent intent = new Intent(BuscarProveedorActivity.this, EstablecimientoActivity.class);

            intent.putExtra("cod_operacion", BE_Constantes.Operacion.Insertar);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //intent.putExtra("idEstablecimiento", 0L);
            startActivity(intent);*/
        }
    };

    private void llenarListaProveedorLocal() {
        ImageLoader imgLoader = new ImageLoader(this);
        imgLoader.clearCache();
        mMainAdapterListaEstablecimiento = new MainAdapterListaOperador(BuscarOperadorActivity.this, 0, lsListadoEstablecimiento);
        lvProveedores.setAdapter(mMainAdapterListaEstablecimiento);
        mMainAdapterListaEstablecimiento.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    //use imageUri here to access the image


                    selectedImageUri = imageUri;

                         /*Bitmap mPic = (Bitmap) data.getExtras().get("data");
                        selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = Util.getPath(selectedImageUri, getApplicationContext());

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    bitmap = Util.decodeFile(filePath, bitmap);

                    if (bitmap == null) {
                        Toast.makeText(getApplicationContext(),
                                "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                    } else {

                        String str = Util.createDirectoryAndSaveFile(bitmap, "Hatunsol.jpg");
                        Util.galleryAddPic(str, getApplicationContext());

                        dialog = ProgressDialog.show(BuscarOperadorActivity.this, "Subiendo",
                                "Porfavor espere...", true);
                        new ImageGalleryTask().execute();
                    }

                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }


    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new BuscarProveedorAsyncTask().execute();
            }
        });
    }

    @Override
    public void onConfirmacionOK(int Operacion) {

    }


    public class ImageGalleryTask extends AsyncTask<Void, Void, RestResult> {

        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);

            try {
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }


            AceptarDialogfragment confirmacionDialogfragment = new AceptarDialogfragment();
            confirmacionDialogfragment.setmConfirmacionDialogfragmentListener(BuscarOperadorActivity.this);


            try {
                JSONObject jso = new JSONObject(restResult.getResult());

                if (jso.getString("UploadPhotoResult").equals("Ok")) {
                    confirmacionDialogfragment.setMensaje("Se Cargó el Archivo Correctamente");
                    confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);
                    onRefresh();

                } else {
                    confirmacionDialogfragment.setMensaje(jso.getString("UploadPhotoResult"));
                    confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);

                }
            } catch (Exception e) {
                confirmacionDialogfragment.setMensaje("Error al Subir Archivos: " + e.getMessage());
                confirmacionDialogfragment.show(getSupportFragmentManager(), AceptarDialogfragment.TAG);
                e.printStackTrace();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;
            BitmapFactory.Options bfo;
            Bitmap bitmapOrg;
            ByteArrayOutputStream bao;

            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;
            //bitmapOrg = BitmapFactory.decodeStream(getAssets().open(""));

            bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);

				/*ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image",ba1));
				nameValuePairs.add(new BasicNameValuePair("cmd","image_android"));
				Log.v("log_tag", System.currentTimeMillis()+".jpg");	*/
            try {
                HttpClient httpclient = new DefaultHttpClient();
                /*HttpPost httppost = new
                        //  Here you need to put your server file address
                        HttpPost(Util.Direccion_WCF+"DocumentoAdjunto.svc/FileUpload");*/
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("image", ba1);
                jsonObject.put("ProveedorLocalId", ProveedorLocaId);
                String formato = Util.getPath(selectedImageUri, getApplicationContext());
                jsonObject.put("Nombre", formato.substring(formato.length() - 4, formato.length()));
                stringEntity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);


            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }

            return new RestClient().post("DocumentoAdjuntoProveedor.svc/UploadPhoto", stringEntity, 120000);


        }
    }


    class BuscarProveedorAsyncTask extends AsyncTask<Void, Void, RestResult> {


        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);
            try {
                if (restResult.getResult() == "") {
                    Toast.makeText(BuscarOperadorActivity.this, "No se encontraron Operarios", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }


                JSONArray jsonarray = new JSONArray(restResult.getResult());
                lsListadoEstablecimiento = new ArrayList<>();
                BE_Empleado proveedorlocal;
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    proveedorlocal = new BE_Empleado();
                  /*  proveedorlocal.setProveedorId(jsonobj.getInt("ProveedorId"));
                    proveedorlocal.setProveedorLocalId(jsonobj.getInt("ProveedorLocalId"));*/
                    proveedorlocal.setIdEmpleado(jsonobj.getInt("IdEmpleado"));
                    proveedorlocal.setNombres_Operario(jsonobj.getString("Nombres_Operario"));
                    proveedorlocal.setTelefono_Operario(jsonobj.getString("Telefono_Operario"));
                    proveedorlocal.setServicio(jsonobj.getString("Servicio"));
                    proveedorlocal.setIdServicio(jsonobj.getInt("IdServicio"));
                    proveedorlocal.setDistrito(jsonobj.getString("Distrito"));
                    proveedorlocal.setExperiencia_Operario(jsonobj.getInt("Experiencia_Operario"));
                    proveedorlocal.setNumDocId_Operario(jsonobj.getString("NumDocId_Operario"));
                    proveedorlocal.setPuntaje(jsonobj.getDouble("Puntaje"));

                    JSONArray criterios = jsonobj.getJSONArray("Criterios");
                    List<BE_EncuestaRepuesta> lista = new ArrayList<>();
                    for (int j = 0; j < criterios.length(); j++) {
                        JSONObject jsoncriterio = criterios.getJSONObject(j);
                        BE_EncuestaRepuesta crite = new BE_EncuestaRepuesta();
                        crite.setCriterio(jsoncriterio.getString("Criterio"));
                        crite.setPuntaje(jsoncriterio.getDouble("Puntaje"));
                        lista.add(crite);
                    }
                    proveedorlocal.setCriterios(lista);

                    if (!jsonobj.getString("Foto").equals("")) {


                        /*URL url = new URL(jsonobj.getString("Foto"));
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);*/


                       /* InputStream stream = null;
                        URL url = new URL(jsonobj.getString("Foto"));
                        URLConnection connection = url.openConnection();
                        HttpURLConnection httpConnection = (HttpURLConnection) connection;
                        httpConnection.setRequestMethod("GET");
                        httpConnection.connect();

                        if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            stream = httpConnection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(stream);
                            proveedorlocal.setFoto(myBitmap);
                        }*/

                       /* URL urlConnection = new URL(jsonobj.getString("Foto"));
                        HttpURLConnection connection = (HttpURLConnection) urlConnection
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);*/

                        proveedorlocal.setFoto(jsonobj.getString("Foto"));

                    }

                    lsListadoEstablecimiento.add((proveedorlocal));
                }

                llenarListaProveedorLocal();

                //String strPassword = Util.EncriptarPassword(etContrasenia.getText().toString());
                 /*if (usuario == null) {
                     Toast.makeText(RegVentaActivity.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                 } else if (usuario.getPassword().equals(strPassword)) {
                     Util.BE_DatosUsuario = usuario;
                     Intent intent = new Intent(RegVentaActivity.this, MainActivity.class);
                     startActivity(intent);
                     finish();

                 } else if (usuario.isIndicadorActivo() == false) {
                     Toast.makeText(RegVentaActivity.this, "El Usuario se encuentra Inactivo", Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(RegVentaActivity.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                 }*/

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(BuscarOperadorActivity.this, "Cargando Operarios", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            return new RestClient().get("WSEmpleado.svc/Listar?idservicio=" + Filtro, stringEntity, 30000);


        }
    }


}
