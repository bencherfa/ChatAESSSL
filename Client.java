import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.security.Security;
import com.sun.net.ssl.internal.ssl.Provider;

public class Client
{
    public static void main(String args[])
    {
        //Le numero du port a travers lequel le serveur va accepter les conexions des clients
        int serverPort = 123456;
        //le serveur local
        String serverName = "localhost";
        /*Il faut ajouter le JSSE (Java Secure Socket Extension)qui nous donne les protocoles SSL et TLS ainsi que les fonctionnalites pour le cryptage/decryptage de donnnee*/
        Security.addProvider(new Provider());
        //Mon trousseau des cles SSL
        System.setProperty("javax.net.ssl.trustStore","macle.jts");
        //Le mot de passe du trousseau
        System.setProperty("javax.net.ssl.trustStorePassword","benacer123");
        System.setProperty("javax.net.debug","all");
        String macle="macle";
        try
        {
        	//La creation du socket SSL qui se fait grace au SSLSocketFactory
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(serverName,serverPort);
            // OutputStream pour l'envoi des messages au serveur 
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
            // InputStream pour la reception des messages
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
            System.out.println(inputStream.readUTF());
            //pour s'assurer que la connexion sera toujours etablie jusqua ce que l'utilisateur demande la fermeture
            while (true)
            {
                System.out.println("Votre message : ");
                String messageToSend = System.console().readLine();//lecture du message
                messageToSend=AES.encrypt(messageToSend, macle);//cryptage AES
                outputStream.writeUTF(messageToSend);//envoi
                System.err.println(inputStream.readUTF());
                if(messageToSend.equals("fermer"))
                {
                    break;
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Une erreur est survenue : "+ex.toString());
        }
    }
}