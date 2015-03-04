package network;

import com.sun.deploy.net.URLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by nixo on 3/3/15.
 */
public class Email {

    public static void sendEmail(String email, String subject, String message) throws Exception {
        String url = "http://nixomod.webege.com/webtek/send.php"
                +"?to=" + email
                + "&subject=" + URLEncoder.encode(subject, "ASCII")
                + "&message=" + URLEncoder.encode(message, "ASCII")
                + "&from=f";
        System.out.println("Sending email: "+url);
        openURL(url);
    }

    public static void openURL(String url) throws Exception {
        URL uri = new URL(url);
        URLConnection yc = uri.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        System.out.print("Remote server replied: ");
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);

        in.close();
    }
}
