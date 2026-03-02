package egate.digital.fasotour.services.valide;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import egate.digital.fasotour.model.valide.Validate;

@AllArgsConstructor
@Service
public class NotificationService {

    JavaMailSender javaMailSender;
    public void sendNot(Validate validate){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("sawadogsakalo@gmail.com");
            mailMessage.setTo(validate.getUtilisateur().getMail());
            mailMessage.setSubject("Votre Code d'activation");

            String texte = String.format(
                    "Bonjour %s Votre code d'activation est %s ; A bientôt",
                    validate.getUtilisateur().getNomComplet(),
                    validate.getCode()
            );
            mailMessage.setText(texte);
            javaMailSender.send(mailMessage);
            System.out.println("Email envoyé à " +validate.getUtilisateur().getNomComplet());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //"Erreur lors de l'envoi du mail : "
            e.printStackTrace();
        }
    }
}
