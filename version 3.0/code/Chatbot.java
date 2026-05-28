import java.util.List;

public class Chatbot {
    private int chatbot_id = 1;

    public String processRequest(String userMessage) {
        String msg = userMessage.toLowerCase();
        if (msg.contains("κρατη") || msg.contains("book"))
            return "Για κρατήσεις: Αναζητήστε host, επιλέξτε ημερομηνίες και πατήστε 'Κράτηση'. Χρειάζεστε περισσότερη βοήθεια;";
        if (msg.contains("ακυρ") || msg.contains("cancel"))
            return "Για ακύρωση: Μεταβείτε στις κρατήσεις σας και πατήστε 'Ακύρωση'. Ισχύει η πολιτική επιστροφής χρημάτων.";
        if (msg.contains("πληρωμ") || msg.contains("pay"))
            return "Δεχόμαστε πιστωτικές/χρεωστικές κάρτες και PayPal. Η χρέωση γίνεται μετά την επιβεβαίωση κράτησης.";
        if (msg.contains("κωδικ") || msg.contains("password"))
            return "Για αλλαγή κωδικού: Ρυθμίσεις > Ασφάλεια > Αλλαγή κωδικού.";
        if (msg.contains("κατοικιδ") || msg.contains("pet"))
            return "Για προσθήκη κατοικίδιου: Προφίλ > 'Τα κατοικίδιά μου' > '+'.";
        if (msg.contains("host"))
            return "Για να γίνετε host: Ρυθμίσεις > 'Γίνε Host' και συμπληρώστε τα στοιχεία σας.";
        return "Δεν βρήκα απάντηση για το ερώτημά σας. Θέλετε να υποβάλετε αίτημα υποστήριξης;";
    }
}
