package yei.poli.edu.botonpanico.util;

import android.provider.ContactsContract;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class Constantes {

    // nombre archivo de preferencias
    public static final String PREFS_BP = "PreferenciasBP";
    // claves con las que se guardan los valores en las preferencias
    public static final String ACTIVO = "activo";
    public static final String ENVIAR_MENSAJE = "enviarMensaje";
    public static final String ENVIAR_CORREO = "enviarCorreo";
    public static final String ADJUNTAR_IMAGEN = "adjuntarImagen";
    public static final String ADJUNTAR_AUDIO = "adjuntarAudio";
    public static final String CONTACTOS = "contactos";
    public static final String CONTACTO = "contacto";
    public static final String CONTACTO1 = "contacto1";
    public static final String CONTACTO2 = "contacto2";
    public static final String CONTACTO3 = "contacto3";
    public static final String CONTACTO4 = "contacto4";
    public static final String HISTORIAS = "historias";
    // id de la notificación permanente de cuando la aplicación está activada
    public static final int NOTIFICATION_ID = 12345;
    // para los permisos
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    // para los contactos
    public static final int REQUEST_CODE_PICK_CONTACTS = 1;

}
