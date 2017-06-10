package yei.poli.edu.botonpanico.util;


import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import yei.poli.edu.botonpanico.R;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class Correo {

    private Activity activity;

    // constructor
    public Correo (Activity activity) {
        this.activity = activity;
    }

    Session session;

    public void enviarCorreo(String destinatario, String body) {
        final String correo = "contacto.sosapp@gmail.com";
        final String pass = "boton2017";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties prop = new Properties();
        prop.put("mail.smtp.host","smtp.googlemail.com");
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.port","465");

        try {
            session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, pass);
                }
            });

            if (session!=null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject(activity.getResources().getString(R.string.ayuda));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                message.setContent(body,"text/html; charset=utf-8");

                Transport.send(message);
            }
        }
        catch (Exception ex){
            Log.d("***Error***",ex.getMessage().toString());
            ex.printStackTrace();
        }

    }

    public String armarCuerpo (String nombre, String ubicacion) {
        String mensaje = activity.getResources().getString(R.string.hola)+ "\n";
        mensaje += activity.getResources().getString(R.string.tuamigo)+ " ";
        mensaje += nombre;
        mensaje += activity.getResources().getString(R.string.cuerpoMensaje)+ " ";
        mensaje += ubicacion;

        return mensaje;
    }

}
