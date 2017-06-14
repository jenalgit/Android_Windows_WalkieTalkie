package brunoricardo.android_windows_walkietalkie;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by bruno on 03/06/2017.
 */

public class SendMessage extends Thread {
    String Number,Msg;
    public SendMessage(String Numero,String Mensagem){
        this.Number=Numero;
        this.Msg=Mensagem;
    }

    @Override
    public void run() {

        try {
            System.out.println("Starting Connection");
            Socket s = new Socket("192.168.1.11", 4321);
            System.out.println("Connection DONE");
            DataInputStream dis=new DataInputStream((s.getInputStream()));
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            String text=Number+" \r\n "+ Msg;
            dos.writeUTF(text);
            dos.flush();
            dos.close();
            s.close();
            System.out.println("Closing socket");                        
        } catch (UnknownHostException e){
            Log.d("Teste","There was an Unknown Erorr:");
            Log.d("Teste",e.getMessage());
        } catch (IOException e) {
            Log.d("Teste","There was an IOException:");
            Log.d("Teste",e.getMessage());
        }
    }
}
