import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class usecase9_ui_launcher {
    public static void main(String[] args) {
        Application.launch(EmergencyApp.class, args);
    }

    // ── Palette ────────────────────────────────────────────────────────────────
    static final String PINK_HEADER = "#FF5E78";
    static final String BG_LIGHT    = "#F5F5F5";
    static final String BUTTON_PINK = "#FF6E8C";
    static final String LABEL_PINK  = "#FFA0B4";
    static final String FIELD_GRAY  = "#E1E1E1";
    static final String PURPLE      = "#C1A3E5";
    static final String CARD_BG     = "#FCFCFC";
    static final String DARK        = "#222222";
    static final String MUTED       = "#888888";
    static final String WHITE       = "#FFFFFF";
    static final String RED_ALERT   = "#E62828";
    static final String GREEN_OK    = "#27AE60";
    static final String ORANGE_WARN = "#E67E22";

    // ── Helpers ────────────────────────────────────────────────────────────────
    static Label lbl(String text, int size, boolean bold, String color) {
        Label l = new Label(text);
        l.setFont(Font.font("SansSerif", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setStyle("-fx-text-fill: " + color + ";");
        return l;
    }

    static Button prominentBtn(String text, String bg, String fg, int size, int vPad, int hPad) {
        Button b = new Button(text);
        b.setFont(Font.font("SansSerif", FontWeight.BOLD, size));
        b.setPadding(new Insets(vPad, hPad, vPad, hPad));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(javafx.scene.Cursor.HAND);
        String base  = "-fx-background-color:" + bg + "; -fx-text-fill:" + fg + "; -fx-background-radius:10;";
        String hover = "-fx-background-color:" + darken(bg) + "; -fx-text-fill:" + fg + "; -fx-background-radius:10;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    static String darken(String hex) {
        if (hex.equals(BUTTON_PINK)) return PINK_HEADER;
        if (hex.equals(PURPLE))      return "#A885D8";
        if (hex.equals(RED_ALERT))   return "#C21F1F";
        if (hex.equals(GREEN_OK))    return "#1E8449";
        if (hex.equals(ORANGE_WARN)) return "#CA6F1E";
        return "#555555";
    }

    static Button outlineBtn(String text, String borderColor, String textColor) {
        Button b = new Button(text);
        b.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        b.setPadding(new Insets(10, 16, 10, 16));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(javafx.scene.Cursor.HAND);
        String base  = "-fx-border-color:" + borderColor + "; -fx-text-fill:" + textColor + "; -fx-border-radius:8; -fx-background-radius:8; -fx-border-width:2; -fx-background-color:" + WHITE + ";";
        String hover = "-fx-border-color:" + borderColor + "; -fx-text-fill:" + textColor + "; -fx-border-radius:8; -fx-background-radius:8; -fx-border-width:2; -fx-background-color:#F9F6FC;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(base));
        return b;
    }

    static Separator separator() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + FIELD_GRAY + ";");
        return s;
    }

    static VBox card(double radius) {
        VBox c = new VBox(10);
        c.setStyle("-fx-background-color:" + CARD_BG + "; -fx-background-radius:" + radius +
                "; -fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:" + radius + "; -fx-border-width:1.5;");
        c.setPadding(new Insets(16));
        return c;
    }

    // ── App ────────────────────────────────────────────────────────────────────
    public static class EmergencyApp extends Application {

        private Stage window;
        private Scene mainScene;
        // Κοινό μοντέλο συμβάντος (μοιράζεται μεταξύ οθονών)
        private EmergencyEvent currentEvent = new EmergencyEvent();
        // Για Εναλλακτική Ροή 2: GPS simulation
        private boolean gpsAvailable = true; // θέσε false για να δοκιμάσεις τη ροή

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            window.setTitle("petBnb — Σύστημα Έκτακτης Ανάγκης");
            window.setResizable(false);
            createMainScreen();
            window.setScene(mainScene);
            window.show();
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 1: Main — Κεντρική Οθόνη
        // ═══════════════════════════════════════════════════════════════════════
        private void createMainScreen() {
            VBox root = new VBox();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            VBox header = new VBox(4);
            header.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            header.setPadding(new Insets(22, 22, 20, 22));
            Label brand = lbl("petBnb", 11, true, "rgba(255,255,255,0.8)");
            Label title = lbl("Ενεργή Φιλοξενία", 22, true, WHITE);
            Label sub   = lbl("Διαχείριση & Έκτακτη Ανάγκη", 13, false, "rgba(255,255,255,0.9)");
            header.getChildren().addAll(brand, title, sub);

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));
            VBox.setVgrow(body, Priority.ALWAYS);

            // Pet card
            VBox petCard = card(14);
            HBox petRow = new HBox(14);
            petRow.setAlignment(Pos.CENTER_LEFT);
            Circle avatar = new Circle(26);
            avatar.setFill(Color.web(FIELD_GRAY));
            avatar.setStroke(Color.web(PURPLE));
            avatar.setStrokeWidth(2);
            Label avatarIcon = lbl("🐕", 22, false, DARK);
            StackPane avatarPane = new StackPane(avatar, avatarIcon);
            VBox petInfo = new VBox(2);
            Label petLabel = lbl("ΦΙΛΟΞΕΝΟΥΜΕΝΟ ΖΩΟ", 10, true, LABEL_PINK);
            Label petName  = lbl("Bella", 18, true, DARK);
            Label petBreed = lbl("Labrador · 3 ετών", 12, false, MUTED);
            petInfo.getChildren().addAll(petLabel, petName, petBreed);
            petRow.getChildren().addAll(avatarPane, petInfo);
            petCard.getChildren().add(petRow);

            // Emergency button with pulse
            Button emergencyBtn = prominentBtn("  ⬤  ΕΚΤΑΚΤΗ ΑΝΑΓΚΗ", RED_ALERT, WHITE, 18, 18, 25);
            emergencyBtn.setOnAction(e -> {
                currentEvent = new EmergencyEvent(); // reset
                showCategoryScreen();
            });

            HBox quickRow = new HBox(10);
            Button callOwner   = outlineBtn("📞  Ιδιοκτήτης", PURPLE, PURPLE);
            Button callSupport = outlineBtn("📞  petBnb Support", BUTTON_PINK, BUTTON_PINK);
            HBox.setHgrow(callOwner, Priority.ALWAYS);
            HBox.setHgrow(callSupport, Priority.ALWAYS);
            callOwner.setOnAction(e   -> showInfo("Κλήση Ιδιοκτήτη..."));
            callSupport.setOnAction(e -> showInfo("Κλήση petBnb Support..."));
            quickRow.getChildren().addAll(callOwner, callSupport);

            body.getChildren().addAll(petCard, emergencyBtn, quickRow);
            root.getChildren().addAll(header, body);
            mainScene = new Scene(root, 420, 700);
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 2: Category — Επιλογή Κατηγορίας Συμβάντος
        // (Εναλλακτική Ροή 1: κουμπί "Ακύρωση — False Alarm")
        // ═══════════════════════════════════════════════════════════════════════
        private void showCategoryScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            HBox topBar = makeTopBar("Αναφορά Συμβάντος", () -> window.setScene(mainScene));
            root.setTop(topBar);

            ScrollPane scroll = new ScrollPane();
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");

            VBox body = new VBox(16);
            body.setPadding(new Insets(22, 22, 22, 22));

            Label catLabel = lbl("ΚΑΤΗΓΟΡΙΑ ΣΥΜΒΑΝΤΟΣ", 10, true, LABEL_PINK);
            ComboBox<String> categoryBox = new ComboBox<>();
            categoryBox.getItems().addAll("🏥  Ιατρικό", "🚪  Απόδραση ζώου", "⚠️  Επιθετική συμπεριφορά", "📋  Άλλο");
            categoryBox.setValue("🏥  Ιατρικό");
            categoryBox.setMaxWidth(Double.MAX_VALUE);
            categoryBox.setStyle("-fx-background-color:" + WHITE + "; -fx-border-color:" + FIELD_GRAY +
                    "; -fx-border-radius:10; -fx-background-radius:10; -fx-font-size:14;");

            Label commentLabel = lbl("ΠΕΡΙΓΡΑΦΗ", 10, true, LABEL_PINK);
            TextArea commentArea = new TextArea();
            commentArea.setPromptText("π.χ. το σκυλί έφαγε σοκολάτα, φαίνεται αδύναμο...");
            commentArea.setFont(Font.font("SansSerif", 14));
            commentArea.setPrefRowCount(4);
            commentArea.setStyle("-fx-background-color:" + WHITE + "; -fx-border-color:" + FIELD_GRAY +
                    "; -fx-border-radius:10; -fx-background-radius:10;");

            // Κουμπί Υποβολής
            Button submitBtn = prominentBtn("Υποβολή Συμβάντος  →", BUTTON_PINK, WHITE, 14, 15, 20);
            submitBtn.setOnAction(e -> {
                String rawCat  = categoryBox.getValue();
                String cat     = rawCat.replaceAll("^[^a-zA-Zα-ωΑ-Ω]+\\s+", "").trim();
                String comment = commentArea.getText().trim().isEmpty() ? "(χωρίς σχόλιο)" : commentArea.getText().trim();

                currentEvent.startEvent(cat, comment);

                // Εναλλακτική Ροή 5: Απόδραση / Επιθετική → ShowSupportScreen
                if (cat.equals("Απόδραση ζώου") || cat.equals("Επιθετική συμπεριφορά")) {
                    showOwnerNotificationStep(cat, false);
                } else {
                    // Κύρια Ροή + Εναλλακτική Ροή 2 (GPS)
                    showOwnerNotificationStep(cat, true);
                }
            });

            // ── ΕΝΑΛΛΑΚΤΙΚΗ ΡΟΗ 1: False Alarm ──────────────────────────────
            Button falseAlarmBtn = outlineBtn("✕  Ακύρωση — Πάτησα κατά λάθος", RED_ALERT, RED_ALERT);
            falseAlarmBtn.setOnAction(e -> {
                // Καμία ειδοποίηση, καμία καταγραφή στο log
                currentEvent.markFalseAlarm();
                showInfo("False Alarm καταγράφηκε. Επιστροφή στην αρχική οθόνη.");
                window.setScene(mainScene);
            });

            separator();

            Label callsLabel = lbl("ΑΜΕΣΗ ΕΠΙΚΟΙΝΩΝΙΑ", 10, true, LABEL_PINK);
            HBox callRow = new HBox(10);
            Button callOwner   = outlineBtn("👤  Ιδιοκτήτης", PURPLE, PURPLE);
            Button callSupport = outlineBtn("🛡️  petBnb Support", BUTTON_PINK, BUTTON_PINK);
            HBox.setHgrow(callOwner, Priority.ALWAYS);
            HBox.setHgrow(callSupport, Priority.ALWAYS);
            callOwner.setOnAction(e -> {
                currentEvent.addLogEntry("Κλήση Ιδιοκτήτη από Host");
                showInfo("Κλήση Ιδιοκτήτη...");
            });
            callSupport.setOnAction(e -> {
                currentEvent.addLogEntry("Κλήση petBnb Support από Host");
                showInfo("Κλήση petBnb Support...");
            });
            callRow.getChildren().addAll(callOwner, callSupport);

            body.getChildren().addAll(catLabel, categoryBox, commentLabel, commentArea, submitBtn, falseAlarmBtn, separator(), callsLabel, callRow);
            scroll.setContent(body);
            root.setCenter(scroll);
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 2.5: Owner Notification + Timeout
        // (Εναλλακτική Ροή 4: αδυναμία επικοινωνίας — countdown 5 λεπτά)
        // ═══════════════════════════════════════════════════════════════════════
        private void showOwnerNotificationStep(String category, boolean isMedical) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Ειδοποίηση Ιδιοκτήτη", () -> showCategoryScreen()));

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));
            body.setAlignment(Pos.TOP_CENTER);

            Label sentIcon = lbl("📨", 40, false, DARK);
            sentIcon.setAlignment(Pos.CENTER);

            Label sentTitle = lbl("Ειδοποίηση Εστάλη!", 18, true, DARK);
            sentTitle.setAlignment(Pos.CENTER);

            Label sentSub = lbl("Push Notification & SMS στάλθηκαν στον ιδιοκτήτη με ένδειξη EMERGENCY.", 13, false, MUTED);
            sentSub.setWrapText(true);
            sentSub.setAlignment(Pos.CENTER);
            sentSub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            currentEvent.addLogEntry("Push Notification & SMS εστάλησαν στον Ιδιοκτήτη — κατηγορία: " + category);

            // ── Countdown 5 λεπτών (Εναλλ. Ροή 4) ───────────────────────────
            final int[] secondsLeft = {300}; // 5 λεπτά = 300 δευτερόλεπτα
            Label timerLabel = lbl("Αναμονή απόκρισης ιδιοκτήτη: 5:00", 13, true, ORANGE_WARN);
            timerLabel.setAlignment(Pos.CENTER);

            Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                secondsLeft[0]--;
                int m = secondsLeft[0] / 60;
                int s = secondsLeft[0] % 60;
                timerLabel.setText(String.format("Αναμονή απόκρισης ιδιοκτήτη: %d:%02d", m, s));

                if (secondsLeft[0] <= 0) {
                    // Εναλλακτική Ροή 4: timeout
                    currentEvent.addLogEntry("Ο Ιδιοκτήτης δεν ανταποκρίθηκε εντός 5 λεπτών.");
                    if (isMedical) showClinicsScreen(true);
                    else showSupportScreen(category, true);
                }
            }));
            countdown.setCycleCount(Timeline.INDEFINITE);
            countdown.play();

            Label callOwnerHint = lbl("Θέλετε να τον καλέσετε απευθείας;", 13, false, MUTED);
            callOwnerHint.setWrapText(true);

            Button callOwnerBtn = outlineBtn("📞  Κλήση Ιδιοκτήτη τώρα", PURPLE, PURPLE);
            callOwnerBtn.setOnAction(e -> {
                countdown.stop();
                currentEvent.addLogEntry("Host ξεκίνησε κλήση προς Ιδιοκτήτη");
                showInfo("Κλήση Ιδιοκτήτη...");
            });

            // Κουμπί "Ο Ιδιοκτήτης Απάντησε" (simulation)
            Button ownerRepliedBtn = prominentBtn("✓  Ο Ιδιοκτήτης Απάντησε", GREEN_OK, WHITE, 14, 15, 20);
            ownerRepliedBtn.setOnAction(e -> {
                countdown.stop();
                currentEvent.addLogEntry("Ο Ιδιοκτήτης ανταποκρίθηκε.");
                if (isMedical) showClinicsScreen(false);
                else showSupportScreen(category, false);
            });

            // Κουμπί "Συνέχεια χωρίς απόκριση"
            Button continueBtn = outlineBtn("→  Συνέχεια χωρίς αναμονή", MUTED, MUTED);
            continueBtn.setOnAction(e -> {
                countdown.stop();
                currentEvent.addLogEntry("Host επέλεξε συνέχεια χωρίς αναμονή απόκρισης Ιδιοκτήτη.");
                if (isMedical) showClinicsScreen(false);
                else showSupportScreen(category, false);
            });

            body.getChildren().addAll(sentIcon, sentTitle, sentSub, timerLabel, separator(), callOwnerHint, callOwnerBtn, ownerRepliedBtn, continueBtn);
            root.setCenter(body);
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 3: Clinics — Επιλογή Κτηνιατρείου (Ιατρικό)
        // (Εναλλακτική Ροή 2: GPS failure)
        // ═══════════════════════════════════════════════════════════════════════
        private void showClinicsScreen(boolean ownerTimedOut) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Κοντινά Κτηνιατρεία", () -> showCategoryScreen()));

            ScrollPane scroll = new ScrollPane();
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");

            VBox body = new VBox(10);
            body.setPadding(new Insets(20, 20, 20, 20));

            // ── ΕΝΑΛΛΑΚΤΙΚΗ ΡΟΗ 2: GPS ───────────────────────────────────────
            if (!gpsAvailable) {
                VBox gpsWarn = card(12);
                gpsWarn.setStyle(gpsWarn.getStyle() + "-fx-border-color:" + ORANGE_WARN + ";");
                Label warnTitle = lbl("⚠️  Αδυναμία εντοπισμού τοποθεσίας GPS", 13, true, ORANGE_WARN);
                Label warnSub   = lbl("Χρησιμοποιείται η διεύθυνση κατοικίας ως προεπιλογή: Κορίνθου 100, Πάτρα.", 12, false, DARK);
                warnSub.setWrapText(true);
                Button updateLocBtn = outlineBtn("📍  Ενημέρωση τρέχουσας τοποθεσίας", ORANGE_WARN, ORANGE_WARN);
                updateLocBtn.setOnAction(e -> {
                    currentEvent.addLogEntry("Host ενημέρωσε τοποθεσία χειροκίνητα.");
                    showInfo("Τοποθεσία ενημερώθηκε. Ανανέωση κτηνιατρείων...");
                });
                gpsWarn.getChildren().addAll(warnTitle, warnSub, updateLocBtn);
                body.getChildren().add(gpsWarn);
            }

            // Banner αν ο ιδιοκτήτης δεν απάντησε
            if (ownerTimedOut) {
                VBox timeoutBanner = card(12);
                timeoutBanner.setStyle(timeoutBanner.getStyle() + "-fx-border-color:" + ORANGE_WARN + ";");
                Label tl = lbl("⏱  Ο Ιδιοκτήτης δεν ανταποκρίθηκε (5 λεπτά)", 13, true, ORANGE_WARN);
                Label ts = lbl("Συνεχίστε με εύρεση κτηνιατρού. Η απόφασή σας θα σταλεί ως SMS.", 12, false, DARK);
                ts.setWrapText(true);
                timeoutBanner.getChildren().addAll(tl, ts);
                body.getChildren().add(timeoutBanner);
            }

            VetClinic[] clinics = {
                    new VetClinic("Κτηνιατρικό Κέντρο Πάτρας", "Κορίνθου 150",        "2610-123456", "Δρ. Παπαδόπουλος",  "https://maps.google.com", "0.8"),
                    new VetClinic("Vet Care Patras",             "Μαιζώνος 55",          "2610-987654", "Δρ. Γεωργίου",      "https://maps.google.com", "1.5"),
                    new VetClinic("Animal Health",               "Αγίου Ανδρέου 12",     "2610-555666", "Δρ. Κωνσταντίνου", "https://maps.google.com", "2.2"),
                    new VetClinic("Patras Pet Clinic",           "Ακρωτηρίου 20",        "2610-222333", "Δρ. Νικολάου",      "https://maps.google.com", "3.1"),
                    new VetClinic("City Vets",                   "Έλληνος Στρατιώτου 40","2610-444555", "Δρ. Δημητρίου",    "https://maps.google.com", "4.0")
            };

            VBox detailPanel = card(12);
            detailPanel.setVisible(false);
            detailPanel.setManaged(false);
            TextArea detailArea = new TextArea();
            detailArea.setEditable(false);
            detailArea.setFont(Font.font("SansSerif", 13));
            detailArea.setPrefRowCount(5);
            detailArea.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
            Hyperlink mapLink = new Hyperlink("🗺  Άνοιγμα στο Google Maps");
            mapLink.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
            mapLink.setStyle("-fx-text-fill:" + PURPLE + ";");
            detailPanel.getChildren().addAll(detailArea, mapLink);

            // ── Κουμπί Αιτήματος Έγκρισης (Κύρια Ροή) ───────────────────────
            Button requestBtn = prominentBtn("Έναρξη Μετάβασης — Αίτημα Έγκρισης", PURPLE, WHITE, 13, 15, 16);
            requestBtn.setVisible(false);
            requestBtn.setManaged(false);

            final VetClinic[] selected = {null};

            for (VetClinic clinic : clinics) {
                VBox clinicCard = card(12);
                clinicCard.setCursor(javafx.scene.Cursor.HAND);

                HBox topRow = new HBox();
                topRow.setAlignment(Pos.CENTER_LEFT);
                Label nameLbl = lbl(clinic.getName(), 14, true, DARK);
                HBox.setHgrow(nameLbl, Priority.ALWAYS);
                Label distBadge = new Label(clinic.getDistance() + " km");
                distBadge.setFont(Font.font("SansSerif", FontWeight.BOLD, 11));
                distBadge.setStyle("-fx-text-fill:" + WHITE + "; -fx-background-color:" + LABEL_PINK + "; -fx-background-radius:20; -fx-padding:3 10 3 10;");
                topRow.getChildren().addAll(nameLbl, distBadge);

                Label addrLbl = lbl(clinic.getAddress(), 12, false, MUTED);
                Label docLbl  = lbl(clinic.getDoctor(),  12, false, DARK);
                clinicCard.getChildren().addAll(topRow, addrLbl, docLbl);

                clinicCard.setOnMouseClicked(evt -> {
                    selected[0] = clinic;
                    // Reset visual selection
                    body.getChildren().stream()
                            .filter(n -> n instanceof VBox && n != detailPanel)
                            .forEach(n -> ((VBox) n).setStyle(
                                    "-fx-background-color:" + CARD_BG + "; -fx-background-radius:12;" +
                                            "-fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:12; -fx-border-width:1.5;"));
                    clinicCard.setStyle(
                            "-fx-background-color:" + WHITE + "; -fx-background-radius:12;" +
                                    "-fx-border-color:" + PURPLE + "; -fx-border-radius:12; -fx-border-width:2;");
                    detailArea.setText(clinic.getDetails());
                    mapLink.setOnAction(me -> getHostServices().showDocument(clinic.getMapLink()));
                    detailPanel.setVisible(true);
                    detailPanel.setManaged(true);
                    requestBtn.setVisible(true);
                    requestBtn.setManaged(true);
                    requestBtn.setText("Έναρξη Μετάβασης → " + clinic.getName() + "  (Αίτημα Έγκρισης)");
                });

                body.getChildren().add(clinicCard);
            }

            requestBtn.setOnAction(e -> {
                if (selected[0] == null) { showError("Επιλέξτε ένα κτηνιατρείο!"); return; }
                currentEvent.addLogEntry("Έναρξη μετάβασης προς: " + selected[0].getName() + " — Αίτημα έγκρισης εστάλη στον Ιδιοκτήτη.");
                ApprovalRequest req = new ApprovalRequest(selected[0]);
                showApprovalWaitScreen(req);
            });

            body.getChildren().addAll(detailPanel, requestBtn);
            scroll.setContent(body);
            root.setCenter(scroll);
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 4: Approval Wait — Αναμονή Έγκρισης Ιδιοκτήτη
        // (Εναλλακτική Ροή 3: Απόρριψη Ενέργειας)
        // ═══════════════════════════════════════════════════════════════════════
        private void showApprovalWaitScreen(ApprovalRequest req) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Αναμονή Έγκρισης", null));

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));
            body.setAlignment(Pos.TOP_CENTER);

            Label icon  = lbl("⏳", 40, false, DARK);
            icon.setAlignment(Pos.CENTER);
            Label title = lbl("Αναμονή Απόκρισης Ιδιοκτήτη", 17, true, DARK);
            title.setAlignment(Pos.CENTER);
            Label sub   = lbl("Εστάλη αίτημα έγκρισης για μετάβαση στο:\n" + req.getSelectedClinic().getName(), 13, false, MUTED);
            sub.setWrapText(true);
            sub.setAlignment(Pos.CENTER);
            sub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            body.getChildren().addAll(icon, title, sub, separator());

            Label simLabel = lbl("— Προσομοίωση Απόκρισης Ιδιοκτήτη —", 11, false, MUTED);
            simLabel.setAlignment(Pos.CENTER);

            // Κύρια Ροή: ΕΓΚΡΙΣΗ
            Button approveBtn = prominentBtn("✓  Ιδιοκτήτης: ΕΓΚΡΙΣΗ", GREEN_OK, WHITE, 14, 15, 20);
            approveBtn.setOnAction(e -> {
                req.approve();
                currentEvent.addLogEntry("Ιδιοκτήτης ΕΝΕΚΡΙΝΕ μετάβαση → " + req.getSelectedClinic().getName());
                showApprovedScreen(req);
            });

            // Εναλλακτική Ροή 3: ΑΠΟΡΡΙΨΗ
            Button rejectBtn = prominentBtn("✕  Ιδιοκτήτης: ΑΠΟΡΡΙΨΗ", RED_ALERT, WHITE, 14, 15, 20);
            rejectBtn.setOnAction(e -> {
                showRejectionScreen(req);
            });

            body.getChildren().addAll(simLabel, approveBtn, rejectBtn);
            root.setCenter(body);
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 4a: Approved — Έγκριση Ενέργειας (Κύρια Ροή — Βήμα 9)
        // + κουμπί "Λήξη Συναγερμού" (Κύρια Ροή — Βήμα 10)
        // ═══════════════════════════════════════════════════════════════════════
        private void showApprovedScreen(ApprovalRequest req) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Ενέργεια Εγκρίθηκε", null));

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));
            body.setAlignment(Pos.TOP_CENTER);

            // Πράσινη ένδειξη
            Label icon  = lbl("✅", 48, false, DARK);
            icon.setAlignment(Pos.CENTER);
            Label title = lbl("Έγκριση Ληφθείσα!", 20, true, GREEN_OK);
            title.setAlignment(Pos.CENTER);
            Label sub   = lbl("Ο ιδιοκτήτης ενέκρινε τη μετάβαση στο:\n" + req.getSelectedClinic().getName(), 14, false, DARK);
            sub.setWrapText(true);
            sub.setAlignment(Pos.CENTER);
            sub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            Label phone = lbl("📞 " + req.getSelectedClinic().getPhone(), 14, true, PURPLE);
            phone.setAlignment(Pos.CENTER);

            body.getChildren().addAll(icon, title, sub, phone, separator());

            // ── ΚΥΡΙΑ ΡΟΗ ΒΗΜΑ 10: Λήξη Συναγερμού ──────────────────────────
            Label hint = lbl("Όταν το πρόβλημα επιλυθεί, πατήστε:", 12, false, MUTED);
            hint.setWrapText(true);

            Button resolveBtn = prominentBtn("🏁  Λήξη Συναγερμού", BUTTON_PINK, WHITE, 15, 16, 20);
            resolveBtn.setOnAction(e -> {
                currentEvent.resolve();
                showEmergencyLogScreen();
            });

            body.getChildren().addAll(hint, resolveBtn);
            root.setCenter(body);
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 4b: Rejection — Απόρριψη (Εναλλακτική Ροή 3)
        // ═══════════════════════════════════════════════════════════════════════
        private void showRejectionScreen(ApprovalRequest req) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Ενέργεια Απορρίφθηκε", null));

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));

            Label icon  = lbl("❌", 36, false, DARK);
            icon.setAlignment(Pos.CENTER);
            Label title = lbl("Ενέργεια Απορρίφθηκε", 17, true, RED_ALERT);
            title.setAlignment(Pos.CENTER);
            Label hint  = lbl("Ο ιδιοκτήτης απέρριψε την ενέργεια. Δείτε την αντιπρότασή του:", 13, false, DARK);
            hint.setWrapText(true);

            // Αντιπρόταση ιδιοκτήτη (simulation)
            TextArea counter = new TextArea();
            counter.setEditable(false);
            counter.setText("Αντιπρόταση Ιδιοκτήτη:\n\"Παρακαλώ πηγαίνετε στο Vet Care Patras (Μαιζώνος 55) — είναι πιο κοντά και έχουμε προηγούμενη σχέση.\"");
            counter.setPrefRowCount(4);
            counter.setFont(Font.font("SansSerif", 13));
            counter.setStyle("-fx-background-color:" + WHITE + "; -fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:8;");
            req.reject(counter.getText());
            currentEvent.addLogEntry("Ιδιοκτήτης ΑΠΟΡΡΙΨΕ ενέργεια — αντιπρόταση: " + counter.getText());

            Label callLabel = lbl("Ή επικοινωνήστε απευθείας:", 12, false, MUTED);

            HBox callRow = new HBox(10);
            Button callBtn  = outlineBtn("📞  Κλήση Ιδιοκτήτη", PURPLE, PURPLE);
            Button videoBtn = outlineBtn("🎥  Βιντεοκλήση", BUTTON_PINK, BUTTON_PINK);
            HBox.setHgrow(callBtn, Priority.ALWAYS);
            HBox.setHgrow(videoBtn, Priority.ALWAYS);
            callBtn.setOnAction(e -> {
                currentEvent.addLogEntry("Host ξεκίνησε κλήση μετά από απόρριψη.");
                showInfo("Κλήση Ιδιοκτήτη...");
            });
            videoBtn.setOnAction(e -> {
                currentEvent.addLogEntry("Host ξεκίνησε βιντεοκλήση μετά από απόρριψη.");
                showInfo("Βιντεοκλήση...");
            });
            callRow.getChildren().addAll(callBtn, videoBtn);

            Button backToClinicBtn = prominentBtn("← Επιλογή Άλλου Κτηνιατρείου", BUTTON_PINK, WHITE, 13, 14, 16);
            backToClinicBtn.setOnAction(e -> showClinicsScreen(false));

            body.getChildren().addAll(icon, title, hint, counter, callLabel, callRow, separator(), backToClinicBtn);
            root.setCenter(new ScrollPane(body) {{ setFitToWidth(true); setStyle("-fx-background:transparent; -fx-background-color:transparent;"); }});
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 5: Support — Απόδραση / Επιθετική (Εναλλακτική Ροή 5)
        // ═══════════════════════════════════════════════════════════════════════
        private void showSupportScreen(String category, boolean ownerTimedOut) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Επικοινωνία", () -> showCategoryScreen()));

            VBox body = new VBox(14);
            body.setPadding(new Insets(24, 22, 24, 22));

            Label badge = new Label("Συμβάν: " + category);
            badge.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
            badge.setStyle("-fx-text-fill:" + WHITE + "; -fx-background-color:" + RED_ALERT + "; -fx-background-radius:20; -fx-padding:6 14 6 14;");

            if (ownerTimedOut) {
                VBox timeoutBanner = card(12);
                timeoutBanner.setStyle(timeoutBanner.getStyle() + "-fx-border-color:" + ORANGE_WARN + ";");
                Label tl = lbl("⏱  Ο Ιδιοκτήτης δεν ανταποκρίθηκε (5 λεπτά)", 12, true, ORANGE_WARN);
                Label ts = lbl("Στάλθηκε νέο SMS με την κατάσταση του συμβάντος.", 12, false, DARK);
                ts.setWrapText(true);
                timeoutBanner.getChildren().addAll(tl, ts);
                currentEvent.addLogEntry("Νέο SMS εστάλη στον Ιδιοκτήτη (timeout).");
                body.getChildren().addAll(badge, timeoutBanner);
            } else {
                body.getChildren().add(badge);
            }

            // Επιλογές επικοινωνίας (Εναλλ. Ροή 5)
            String[][] options = {
                    {"👤", "Κλήση Ιδιοκτήτη",   "Άμεση επικοινωνία με τον ιδιοκτήτη",  PURPLE,      "Κλήση Ιδιοκτήτη..."},
                    {"🎥", "Βιντεοκλήση Ιδιοκτήτη", "Βλέπετε το ζώο μαζί σε πραγματικό χρόνο", BUTTON_PINK, "Βιντεοκλήση..."},
                    {"🛡️", "petBnb Support",     "24/7 υποστήριξη από την ομάδα μας",   MUTED,       "Κλήση petBnb Support..."}
            };

            for (String[] opt : options) {
                VBox supportCard = card(14);
                supportCard.setCursor(javafx.scene.Cursor.HAND);
                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);
                Label iconBox = new Label(opt[0]);
                iconBox.setFont(Font.font(22));
                iconBox.setStyle("-fx-background-color:" + opt[3] + "; -fx-text-fill:white; -fx-background-radius:10; -fx-padding:10 12 10 12;");
                VBox info = new VBox(3);
                Label title2 = lbl(opt[1], 15, true, DARK);
                Label sub2   = lbl(opt[2], 12, false, MUTED);
                info.getChildren().addAll(title2, sub2);
                HBox.setHgrow(info, Priority.ALWAYS);
                Label arrow = lbl("→", 18, false, MUTED);
                row.getChildren().addAll(iconBox, info, arrow);
                supportCard.getChildren().add(row);

                String logMsg   = opt[1];
                String toastMsg = opt[4];
                supportCard.setOnMouseClicked(ev -> {
                    currentEvent.addLogEntry("Host επέλεξε: " + logMsg);
                    showInfo(toastMsg);
                });
                supportCard.setOnMouseEntered(ev -> supportCard.setStyle(
                        "-fx-background-color:#F9F6FC; -fx-background-radius:14;" +
                                "-fx-border-color:" + PURPLE + "; -fx-border-radius:14; -fx-border-width:1.5;"));
                supportCard.setOnMouseExited(ev -> supportCard.setStyle(
                        "-fx-background-color:" + CARD_BG + "; -fx-background-radius:14;" +
                                "-fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:14; -fx-border-width:1.5;"));
                body.getChildren().add(supportCard);
            }

            // Λήξη Συναγερμού
            body.getChildren().add(separator());
            Button resolveBtn = prominentBtn("🏁  Λήξη Συναγερμού", BUTTON_PINK, WHITE, 14, 14, 20);
            resolveBtn.setOnAction(e -> {
                currentEvent.resolve();
                showEmergencyLogScreen();
            });
            body.getChildren().add(resolveBtn);

            root.setCenter(new ScrollPane(body) {{ setFitToWidth(true); setStyle("-fx-background:transparent; -fx-background-color:transparent;"); }});
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // SCREEN 6: Emergency Log — Τελικό Log (Μετά-Συνθήκη)
        // ═══════════════════════════════════════════════════════════════════════
        private void showEmergencyLogScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");
            root.setTop(makeTopBar("Emergency Log", null));

            VBox body = new VBox(14);
            body.setPadding(new Insets(24, 22, 24, 22));

            Label icon  = lbl("📋", 36, false, DARK);
            icon.setAlignment(Pos.CENTER);
            Label title = lbl("Συμβάν Κλειστό", 18, true, GREEN_OK);
            title.setAlignment(Pos.CENTER);
            Label sub   = lbl("Το ιστορικό αποθηκεύτηκε. Προσβάσιμο από Host & Ιδιοκτήτη.", 12, false, MUTED);
            sub.setWrapText(true);
            sub.setAlignment(Pos.CENTER);
            sub.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            body.getChildren().addAll(icon, title, sub, separator());

            // Εμφάνιση Emergency Log
            Label logLabel = lbl("ΑΡΧΕΙΟ ΕΝΕΡΓΕΙΩΝ (EMERGENCY LOG)", 10, true, LABEL_PINK);
            body.getChildren().add(logLabel);

            TextArea logArea = new TextArea();
            logArea.setEditable(false);
            logArea.setFont(Font.font("Monospaced", 11));
            logArea.setPrefRowCount(12);
            logArea.setStyle("-fx-background-color:" + WHITE + "; -fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:8;");

            StringBuilder sb = new StringBuilder();
            for (String entry : currentEvent.getEmergencyLog()) {
                sb.append(entry).append("\n");
            }
            logArea.setText(sb.toString());
            body.getChildren().add(logArea);

            Button homeBtn = prominentBtn("🏠  Επιστροφή στην Αρχική", BUTTON_PINK, WHITE, 14, 15, 20);
            homeBtn.setOnAction(e -> {
                currentEvent = new EmergencyEvent(); // reset
                window.setScene(mainScene);
            });
            body.getChildren().add(homeBtn);

            root.setCenter(new ScrollPane(body) {{ setFitToWidth(true); setStyle("-fx-background:transparent; -fx-background-color:transparent;"); }});
            window.setScene(new Scene(root, 420, 700));
        }

        // ═══════════════════════════════════════════════════════════════════════
        // Shared Helpers
        // ═══════════════════════════════════════════════════════════════════════
        private HBox makeTopBar(String title, Runnable backAction) {
            HBox bar = new HBox(10);
            bar.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            bar.setPadding(new Insets(14, 18, 14, 18));
            bar.setAlignment(Pos.CENTER_LEFT);
            if (backAction != null) {
                Button back = new Button("← Πίσω");
                back.setFont(Font.font("SansSerif", FontWeight.NORMAL, 13));
                back.setStyle("-fx-text-fill:" + WHITE + "; -fx-background-color:transparent; -fx-cursor:hand;");
                back.setOnAction(e -> backAction.run());
                bar.getChildren().add(back);
            }
            Label hdr = lbl(title, 17, true, WHITE);
            bar.getChildren().add(hdr);
            return bar;
        }

        private void showInfo(String msg) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            a.setHeaderText(null); a.setTitle("petBnb"); a.showAndWait();
        }

        private void showError(String msg) {
            Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            a.setHeaderText(null); a.setTitle("Σφάλμα"); a.showAndWait();
        }
    }
}
