package yei.poli.edu.botonpanico.util;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class AdminPreferencias {

    /** preferencias **/
    private SharedPreferences preferenciasBP;
    private SharedPreferences.Editor editor;
    private Activity activity;

    // constructor
    public AdminPreferencias (Activity activity) {
        this.activity = activity;
        inicializarPreferencias();
    }


    // inicializa preferencias
    public void inicializarPreferencias() {
        preferenciasBP = this.activity.getSharedPreferences(Constantes.PREFS_BP, 0);
    }

    // guarda preferencia
    public void guardarValor(String clave, String valor) {

        editor = preferenciasBP.edit();

        switch (clave) {
            case Constantes.ACTIVO :
                editor.putString(Constantes.ACTIVO, valor);
                break;
            case Constantes.ENVIAR_MENSAJE :
                editor.putString(Constantes.ENVIAR_MENSAJE, valor);
                break;
            case Constantes.ENVIAR_CORREO :
                editor.putString(Constantes.ENVIAR_CORREO, valor);
                break;
            case Constantes.ADJUNTAR_IMAGEN :
                editor.putString(Constantes.ADJUNTAR_IMAGEN, valor);
                break;
            case Constantes.ADJUNTAR_AUDIO :
                editor.putString(Constantes.ADJUNTAR_AUDIO, valor);
                break;
            case Constantes.CONTACTOS :
                editor.putString(Constantes.CONTACTOS, valor);
                break;
            case Constantes.HISTORIAS :
                editor.putString(Constantes.HISTORIAS, valor);
                break;
        }

        editor.commit();

    }

    // recupera preferencia
    public String obtenerValor(String clave) {

        switch (clave) {
            case Constantes.ACTIVO :
                return preferenciasBP.getString(Constantes.ACTIVO, "N");
            case Constantes.ENVIAR_MENSAJE :
                return preferenciasBP.getString(Constantes.ENVIAR_MENSAJE, "N");
            case Constantes.ENVIAR_CORREO :
                return preferenciasBP.getString(Constantes.ENVIAR_CORREO, "N");
            case Constantes.ADJUNTAR_IMAGEN :
                return preferenciasBP.getString(Constantes.ADJUNTAR_IMAGEN, "N");
            case Constantes.ADJUNTAR_AUDIO :
                return preferenciasBP.getString(Constantes.ADJUNTAR_AUDIO, "N");
            case Constantes.CONTACTOS :
                return preferenciasBP.getString(Constantes.CONTACTOS, "0");
            case Constantes.HISTORIAS :
                return preferenciasBP.getString(Constantes.HISTORIAS, "0");
        }

        return null;

    }
}
