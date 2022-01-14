package top.oasismc.api.email;

import com.sun.mail.util.MailSSLSocketFactory;
import org.bukkit.entity.Player;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailSender {

    public static String send(Player player, String mail) {
        String from = "oasismc.top@qq.com";
        String host = "smtp.qq.com";  //QQ 邮件服务器

        Properties properties = System.getProperties();


        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("oasismc.top@qq.com", "waawuoqwaebfdabe");
            }
        });

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mail));

            message.setSubject("验证码");

            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                code.append((int) (Math.random() * 7));
            }
            message.setText("尊敬的" + player.getName() + ",你的验证码为" + code);
            Transport.send(message);
//            System.out.println("Send Email to " + mail + " successfully");
            return String.valueOf(code);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return null;
    }

}
