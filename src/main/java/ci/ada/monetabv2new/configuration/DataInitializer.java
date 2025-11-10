package ci.ada.monetabv2new.configuration;


import ci.ada.monetabv2new.models.*;
import ci.ada.monetabv2new.models.enumeration.Poste;
import ci.ada.monetabv2new.models.enumeration.Role;
import ci.ada.monetabv2new.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ClassRepository classRepository;
    private final MatiereRepository matiereRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultUsers();
    }

    private void createDefaultUsers() {
        log.info("Creating default users...");

        createUserIfNotExists("admin", "admin123", Role.ADMIN, "Administrateur", "Système");

        createUserIfNotExists("teacher", "teacher123", Role.TEACHER, "Jean", "Dupont");

        createUserIfNotExists("student", "student123", Role.STUDENT, "Marie", "Martin");

   

        log.info("Default users created successfully!");
        printDefaultCredentials();
    }

    private void createUserIfNotExists(String username, String password, Role role,
                                       String nom, String prenom) {
        if (userRepository.findByUsername(username).isEmpty()) {
            UserAccountEntity user = new UserAccountEntity();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user = userRepository.save(user);

            ClassEntity classEntity = new ClassEntity();

            if (! classRepository.existsByLabel("6eme A")){
                classEntity.setLabel("6eme A");
                classEntity = classRepository.save(classEntity);
            }


            switch (role){
                case ADMIN:{
                    AdminEntity admin = new AdminEntity();
                    admin.setNom(nom);
                    admin.setPrenom(prenom);
                    admin.setPoste(Poste.DIRECTEUR_GENERAL);
                    admin.setEmail(nom+"@monetab.com");
                    admin.setUserAccountEntity(user);
                    adminRepository.save(admin);
                    break;
                }
                case  TEACHER:{
                    TeacherEntity teacher = new TeacherEntity();
                    teacher.setNom(nom);
                    teacher.setPrenom(prenom);
                    teacher.setEmail(nom+"@monetab.com");
                    teacher.setUserAccountEntity(user);

                    List<ClassEntity> classes = new ArrayList<>();
                    classes.add(classEntity);
                    teacher.setClasses(classes);

                    MatiereEntity matiere = new MatiereEntity();
                    matiere.setNom("MATHEMATIQUES");
                    matiere = matiereRepository.save(matiere);
                    teacher.setMatiere(matiere);
                    teacherRepository.save(teacher);
                    break;
                }
                case STUDENT:{
                    StudentEntity student = new StudentEntity();
                    student.setNom(nom);
                    student.setPrenom(prenom);
                    student.setEmail(nom+"@monetab.com");
                    student.setUserAccountEntity(user);
                    student.setMatricule("MONETAB2025");
                    student.setClassEntity(classEntity);
                    studentRepository.save(student);
                    break;
                }
            }



            log.info("Created user: {} with role: {}", username, role.name());
        } else {
            log.info("User {} already exists, skipping...", username);
        }
    }

    private void printDefaultCredentials() {
        log.info("=".repeat(60));
        log.info("DEFAULT CREDENTIALS FOR TESTING:");
        log.info("=".repeat(60));
        log.info("ADMIN     -> Username: admin    | Password: admin123");
        log.info("TEACHER   -> Username: teacher  | Password: teacher123");
        log.info("STUDENT   -> Username: student  | Password: student123");
        log.info("PARENT    -> Username: parent   | Password: parent123");
        log.info("=".repeat(60));
        log.info("Login URL: http://localhost:8080/login");
        log.info("=".repeat(60));
    }
}
