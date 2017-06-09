package yei.poli.edu.botonpanico.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import yei.poli.edu.botonpanico.R;

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
        editor.putString(clave, valor);
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
            case Constantes.CONTACTO1 :
                return preferenciasBP.getString(Constantes.CONTACTO1, null);
            case Constantes.CONTACTO2 :
                return preferenciasBP.getString(Constantes.CONTACTO2, null);
            case Constantes.CONTACTO3 :
                return preferenciasBP.getString(Constantes.CONTACTO3, null);
            case Constantes.CONTACTO4 :
                return preferenciasBP.getString(Constantes.CONTACTO4, null);
        }

        return null;

    }

    //elimina preferencia
    public void eliminarValor(String clave) {

        editor = preferenciasBP.edit();
        editor.remove(clave);
        editor.commit();

    }

    /**********************************************************************************************/
    /************************** ADMINISTRAR CONTACTOS *********************************************/
    /**********************************************************************************************/

    // guarda un contacto en el archivo de preferencias y devuelve la posición
    public int agregarContacto (String valor) {

        // incrementar el contador
        int pos = Integer.parseInt(obtenerValor(Constantes.CONTACTOS)) + 1;

        // guardar el valor en la siguiente posición
        if(pos > 4) {
            Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.max4contactos), Toast.LENGTH_LONG).show();
            return -1;
        } else {

            //valida que el contacto no exísta
            if(!contactoExiste(valor)) {
                // actualiza la cantidad de contactos configurados
                guardarValor(Constantes.CONTACTOS, String.valueOf(pos));
                // guarda el valor en las preferencias
                guardarValor(Constantes.CONTACTO + pos, valor);

            } else {
                return -1;
            }

        }
        return pos;
    }

    //valida que el contacto no exísta
    public boolean contactoExiste(String valor) {

        for (int i = 1; i < 4; i++) {
            String val = obtenerValor(Constantes.CONTACTO+i);
            if(val != null && val.equals(valor)) {
                Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.contactoExiste), Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    public int editarContacto (String valor, int pos) {

        // guardar el valor en la posición recibida como parámetro
        if(pos > 4) {
            Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.max4contactos), Toast.LENGTH_LONG).show();
            return -1;
        } else {
            //valida que el contacto no exísta
            if(!contactoExiste(valor)) {
                guardarValor(Constantes.CONTACTO + pos, valor);
            } else {
                return -1;
            }
        }
        return pos;
    }

    public void eliminarContacto(int pos) {

        if(pos > 4) {
            Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.max4contactos), Toast.LENGTH_LONG).show();
        } else {
            // consultar cantidad actual de contactos
            int cant = Integer.parseInt(obtenerValor(Constantes.CONTACTOS));

            // si la pos no es el último mover los contactos para reorganizarlos
            if(pos < cant) {
                for (int i = pos; i < cant; i++) {
                    guardarValor(Constantes.CONTACTO+i, obtenerValor(Constantes.CONTACTO+(i+1)));
                }
            }

            // borrar el último
            eliminarValor(Constantes.CONTACTO+cant);

            // actualizar el contador
            guardarValor(Constantes.CONTACTOS, String.valueOf(cant-1));

        }
    }

    public String consultarContacto(int pos) {

        return obtenerValor(Constantes.CONTACTO+pos);

    }
}
