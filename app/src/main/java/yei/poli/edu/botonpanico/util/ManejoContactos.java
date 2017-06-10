package yei.poli.edu.botonpanico.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import yei.poli.edu.botonpanico.ConfiguracionBPActivity;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class ManejoContactos {

    // para los contactos
    public Uri uriContact;
    public String contactID;
    public String contactName;
    public String contactEmail;
    public String contactNumber;

    private Activity activity;

    // constructor
    public ManejoContactos (Activity activity) {
        this.activity = activity;
    }

    /** variable para los logs **/
    private static final String TAG = ManejoContactos.class.getSimpleName();


    public void consultarIdContacto() {

        // getting contacts ID
        Cursor cursorID = activity.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.d(TAG, "Contact ID: " + contactID);

    }

    public void consultarNombreContacto() {

        contactName = null;

        Cursor cursor = activity.getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.d(TAG, "Contact Name: " + contactName);
    }

    public void consultarTelefonoContacto() {

        contactNumber = null;

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = activity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    public void consultarCorreoContacto() {

        contactEmail = null;

        // querying contact data store
        Cursor emailCur = activity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);

        while (emailCur.moveToNext()) {
            contactEmail = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emailCur.close();
        Log.d(TAG, "Contact Email: " + contactEmail);

    }

}
