import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;
import com.sun.net.ssl.internal.ssl.Provider;

public class Server 
{
    public static void main(String args[])
    {
        //Le numero du port de connexion sur lequel le serveur va acceThe Port number through which this server will accept client connections
        int port = 123456;
        /*Il faut ajouter le JSSE (Java Secure Socket Extension)qui nous donne les protocoles SSL et TLS ainsi que les fonctionnalites pour le cryptage/decryptage de donnnee*/
        Security.addProvider(new Provider());
      //Mon trousseau des cles SSL
        System.setProperty("javax.net.ssl.keyStore","montrousseaudecle.jks");
      //Le mot de passe du trousseau
        System.setProperty("javax.net.ssl.keyStorePassword","benacer123");
        System.setProperty("javax.net.debug","all");
        String macle="macle";
        try
        {
        	//La creation du socket SSL qui se fait grace au SSLSocketFactory
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(port);
            System.out.println("Le serveur est pret");
            //Attente du client SSL pour la connexion au serveur
            SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
         // DataIntputStream pour la reception des messages du client 
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
         // DataOutputStream pour l'envoi des messages au client 
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
            outputStream.writeUTF("Bienvenue a l'application de chat, veuillez envoyer un message");
            //Tant que le client ne ferme pas la connexion le serveur continuera a recevoir les messages
            while(true)
            {
                String recivedMessage = inputStream.readUTF();//reception du message
                recivedMessage=AES.decrypt(recivedMessage,macle);//decryptage AES
                System.out.println("Le client : " + recivedMessage);
                if(recivedMessage.equals("fermer"))
                {
                    outputStream.writeUTF("Au revoir");
                    outputStream.close();
                    inputStream.close();
                    sslSocket.close();
                    sslServerSocket.close();
                    break;
                }
                else
                {
                    outputStream.writeUTF("Vous aviez envoye : "+recivedMessage);
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Une erreur est survenue: "+ex.toString());
        }
    }
}

