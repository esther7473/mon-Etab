package ci.ada.monetabv2new.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sendSimpleEmail(String to, String subject, String text);
    public void sendStudentConfirmation(String email, String nom, String prenom,
                                        String login, String password, String classe, String matricule);
    public void sendTeacherConfirmation(String email, String nom, String prenom,
                                        String login, String password, String matiere);
    public void sendPasswordReset(String email, String nom, String prenom, String resetToken);


}
