package com.example.DigitalWallet.Util;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    public void sendEmail(String receverAdd, String subject, String content) {
        Email email = EmailBuilder.startingBlank()
                .from("chandrashekarrathipelly@gmail.com")
                .to(receverAdd)
                .withSubject(subject)
                .withPlainText(content)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "chandrashekarrathipelly.com", "")
                .buildMailer();

        mailer.sendMail(email, true);

    }

}