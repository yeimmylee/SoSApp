package yei.poli.edu.botonpanico;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;
import yei.poli.edu.botonpanico.util.ManejoContactos;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class ConfiguracionBPActivity extends AppCompatActivity {

    /** variable para los logs **/
    private static final String TAG = ConfiguracionBPActivity.class.getSimpleName();

    /** elementos de la vista **/
    private Switch btnEnviarMensaje;
    private Switch btnEnviarCorreo;
    private CheckBox chkAdjuntarAudio;
    private CheckBox chkAdjuntarImagenes;
    private LinearLayout contacto1;
    private LinearLayout contacto2;
    private LinearLayout contacto3;
    private LinearLayout contacto4;

    /** preferencias **/
    private AdminPreferencias adminPreferencias;


    /**  contactos */
    ManejoContactos manejoContactos;

    /** menú contextual */
    private ActionMode mActionMode;
    private int contactoActual;

    /*** pruebas menu popup */
    private Context mContext;
    private PopupMenu popupMenu;
    private boolean editarContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_bp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // inicializa preferencias
        adminPreferencias = new AdminPreferencias(this);
        editarContacto = false;

        // inicializa objeto de manejo de contactos
        manejoContactos = new ManejoContactos(this);

        // inicializa vista
        inicializarBtnEnviarMensaje();
        inicializarBtnEnviarCorreo();
        inicializarChkAdjuntarAudio();
        inicializarChkAdjuntarImagenes();
        inicializarMenuContextual();
        inicializarContactos();

        /*** para el menu popup */
        mContext = getApplicationContext();

        /************************************/
        //verTodos();

    }

    /**********************************************************************************************/
    /************************** PROCESA OBJETOS DE LA VISTA ***************************************/
    /**********************************************************************************************/

    // Inicializa botón enviar mensaje
    private void inicializarBtnEnviarMensaje () {

        btnEnviarMensaje = (Switch) findViewById(R.id.enviarMensaje);

        //Se le agrega el listener para que opere según la selección del usuario
        btnEnviarMensaje.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ENVIAR_MENSAJE, "S");
                    validarPermisosSMS();
                }else{
                    adminPreferencias.guardarValor(Constantes.ENVIAR_MENSAJE, "N");
                }
            }
        });

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ENVIAR_MENSAJE).equals("S")) {
            btnEnviarMensaje.setChecked(true);
        } else {
            btnEnviarMensaje.setChecked(false);
        }
    }

    // Inicializa botón enviar correos
    private void inicializarBtnEnviarCorreo () {

        btnEnviarCorreo = (Switch) findViewById(R.id.enviarCorreo);

        //Se le agrega el listener para que opere según la selección del usuario
        btnEnviarCorreo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ENVIAR_CORREO, "S");
                }else{
                    adminPreferencias.guardarValor(Constantes.ENVIAR_CORREO, "N");
                }
            }
        });

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ENVIAR_CORREO).equals("S")) {
            btnEnviarCorreo.setChecked(true);
        } else {
            btnEnviarCorreo.setChecked(false);
        }
    }

    // Inicializa checkBox adjuntar audio
    private void inicializarChkAdjuntarAudio () {

        chkAdjuntarAudio = (CheckBox) findViewById(R.id.adjuntarAudio);

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ADJUNTAR_AUDIO).equals("S")) {
            chkAdjuntarAudio.setChecked(true);
        } else {
            chkAdjuntarAudio.setChecked(false);
        }
    }

    // Inicializa checkBox adjuntar imagenes
    private void inicializarChkAdjuntarImagenes () {

        chkAdjuntarImagenes = (CheckBox) findViewById(R.id.adjuntarImagenes);

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ADJUNTAR_IMAGEN).equals("S")) {
            chkAdjuntarImagenes.setChecked(true);
        } else {
            chkAdjuntarImagenes.setChecked(false);
        }
    }

    // listener de los checkbox
    public void onCheckboxClicked(View view) {

        // averigua si fue marcado o desmarcado
        boolean checked = ((CheckBox) view).isChecked();

        // verifica cuál checkbox fue modificado
        switch(view.getId()) {
            case R.id.adjuntarAudio:
                if (checked)
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_AUDIO, "S");
                else
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_AUDIO, "N");
                break;
            case R.id.adjuntarImagenes:
                if (checked)
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_IMAGEN, "S");
                else
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_IMAGEN, "N");
                break;
        }
    }

    // Inicializa checkBox adjuntar imagenes
    private void inicializarContactos () {

        //int cant = Integer.parseInt(adminPreferencias.obtenerValor(Constantes.CONTACTOS));
        //Log.d(TAG, "cant: " + cant);
        for (int i = 1; i <= 4; i++) {
            String uri = adminPreferencias.obtenerValor(Constantes.CONTACTO+i);
            Log.d(TAG, "uri: " + uri);
            if(uri != null) {
                manejoContactos.uriContact = Uri.parse(uri);
                contactoActual = i;

                manejoContactos.consultarIdContacto();
                manejoContactos.consultarNombreContacto();
                manejoContactos.consultarCorreoContacto();
                manejoContactos.consultarTelefonoContacto();
                mostrarInfoContacto();
            } else {
                contactoActual = i;
                borrarInfoContacto();
            }
        }
    }

    public void inicializarMenuContextual() {

        getSupportActionBar();

        contacto1 = (LinearLayout) findViewById(R.id.contacto1);
        contacto1.setOnTouchListener(new AdministraTouch());

        contacto2 = (LinearLayout) findViewById(R.id.contacto2);
        contacto2.setOnTouchListener(new AdministraTouch());

        contacto3 = (LinearLayout) findViewById(R.id.contacto3);
        contacto3.setOnTouchListener(new AdministraTouch());

        contacto4 = (LinearLayout) findViewById(R.id.contacto4);
        contacto4.setOnTouchListener(new AdministraTouch());


    }

    class AdministraTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    //Log.d(TAG, "La accion ha sido ABAJO");

                        switch (v.getId()) {

                            case R.id.contacto1:

                                popupMenu = new PopupMenu(mContext, contacto1);
                                contactoActual = 1;
                                break;

                            case R.id.contacto2:

                                popupMenu = new PopupMenu(mContext, contacto2);
                                contactoActual = 2;
                                break;

                            case R.id.contacto3:

                                popupMenu = new PopupMenu(mContext, contacto3);
                                contactoActual = 3;
                                break;

                            case R.id.contacto4:

                                popupMenu = new PopupMenu(mContext, contacto4);
                                contactoActual = 4;
                                break;
                        }
                        mostrarOpcionesContacto();

                    return true;
                default:
                    return true;
            }
        }
    }

    public void mostrarOpcionesContacto(){

        if(adminPreferencias.obtenerValor(Constantes.CONTACTO+contactoActual) == null) {
            agregarContacto(null);
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch(menuItem.getItemId()){
                        case R.id.borrar:
                            adminPreferencias.eliminarContacto(contactoActual);
                            inicializarContactos ();
                            return true;
                        case R.id.actualizar:
                            editarContacto = true;
                            agregarContacto(null);
                            return true;
                        default:
                            return false;
                    }
                }
            });

            popupMenu.show();
        }





/*
        if(adminPreferencias.obtenerValor(Constantes.CONTACTO+contactoActual) == null) {
            cerrarMenuContextual();
            agregarContacto(null);
        } else {
            mActionMode = ConfiguracionBPActivity.this.startActionMode(new ActionBarCallBack());
        }*/
    }

    private void cerrarMenuContextual (){
        if(mActionMode != null )
            mActionMode.finish();
    }

    /**********************************************************************************************/
    /************************** PERMISOS **********************************************************/
    /**********************************************************************************************/

    public void validarPermisosSMS(){

        //Se valida inicialmente si tenemos permisos para acceder a los contactos
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //Aquí solicita permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},Constantes.MY_PERMISSIONS_REQUEST_SEND_SMS);
            //después de solicitar los permisos llama al método: onRequestPermissionsResult, para proceder de acuerdo a la respuesta del usuario.
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constantes.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // Si la solicitud fue cancelada, el resultado es un arreglo vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El usuario otorgó los permisos
                    cargarContactos();
                } else {

                    // El usuario negó los permisos
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msjPermisos), Toast.LENGTH_LONG).show();
                }
                return;
            }
            case Constantes.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // Si la solicitud fue cancelada, el resultado es un arreglo vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El usuario otorgó los permisos

                } else {

                    // El usuario negó los permisos
                    adminPreferencias.guardarValor(Constantes.ENVIAR_MENSAJE, "N");
                    btnEnviarMensaje.setChecked(false);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msjPermisos), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**********************************************************************************************/
    /************************** ADMINISTRACIÓN DE CONTACTOS ***************************************/
    /**********************************************************************************************/

    public void agregarContacto(View v){

        //Se valida inicialmente si tenemos permisos para acceder a los contactos
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Aquí solicita permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},Constantes.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            //después de solicitar los permisos llama al método: onRequestPermissionsResult, para proceder de acuerdo a la respuesta del usuario.
        } else {
            //Si tenemos permisos continúa con el proceso normal
            cargarContactos();
        }

    }

    public void cargarContactos() {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), Constantes.REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constantes.REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            //Log.d(TAG, "Response: " + data.toString());
            manejoContactos.uriContact = data.getData();
            //Log.d(TAG, "uri: " + uriContact.toString());
            if(editarContacto) {
                contactoActual =  adminPreferencias.editarContacto(manejoContactos.uriContact.toString(), contactoActual);
            } else {
                contactoActual =  adminPreferencias.agregarContacto(manejoContactos.uriContact.toString());
            }
            editarContacto = false;
            if(contactoActual > 0) {
                manejoContactos.consultarIdContacto();
                manejoContactos.consultarNombreContacto();
                manejoContactos.consultarCorreoContacto();
                manejoContactos.consultarTelefonoContacto();
                mostrarInfoContacto();
            }

        }
    }

    private void mostrarInfoContacto() {

        Bitmap photo = null;
        TextView nombreContacto = null;
        TextView telefonoContacto = null;
        TextView correoContacto = null;
        ImageView imageView = null;

        switch (contactoActual) {
            case 1 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto1);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto1);
                correoContacto = (TextView) findViewById(R.id.correoContacto1);
                imageView = (ImageView) findViewById(R.id.imagenContacto1);
                break;
            case 2 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto2);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto2);
                correoContacto = (TextView) findViewById(R.id.correoContacto2);
                imageView = (ImageView) findViewById(R.id.imagenContacto2);
                break;
            case 3 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto3);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto3);
                correoContacto = (TextView) findViewById(R.id.correoContacto3);
                imageView = (ImageView) findViewById(R.id.imagenContacto3);
                break;
            case 4 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto4);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto4);
                correoContacto = (TextView) findViewById(R.id.correoContacto4);
                imageView = (ImageView) findViewById(R.id.imagenContacto4);
                break;
        }

        nombreContacto.setText(manejoContactos.contactName);
        telefonoContacto.setText(manejoContactos.contactNumber);
        correoContacto.setText(manejoContactos.contactEmail);
        imageView.setImageResource(R.mipmap.ic_launcher);

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(manejoContactos.contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                //ImageView imageView = (ImageView) findViewById(R.id.imagenContacto1);
                imageView.setImageBitmap(photo);
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void borrarInfoContacto() {

        Bitmap photo = null;
        TextView nombreContacto = null;
        TextView telefonoContacto = null;
        TextView correoContacto = null;
        ImageView imageView = null;

        switch (contactoActual) {
            case 1 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto1);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto1);
                correoContacto = (TextView) findViewById(R.id.correoContacto1);
                imageView = (ImageView) findViewById(R.id.imagenContacto1);
                break;
            case 2 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto2);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto2);
                correoContacto = (TextView) findViewById(R.id.correoContacto2);
                imageView = (ImageView) findViewById(R.id.imagenContacto2);
                break;
            case 3 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto3);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto3);
                correoContacto = (TextView) findViewById(R.id.correoContacto3);
                imageView = (ImageView) findViewById(R.id.imagenContacto3);
                break;
            case 4 :
                nombreContacto = (TextView) findViewById(R.id.nombreContacto4);
                telefonoContacto = (TextView) findViewById(R.id.telefonoContacto4);
                correoContacto = (TextView) findViewById(R.id.correoContacto4);
                imageView = (ImageView) findViewById(R.id.imagenContacto4);
                break;
        }
        if(contactoActual > 0) {
            nombreContacto.setText("");
            telefonoContacto.setText(getResources().getString(R.string.sinContacto));
            correoContacto.setText("");
            imageView.setImageResource(R.mipmap.ic_launcher);
        }


    }

    public void verTodos(){
        Log.d(TAG, "Cantidad: " + adminPreferencias.obtenerValor(Constantes.CONTACTOS));
        Log.d(TAG, "1: " + adminPreferencias.obtenerValor(Constantes.CONTACTO1));
        Log.d(TAG, "2: " + adminPreferencias.obtenerValor(Constantes.CONTACTO2));
        Log.d(TAG, "3: " + adminPreferencias.obtenerValor(Constantes.CONTACTO3));
        Log.d(TAG, "4: " + adminPreferencias.obtenerValor(Constantes.CONTACTO4));


    }


   /* class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_configuracion_b, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //mode.setTitle(getResources().getString(R.string.menuContacto));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }*/

}
