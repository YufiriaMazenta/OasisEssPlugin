package top.oasismc.api.email;

import com.sun.mail.util.MailSSLSocketFactory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.oasismc.OasisEss;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static top.oasismc.OasisEss.*;

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

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 7));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
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

                    String text = getTextConfig().getConfig().getString("auth.emailFormat", "%code%");
                    text = text.replace("%code%", code).replace("%player%", player.getName());
                    message.setText(text);
                    Transport.send(message);
                    info("Send Email to " + mail + " successfully");
                }catch (MessagingException mex) {
                    mex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(OasisEss.getPlugin());
        return String.valueOf(code);
    }

}
