/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author MSI
 */
public class MailHelper {

    public final String apikey = "e2729b42f5122c1b2e2422c33ba8d913";

    public String checkEmail(String email) throws Exception {

        String url = "https://apilayer.net/api/check?access_key=" + apikey + "&email=" + email + "&smtp=1&format=1";

        URL urlobj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) urlobj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/17.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        List response = new ArrayList();

        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();

        String result = (String) response.get(7);

        return result.split(":")[1].replace(",", "");
    }

    public void sendMail(String to,String sub,String content) throws AddressException, MessagingException {
        String user = "laptrinhcity@gmail.com";
        String pass = "trananh81095";

//        try {
            Properties mailServerProperties = null;
            Session getMailSession = null;
            MimeMessage mailMessage = null;
            MimeBodyPart contentPart = null;
            MimeBodyPart filePart = null;
            File file = null;
            MimeMultipart multiPart = null;

            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", 587);
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");

            getMailSession = Session.getDefaultInstance(mailServerProperties, new Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                    return new javax.mail.PasswordAuthentication(user, pass);
                }
            });

            mailMessage = new MimeMessage(getMailSession);
            mailMessage.setFrom(new InternetAddress(user));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mailMessage.setSubject(sub);
            mailMessage.setText(content);

            javax.mail.Transport.send(mailMessage);
//        } catch (AddressException ex) {
//            Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, null, ex);
////            JOptionPane.showMessageDialog(null, "Vui lòng nhập địa chỉ email người nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        } catch (MessagingException ex) {
//            Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, null, ex);
////            JOptionPane.showMessageDialog(null, "Đăng nhập email thất bại! Kết thúc chương trình", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
    }
}
