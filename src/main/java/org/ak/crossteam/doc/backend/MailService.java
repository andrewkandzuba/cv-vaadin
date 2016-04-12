package org.ak.crossteam.doc.backend;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class MailService  {

    private String username;
    private String password;
    private String host;
    private int port;

    private MailService() {
        this.username = "andrey.kandzuba@gmail.com";
        this.password = "A6hotdog448BAD";
        this.host = "smtp.googlemail.com";
        this.port = 465;
    }

    private static final Supplier<MailService> SUPPLIER_INSTANCE = Suppliers.memoize(MailService::new);

    public static MailService get() {
        return SUPPLIER_INSTANCE.get();
    }

    public void send(String to, String text) {
        Email email = new SimpleEmail();
        email.setHostName(host);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setSubject("CV response");
        try {
            email.setFrom("no-reply@gmail.com");
            email.setMsg(text);
            email.addTo(to);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
            Throwables.propagateIfPossible(e);
        }
    }
}
