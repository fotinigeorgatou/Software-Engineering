import java.util.ArrayList;
import java.util.List;

public class FAQ {
    private int faq_id;
    private String question;
    private String answer;
    private String faq_category;
    private static int nextId = 1;

    public FAQ(String question, String answer, String category) {
        this.faq_id = nextId++;
        this.question = question;
        this.answer = answer;
        this.faq_category = category;
    }

    public static List<FAQ> searchFAQ(String keyword) {
        List<FAQ> all = getAllFAQs();
        List<FAQ> results = new ArrayList<>();
        for (FAQ faq : all) {
            if (faq.question.toLowerCase().contains(keyword.toLowerCase())
                    || faq.faq_category.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(faq);
            }
        }
        return results.isEmpty() ? all.subList(0, Math.min(3, all.size())) : results;
    }

    public String getAnswer() { return answer; }
    public String getQuestion() { return question; }
    public String getFaq_category() { return faq_category; }
    public int getFaq_id() { return faq_id; }

    public static List<FAQ> getAllFAQs() {
        List<FAQ> faqs = new ArrayList<>();
        faqs.add(new FAQ("Πώς κάνω κράτηση;",
                "Μεταβείτε στην αναζήτηση, επιλέξτε host και πατήστε 'Κράτηση'. Συμπληρώστε τις ημερομηνίες και επιβεβαιώστε.", "Κρατήσεις"));
        faqs.add(new FAQ("Πώς ακυρώνω μια κράτηση;",
                "Μεταβείτε στις κρατήσεις σας, επιλέξτε την κράτηση και πατήστε 'Ακύρωση'. Ισχύει η πολιτική επιστροφής χρημάτων.", "Κρατήσεις"));
        faqs.add(new FAQ("Πώς αλλάζω τον κωδικό μου;",
                "Πηγαίνετε στις Ρυθμίσεις > Ασφάλεια > Αλλαγή κωδικού. Εισάγετε τον παλιό και τον νέο κωδικό.", "Λογαριασμός"));
        faqs.add(new FAQ("Πώς προσθέτω κατοικίδιο;",
                "Στο προφίλ σας επιλέξτε 'Τα κατοικίδιά μου' και πατήστε '+'. Συμπληρώστε τα στοιχεία του κατοικίδιου.", "Κατοικίδια"));
        faqs.add(new FAQ("Πώς γίνομαι host;",
                "Πηγαίνετε στις Ρυθμίσεις και επιλέξτε 'Γίνε Host'. Συμπληρώστε τα στοιχεία του χώρου σας.", "Host"));
        faqs.add(new FAQ("Πώς λειτουργεί η πληρωμή;",
                "Οι πληρωμές γίνονται μέσω πιστωτικής/χρεωστικής κάρτας ή PayPal. Η χρέωση γίνεται μετά την επιβεβαίωση.", "Πληρωμές"));
        return faqs;
    }
}
