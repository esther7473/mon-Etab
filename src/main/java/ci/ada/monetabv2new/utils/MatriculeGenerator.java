package ci.ada.monetabv2new.utils;

import java.time.Year;
import java.util.Random;

public final class MatriculeGenerator {

    private static final Random random = new Random();

    private MatriculeGenerator() {
    }

    public static String generate(int anneeInscription, String codeEtab) {
        String annee = String.valueOf(anneeInscription).substring(2);

        int numero = 1000 + random.nextInt(9000);

        return annee + codeEtab.toUpperCase() + numero;
    }

    public static String generateForCurrentYear(String codeEtab) {
        int currentYear = Year.now().getValue();
        return generate(currentYear, codeEtab);
    }
}
