package egate.digital.fasotour.services.valide;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import egate.digital.fasotour.model.Utilisateur;
import egate.digital.fasotour.model.valide.Validate;
import egate.digital.fasotour.repository.valide.ValidateRepository;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
public class ValidateService {

    private ValidateRepository validateRepository;
    private  NotificationService notificationService;

    public void enregistrer(Utilisateur utilisateur){
        Validate validate = new Validate();
        validate.setUtilisateur(utilisateur);
        Instant created = Instant.now();
        validate.setCreated(created);
        Instant expired = created.plus(10, MINUTES);
        validate.setExpired(expired);

        Random random = new Random();
        int randomIntrger = random.nextInt(999999);
        String code = String.format("%06d",randomIntrger);

        validate.setCode(code);

        this.validateRepository.save(validate);
        //this.notificationService.sendNot(validate);
    }

    //
    public Validate redCode(String code){
        return this.validateRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException(("Votre code est invalide")));
    }

}
