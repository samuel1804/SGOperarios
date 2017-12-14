package pe.com.hatunsol.ferreterias.adapter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.com.hatunsol.ferreterias.BuscarProveedorActivity;
import pe.com.hatunsol.ferreterias.BuscarSupervisorActivity;
import pe.com.hatunsol.ferreterias.R;
import pe.com.hatunsol.ferreterias.entity.BE_Encuesta;
import pe.com.hatunsol.ferreterias.entity.RestResult;
import pe.com.hatunsol.ferreterias.entity.Supervisor;
import pe.com.hatunsol.ferreterias.rest.RestClient;
import pe.com.hatunsol.ferreterias.utilitario.DownloadFile;
import pe.com.hatunsol.ferreterias.utilitario.ImageLoader;

/**
 * Created by Vladimir on 24/02/2015.
 */
public class MainAdapterListaComentario extends ArrayAdapter<BE_Encuesta> {
    private Context myContext;


    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    Uri imageUri;

    public MainAdapterListaComentario(Context context, int resource, List<BE_Encuesta> objects) {
        super(context, resource, objects);
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainHolderSupervisor mainHolderProveedorLocal = null;
        if (convertView == null || !(convertView.getTag() instanceof MainHolderSupervisor)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_comentarios, parent, false);
            mainHolderProveedorLocal = new MainHolderSupervisor();
            mainHolderProveedorLocal.tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
            mainHolderProveedorLocal.tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);
            mainHolderProveedorLocal.etComentario = (EditText) convertView.findViewById(R.id.etComentario);

            convertView.setTag(mainHolderProveedorLocal);
        } else {
            mainHolderProveedorLocal = (MainHolderSupervisor) convertView.getTag();
        }

        final BE_Encuesta supervisor = getItem(position);

        if (supervisor != null) {
            mainHolderProveedorLocal.tvNombre.setText(supervisor.getNombre());
            mainHolderProveedorLocal.tvFecha.setText(supervisor.getFecha());
            mainHolderProveedorLocal.etComentario.setText(supervisor.getComentario());
        } else {
            mainHolderProveedorLocal.tvNombre.setText("");
            mainHolderProveedorLocal.tvFecha.setText("");
            mainHolderProveedorLocal.etComentario.setText("");

        }

        return convertView;
    }


    public class Click_Foto implements View.OnClickListener {
        int IdSupervisor;
        String Ruta;

        public Click_Foto(int IdSupervisor, String Ruta) {
            this.IdSupervisor = IdSupervisor;
            this.Ruta = Ruta;
        }

        @Override
        public void onClick(View v) {
            selectImage(this.IdSupervisor, this.Ruta);
        }
    }


    private void selectImage(final int IdSupervisor, final String Ruta) {
        final ActionBarActivity mycontext = (ActionBarActivity) myContext;
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext);
        builder.setTitle("Seleccionar Opción");
        final CharSequence[] items;
        List<String> listItems = new ArrayList<String>();
        if (Ruta == null) {
            listItems.add("Tomar Foto");
            listItems.add("Imagen de Galería");
            items = listItems.toArray(new CharSequence[listItems.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Tomar Foto")) {
                        String fileName = "Hatunsol.jpg";
                        //create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        //values.put(MediaStore.Images.Media.TITLE, fileName);
                        //values.put(MediaStore.Images.Media.DESCRIPTION,"Imagen Capturada por Hatun Movil");

                        imageUri = mycontext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        takePicture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);


                        ((BuscarSupervisorActivity) mycontext).IdSupervisor = IdSupervisor;
                        ((BuscarSupervisorActivity) mycontext).imageUri = imageUri;
                        mycontext.startActivityForResult(takePicture, REQUEST_CAMERA);

                    } else if (items[item].equals("Imagen de Galería")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");

                        ((BuscarSupervisorActivity) mycontext).IdSupervisor = IdSupervisor;
                        mycontext.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                }
            });
        } else {
            listItems.add("Ver Imagen");
            listItems.add("Tomar Foto");
            listItems.add("Imagen de Galería");
            listItems.add("Quitar Foto");
            items = listItems.toArray(new CharSequence[listItems.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Ver Imagen")) {

                        new DownloadFile(myContext).execute(Ruta, "Supervisor_"+IdSupervisor+".jpg");
                    } else if (items[item].equals("Tomar Foto")) {
                        String fileName = "Hatunsol.jpg";
                        //create parameters for Intent with filename
                        ContentValues values = new ContentValues();
                        //values.put(MediaStore.Images.Media.TITLE, fileName);
                        //values.put(MediaStore.Images.Media.DESCRIPTION,"Imagen Capturada por Hatun Movil");

                        imageUri = mycontext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        takePicture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);


                        ((BuscarSupervisorActivity) mycontext).IdSupervisor = IdSupervisor;
                        ((BuscarSupervisorActivity) mycontext).imageUri = imageUri;
                        mycontext.startActivityForResult(takePicture, REQUEST_CAMERA);

                    } else if (items[item].equals("Imagen de Galería")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");

                        ((BuscarSupervisorActivity) mycontext).IdSupervisor = IdSupervisor;
                        mycontext.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else if (items[item].equals("Quitar Foto")) {
                        new QuitarFoto(IdSupervisor, mycontext).execute();
                    }
                }
            });
        }


        builder.show();
    }

    public class QuitarFoto extends AsyncTask<Void, Void, RestResult> {
        int ProveedorLocalId;
        Context context;

        public QuitarFoto(int ProveedorLocalId, Context context) {
            this.ProveedorLocalId = ProveedorLocalId;
            this.context = context;

        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            super.onPostExecute(restResult);
            try {
                BuscarProveedorActivity mycontext = (BuscarProveedorActivity) myContext;

                mycontext.onRefresh();
                //((AuditoriaEstablecimientoActivity) mycontext.getSupportFragmentManager().getFragments().get(2)).onRefresh();
                JSONObject jso = new JSONObject(restResult.getResult());
                if (jso.getString("EliminarPerfilResult").equals("Ok")) {
                    Toast.makeText(context, "La Foto de Perfil fue Eliminada", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Error al Eliminar la Foto de Perfil", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al Eliminar la Foto de Perfil", Toast.LENGTH_SHORT).show();
            }


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected RestResult doInBackground(Void... params) {
            StringEntity stringEntity = null;

            return new RestClient().get("DocumentoAdjuntoProveedor.svc/EliminarPerfil?ProveedorLocalId=" + ProveedorLocalId, stringEntity, 30000);


        }
    }

    public class Click_Grilla implements View.OnClickListener {
        int IdSupervisor;
        String NombreSupervisor;

        public Click_Grilla(int IdSupervisor, String NombreSupervisor) {
            this.IdSupervisor = IdSupervisor;
            this.NombreSupervisor = NombreSupervisor;

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getContext(),"Hi",Toast.LENGTH_SHORT).show();
           /* ActionBarActivity mycontext = (ActionBarActivity) myContext;

            OpcionesDialogfragment opcionesDialogfragment = new OpcionesDialogfragment();
            Bundle bundle = new Bundle();*/
            /*bundle.putInt("ExpedienteCreditoId", ExpedienteCreditoId);
            bundle.putInt("ProcesoId", ProcesoId);
            bundle.putString("DNI", DNI);
            bundle.putString("NombreCompleto", NombreCompleto);*/
            /*opcionesDialogfragment.setArguments(bundle);
            opcionesDialogfragment.setmOpcionesDialogfragmentListener(MainAdapterListaEstablecimiento.this);
            opcionesDialogfragment.show(mycontext.getSupportFragmentManager(), opcionesDialogfragment.TAG);*/
            /*Intent intent = new Intent(getContext(), PlanTrabajoActivity.class);
            intent.putExtra("IdSupervisor", IdSupervisor);
            intent.putExtra("NombreSupervisor", NombreSupervisor);
            myContext.startActivity(intent);*/
        }


    }

    static class MainHolderSupervisor {
        TextView tvNombre, tvFecha;
        EditText etComentario;

    }

}
