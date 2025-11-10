package ci.ada.monetabv2new.utils;

import ci.ada.monetabv2new.services.dto.CoursDTO;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TimeTableUtils {

    /**
     * Prépare les données communes pour l'emploi du temps
     * @param courses Liste des cours à traiter
     * @param model Modèle Spring MVC
     */
    public void prepareTimeTableData(List<CoursDTO> courses, Model model) {
        // Trier les cours par date puis par horaire
        List<CoursDTO> sortedCourses = sortCourses(courses);

        // Calculer les données supplémentaires
        long totalHours = calculateTotalHours(sortedCourses);
        long differentClasses = countDifferentClasses(sortedCourses);

        // Définir les créneaux horaires
        List<String> timeSlots = getTimeSlots();

        // Jours de la semaine
        List<String> daysOfWeek = getDaysOfWeek();

        // Semaine courante
        String currentWeek = getCurrentWeekString();

        // Créer une grille d'emploi du temps structurée
        Map<String, Map<String, CoursDTO>> timetableGrid = createTimetableGrid(sortedCourses, timeSlots, daysOfWeek);

        // Ajouter au modèle
        model.addAttribute("courses", sortedCourses);
        model.addAttribute("totalHours", totalHours);
        model.addAttribute("differentClasses", differentClasses);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("currentWeek", currentWeek);
        model.addAttribute("timetableGrid", timetableGrid);
    }

    /**
     * Trie les cours par date puis par horaire
     */
    public List<CoursDTO> sortCourses(List<CoursDTO> courses) {
        return courses.stream()
                .sorted(Comparator
                        .comparing(CoursDTO::getDateCours) // d'abord par date
                        .thenComparing(CoursDTO::getHeureDebut) // ensuite par heure de début
                )
                .collect(Collectors.toList());
    }

    /**
     * Calcule le nombre total d'heures de cours
     */
    public long calculateTotalHours(List<CoursDTO> courses) {
        return courses.stream()
                .filter(c -> c.getHeureDebut() != null && c.getHeureFin() != null)
                .mapToLong(c -> Duration.between(c.getHeureDebut(), c.getHeureFin()).toHours())
                .sum();
    }

    /**
     * Compte le nombre de classes différentes
     */
    public long countDifferentClasses(List<CoursDTO> courses) {
        return courses.stream()
                .map(c -> c.getClasse() != null ? c.getClasse().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    /**
     * Retourne les créneaux horaires standards
     */
    public List<String> getTimeSlots() {
        return Arrays.asList(
                "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"
        );
    }

    /**
     * Retourne les jours de la semaine
     */
    public List<String> getDaysOfWeek() {
        return Arrays.asList("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi");
    }

    /**
     * Génère la chaîne de la semaine courante
     */
    public String getCurrentWeekString() {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate friday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        
        return "Semaine du " + monday.format(DateTimeFormatter.ofPattern("dd MMMM")) +
                " au " + friday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    /**
     * Compte le nombre de matières différentes (pour les étudiants)
     */
    public long countDifferentSubjects(List<CoursDTO> courses) {
        return courses.stream()
                .map(c -> c.getEnseignant() != null && c.getEnseignant().getMatiere() != null ? 
                         c.getEnseignant().getMatiere().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    /**
     * Filtre les cours pour une date spécifique
     */
    public List<CoursDTO> getCoursesForDate(List<CoursDTO> courses, LocalDate date) {
        return courses.stream()
                .filter(c -> c.getDateCours() != null && c.getDateCours().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Filtre les cours pour la semaine courante
     */
    public List<CoursDTO> getCoursesForCurrentWeek(List<CoursDTO> courses) {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate friday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        
        return courses.stream()
                .filter(c -> c.getDateCours() != null && 
                           !c.getDateCours().isBefore(monday) && 
                           !c.getDateCours().isAfter(friday))
                .collect(Collectors.toList());
    }

    /**
     * Crée une grille d'emploi du temps structurée
     * Structure: Map<timeSlot, Map<dayName, CoursDTO>>
     */
    public Map<String, Map<String, CoursDTO>> createTimetableGrid(List<CoursDTO> courses, List<String> timeSlots, List<String> daysOfWeek) {
        Map<String, Map<String, CoursDTO>> grid = new LinkedHashMap<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        System.out.println("======DEBUG TIMETABLE GRID CREATION======");
        System.out.println("Courses count: " + (courses != null ? courses.size() : "NULL"));
        System.out.println("Time slots: " + timeSlots);
        System.out.println("Days of week: " + daysOfWeek);
        
        // Initialiser la grille avec des valeurs nulles
        for (String timeSlot : timeSlots) {
            Map<String, CoursDTO> dayMap = new LinkedHashMap<>();
            for (String day : daysOfWeek) {
                dayMap.put(day, null);
            }
            grid.put(timeSlot, dayMap);
        }
        
        // Remplir la grille avec les cours
        for (CoursDTO course : courses) {
            if (course.getDateCours() != null && course.getHeureDebut() != null && course.getHeureFin() != null) {
                // Formater correctement les heures pour correspondre aux créneaux prédéfinis
                String courseTimeSlot = course.getHeureDebut().format(timeFormatter) + "-" + course.getHeureFin().format(timeFormatter);
                String dayName = getDayName(course.getDateCours().getDayOfWeek());
                
                System.out.println("Processing course: " + courseTimeSlot + " on " + dayName);
                
                // Vérifier si le créneau exact existe, sinon essayer de trouver un créneau compatible
                if (grid.containsKey(courseTimeSlot) && grid.get(courseTimeSlot).containsKey(dayName)) {
                    grid.get(courseTimeSlot).put(dayName, course);
                    System.out.println("Course placed in exact slot: " + courseTimeSlot);
                } else {
                    // Essayer de trouver un créneau compatible si l'heure exacte ne correspond pas
                    String compatibleTimeSlot = findCompatibleTimeSlot(course.getHeureDebut(), course.getHeureFin(), timeSlots);
                    if (compatibleTimeSlot != null && grid.containsKey(compatibleTimeSlot) && grid.get(compatibleTimeSlot).containsKey(dayName)) {
                        grid.get(compatibleTimeSlot).put(dayName, course);
                        System.out.println("Course placed in compatible slot: " + compatibleTimeSlot + " (original: " + courseTimeSlot + ")");
                    } else {
                        System.out.println("No compatible slot found for course: " + courseTimeSlot + " on " + dayName);
                    }
                }
            } else {
                System.out.println("Course skipped due to missing data: " + course);
            }
        }
        
        return grid;
    }

    /**
     * Trouve un créneau compatible pour un cours donné
     */
    private String findCompatibleTimeSlot(LocalTime startTime, LocalTime endTime, List<String> timeSlots) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (String timeSlot : timeSlots) {
            String[] parts = timeSlot.split("-");
            if (parts.length == 2) {
                try {
                    LocalTime slotStart = LocalTime.parse(parts[0], timeFormatter);
                    LocalTime slotEnd = LocalTime.parse(parts[1], timeFormatter);
                    
                    // Vérifier si le cours commence dans ce créneau
                    if ((startTime.equals(slotStart) || startTime.isAfter(slotStart)) && 
                        startTime.isBefore(slotEnd)) {
                        return timeSlot;
                    }
                } catch (Exception e) {
                    // Ignorer les créneaux mal formatés
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * Convertit un DayOfWeek en nom français
     */
    private String getDayName(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "Lundi";
            case TUESDAY: return "Mardi";
            case WEDNESDAY: return "Mercredi";
            case THURSDAY: return "Jeudi";
            case FRIDAY: return "Vendredi";
            default: return "";
        }
    }
}
