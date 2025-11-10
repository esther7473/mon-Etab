package ci.ada.monetabv2new.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("no-reply@monetab.ci");

            mailSender.send(message);
            log.info("Email envoyé à : {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            throw new RuntimeException("Erreur d'envoi d'email", e);
        }

    }

    @Override
    public void sendStudentConfirmation(String email, String nom, String prenom, String login, String password, String classe, String matricule) {
        String subject = "Confirmation d'inscription - Vos accès MonEtab";

        String text = "Bonjour " + prenom + " " + nom + ",\n\n" +
                "Votre inscription à MonEtab a été confirmée avec succès.\n\n" +
                "Voici vos identifiants de connexion :\n" +
                "➤ Identifiant: " + login + "\n" +
                "➤ Mot de passe: " + password + "\n" +
                "➤ Classe: " + classe + "\n\n" +
                "➤ Matricule: " + matricule + "\n\n" +
                "Accédez à votre espace : https://monetab.ci/login\n\n" +
                "Nous vous recommandons de changer votre mot de passe après votre première connexion.\n\n" +
                "Cordialement,\nL'équipe MonEtab";

        sendSimpleEmail(email, subject, text);
    }

    @Override
    public void sendTeacherConfirmation(String email, String nom, String prenom, String login, String password, String matiere) {
        String subject = "Confirmation d'inscription - Vos accès Enseignant MonEtab";

        String text = "Bonjour " + prenom + " " + nom + ",\n\n" +
                "Votre compte enseignant sur MonEtab a été créé avec succès.\n\n" +
                "Voici vos identifiants de connexion :\n" +
                "➤ Identifiant: " + login + "\n" +
                "➤ Mot de passe: " + password + "\n" +
                "➤ Matière: " + matiere + "\n\n" +
                "Accédez à votre espace enseignant : https://monetab.ci/login\n\n" +
                "Fonctionnalités disponibles :\n" +
                "• Gestion des notes\n" +
                "• Suivi des absences\n" +
                "• Emploi du temps\n" +
                "• Communication avec les étudiants\n\n" +
                "Nous vous recommandons de changer votre mot de passe après votre première connexion.\n\n" +
                "Cordialement,\nL'équipe MonEtab";

        sendSimpleEmail(email, subject, text);
    }

    @Override
    public void sendPasswordReset(String email, String nom, String prenom, String resetToken) {
        String subject = "Réinitialisation de votre mot de passe MonEtab";

        String text = "Bonjour " + prenom + " " + nom + ",\n\n" +
                "Vous avez demandé la réinitialisation de votre mot de passe.\n\n" +
                "Voici votre code de réinitialisation : " + resetToken + "\n\n" +
                "Pour réinitialiser votre mot de passe, rendez-vous sur :\n" +
                "https://monetab.ci/reset-password\n\n" +
                "Ce code expirera dans 24 heures.\n\n" +
                "Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet email.\n\n" +
                "Cordialement,\nL'équipe MonEtab";

        sendSimpleEmail(email, subject, text);

    }
}
