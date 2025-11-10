package ci.ada.monetabv2new.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PasswordUtils {

    /**
     * Génère un mot de passe au format: datedenaissance+initialNom+initialPrenom
     * Exemple: 25012003BG (25/01/2003 + B + G pour Bernard Guedj)
     *
     * @param birthDate la date de naissance
     * @param nom le nom de famille
     * @param prenom le prénom
     * @return le mot de passe généré
     */
    public static String generatePasswordFromBirthDate(LocalDate birthDate, String nom, String prenom) {
        if (birthDate == null || nom == null || prenom == null ||
                nom.isBlank() || prenom.isBlank()) {
            throw new IllegalArgumentException("Tous les paramètres doivent être non nuls et non vides");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = birthDate.format(formatter);

        String initialNom = nom.substring(0, 1).toUpperCase();

        String initialPrenom = prenom.substring(0, 1).toUpperCase();

        return datePart + initialNom + initialPrenom;
    }


    public static String generatePasswordFromBirthDate(String birthDateString, String nom, String prenom) {
        if (birthDateString == null || birthDateString.isBlank()) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être nulle");
        }

        LocalDate birthDate = LocalDate.parse(birthDateString);
        return generatePasswordFromBirthDate(birthDate, nom, prenom);
    }

    public static String generateValidatedPassword(LocalDate birthDate, String nom, String prenom) {
        String password = generatePasswordFromBirthDate(birthDate, nom, prenom);


        return password;
    }
}