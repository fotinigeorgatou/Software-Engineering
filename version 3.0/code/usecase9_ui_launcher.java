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

    // ── Palette (Ροζ/Μωβ Theme) ────────────────────────────────────────────────
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
    static final String RED_ALERT   = "#E62828";  // Ζωντανό, συμπαγές κόκκινο

    // ── Helpers ────────────────────────────────────────────────────────────────
    static Label lbl(String text, int size, boolean bold, String color) {
        Label l = new Label(text);
        l.setFont(Font.font("SansSerif", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setStyle("-fx-text-fill: " + color + ";"); // CSS Fix για τα γράμματα
        return l;
    }

    // Κυρίαρχα, συμπαγή κουμπιά (Χωρίς περιγράμματα - μόνο γεμάτο χρώμα)
    static Button prominentBtnHelper(String text, String bg, String fg, int size, int vPadding, int hPadding) {
        Button b = new Button(text);
        b.setFont(Font.font("SansSerif", FontWeight.BOLD, size));
        b.setPadding(new Insets(vPadding, hPadding, vPadding, hPadding));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(javafx.scene.Cursor.HAND);

        // Επιβολή συμπαγούς χρώματος με CSS για να μην υπάρχουν διαφάνειες
        b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-background-radius: 10;");

        String hoverBg = bg.equals(BUTTON_PINK) ? PINK_HEADER : (bg.equals(PURPLE) ? "#A885D8" : (bg.equals(RED_ALERT) ? "#C21F1F" : "#555555"));

        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + hoverBg + "; -fx-text-fill: " + fg + "; -fx-background-radius: 10;"));
        b.setOnMouseExited(e  -> b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-background-radius: 10;"));
        return b;
    }

    // Δευτερεύοντα κουμπιά με περίγραμμα (όπως τα τηλέφωνα)
    static Button outlineBtn(String text, String borderColor, String textColor) {
        Button b = new Button(text);
        b.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        b.setPadding(new Insets(10, 16, 10, 16));
        b.setMaxWidth(Double.MAX_VALUE);
        b.setCursor(javafx.scene.Cursor.HAND);
        b.setStyle("-fx-border-color:" + borderColor + "; -fx-text-fill:" + textColor + "; -fx-border-radius:8; -fx-background-radius:8; -fx-border-width:2; -fx-background-color:" + WHITE + ";");
        b.setOnMouseEntered(e -> b.setStyle("-fx-border-color:" + borderColor + "; -fx-text-fill:" + textColor + "; -fx-border-radius:8; -fx-background-radius:8; -fx-border-width:2; -fx-background-color:#F9F6FC;"));
        b.setOnMouseExited(e  -> b.setStyle("-fx-border-color:" + borderColor + "; -fx-text-fill:" + textColor + "; -fx-border-radius:8; -fx-background-radius:8; -fx-border-width:2; -fx-background-color:" + WHITE + ";"));
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
        private final EmergencyEvent eventModel = new EmergencyEvent();

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            window.setTitle("petBnb — Σύστημα Έκτακτης Ανάγκης");
            window.setResizable(false);
            createMainScreen();
            window.setScene(mainScene);
            window.show();
        }

        // ── Screen 1: Main ─────────────────────────────────────────────────────
        private void createMainScreen() {
            VBox root = new VBox();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            VBox header = new VBox(4);
            header.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            header.setPadding(new Insets(22, 22, 20, 22));
            Label brand = lbl("petBnb", 11, true, "rgba(255,255,255,0.8)");
            brand.setStyle("-fx-letter-spacing: 2;");
            Label title = lbl("Ενεργή Φιλοξενία", 22, true, WHITE);
            Label sub   = lbl("Διαχείριση & Έκτακτη Ανάγκη", 13, false, "rgba(255,255,255,0.9)");
            header.getChildren().addAll(brand, title, sub);

            VBox body = new VBox(16);
            body.setPadding(new Insets(24, 22, 24, 22));
            VBox.setVgrow(body, Priority.ALWAYS);

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

            StackPane emergencyStack = new StackPane();
            // Απόλυτα κόκκινο, μεγάλο κουμπί
            Button emergencyBtn = prominentBtnHelper("  ⬤  ΕΚΤΑΚΤΗ ΑΝΑΓΚΗ", RED_ALERT, WHITE, 18, 18, 25);
            emergencyBtn.setOnAction(e -> showCategoryScreen());

            Circle pulse = new Circle(0, Color.TRANSPARENT);
            pulse.setStroke(Color.web(RED_ALERT, 0.4));
            pulse.setStrokeWidth(2);
            ScaleTransition pt = new ScaleTransition(Duration.millis(1400), pulse);
            pt.setFromX(1); pt.setToX(2.2);
            pt.setFromY(1); pt.setToY(2.2);
            pt.setCycleCount(Timeline.INDEFINITE);
            FadeTransition ft = new FadeTransition(Duration.millis(1400), pulse);
            ft.setFromValue(0.6); ft.setToValue(0);
            ft.setCycleCount(Timeline.INDEFINITE);
            pt.play(); ft.play();

            emergencyStack.getChildren().addAll(emergencyBtn);

            HBox quickRow = new HBox(10);
            Button callOwner   = outlineBtn("📞  Ιδιοκτήτης", PURPLE, PURPLE);
            Button callSupport = outlineBtn("📞  petBnb Support", BUTTON_PINK, BUTTON_PINK);
            HBox.setHgrow(callOwner, Priority.ALWAYS);
            HBox.setHgrow(callSupport, Priority.ALWAYS);
            callOwner.setOnAction(e -> {
                eventModel.addLogEntry("Επιλογή Χρήστη: Κλήση Ιδιοκτήτη...");
                showInfo("Κλήση Ιδιοκτήτη...");
            });
            callSupport.setOnAction(e -> {
                eventModel.addLogEntry("Επιλογή Χρήστη: Κλήση petBnb Support...");
                showInfo("Κλήση petBnb Support...");
            });
            quickRow.getChildren().addAll(callOwner, callSupport);

            body.getChildren().addAll(petCard, emergencyStack, quickRow);
            root.getChildren().addAll(header, body);

            mainScene = new Scene(root, 420, 700);
        }

        // ── Screen 2: Category ─────────────────────────────────────────────────
        private void showCategoryScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            HBox topBar = new HBox(10);
            topBar.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            topBar.setPadding(new Insets(14, 18, 14, 18));
            topBar.setAlignment(Pos.CENTER_LEFT);
            Button back = new Button("← Πίσω");
            back.setFont(Font.font("SansSerif", FontWeight.NORMAL, 13));
            back.setTextFill(Color.WHITE);
            back.setBackground(Background.fill(Color.TRANSPARENT));
            back.setCursor(javafx.scene.Cursor.HAND);
            back.setOnAction(e -> window.setScene(mainScene));
            Label hdr = lbl("Αναφορά Συμβάντος", 17, true, WHITE);
            topBar.getChildren().addAll(back, hdr);

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

            // Συμπαγές, γεμάτο ροζ κουμπί
            Button submitBtn = prominentBtnHelper("Υποβολή Συμβάντος  →", BUTTON_PINK, WHITE, 14, 15, 20);
            submitBtn.setOnAction(e -> {
                String rawCat   = categoryBox.getValue();
                String cat      = rawCat.replaceAll("^[^a-zA-Zα-ωΑ-Ω]+\\s+", "").trim();

                // ΔΙΟΡΘΩΣΗ JAVA 8: trim().isEmpty()
                String comment  = commentArea.getText().trim().isEmpty() ? "(χωρίς σχόλιο)" : commentArea.getText();

                EmergencyEvent event = new EmergencyEvent();
                event.startEvent(cat, comment);
                if (cat.equals("Ιατρικό")) showClinicsScreen();
                else showSupportScreen(cat);
            });

            body.getChildren().addAll(catLabel, categoryBox, commentLabel, commentArea, submitBtn, separator());

            Label callsLabel = lbl("ΑΜΕΣΗ ΕΠΙΚΟΙΝΩΝΙΑ", 10, true, LABEL_PINK);
            HBox callRow = new HBox(10);
            Button callOwner   = outlineBtn("👤  Ιδιοκτήτης", PURPLE, PURPLE);
            Button callSupport = outlineBtn("🛡️  petBnb Support", BUTTON_PINK, BUTTON_PINK);
            HBox.setHgrow(callOwner, Priority.ALWAYS);
            HBox.setHgrow(callSupport, Priority.ALWAYS);
            callOwner.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Κλήση Ιδιοκτήτη...");
                showInfo("Κλήση Ιδιοκτήτη...");
            });
            callSupport.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Κλήση petBnb Support...");
                showInfo("Κλήση petBnb Support...");
            });
            callRow.getChildren().addAll(callOwner, callSupport);
            body.getChildren().addAll(callsLabel, callRow);

            scroll.setContent(body);
            root.setTop(topBar);
            root.setCenter(scroll);
            window.setScene(new Scene(root, 420, 700));
        }

        // ── Screen 3: Clinics (Medical) ────────────────────────────────────────
        private void showClinicsScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            HBox topBar = makeTopBar("Κοντινά Κτηνιατρεία", () -> showCategoryScreen());

            ScrollPane scroll = new ScrollPane();
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");

            VBox body = new VBox(10);
            body.setPadding(new Insets(20, 20, 20, 20));

            VetClinic[] clinics = {
                    new VetClinic("Κτηνιατρικό Κέντρο Πάτρας", "Κορίνθου 150",      "2610-123456", "Δρ. Παπαδόπουλος",  "https://maps.google.com", "0.8"),
                    new VetClinic("Vet Care Patras",             "Μαιζώνος 55",        "2610-987654", "Δρ. Γεωργίου",      "https://maps.google.com", "1.5"),
                    new VetClinic("Animal Health",               "Αγίου Ανδρέου 12",   "2610-555666", "Δρ. Κωνσταντίνου", "https://maps.google.com", "2.2"),
                    new VetClinic("Patras Pet Clinic",           "Ακρωτηρίου 20",      "2610-222333", "Δρ. Νικολάου",      "https://maps.google.com", "3.1"),
                    new VetClinic("City Vets",                   "Έλληνος Στρατιώτου 40","2610-444555","Δρ. Δημητρίου",   "https://maps.google.com", "4.0")
            };

            VBox detailPanel = card(12);
            detailPanel.setVisible(false);
            detailPanel.setManaged(false);
            TextArea detailArea = new TextArea();
            detailArea.setEditable(false);
            detailArea.setFont(Font.font("SansSerif", 13));
            detailArea.setPrefRowCount(5);
            detailArea.setStyle("-fx-background-color:transparent; -fx-border-color:transparent; -fx-background-insets:0;");

            detailArea.lookup(".content");
            detailArea.setStyle("-fx-text-fill: " + DARK + ";");

            Hyperlink mapLink = new Hyperlink("🗺  Άνοιγμα στο Google Maps");
            mapLink.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
            mapLink.setStyle("-fx-text-fill: " + PURPLE + ";");
            detailPanel.getChildren().addAll(detailArea, mapLink);

            // Συμπαγές, γεμάτο μωβ κουμπί
            Button requestBtn = prominentBtnHelper("Αποστολή Αιτήματος", PURPLE, WHITE, 14, 15, 20);
            requestBtn.setVisible(false);
            requestBtn.setManaged(false);

            final VetClinic[] selected = {null};

            ToggleGroup tg = new ToggleGroup();
            for (VetClinic clinic : clinics) {
                RadioButton rb = new RadioButton();
                rb.setToggleGroup(tg);
                rb.setVisible(false);

                VBox clinicCard = card(12);
                clinicCard.setCursor(javafx.scene.Cursor.HAND);

                HBox topRow = new HBox();
                topRow.setAlignment(Pos.CENTER_LEFT);
                Label nameLbl = lbl(clinic.getName(), 14, true, DARK);
                HBox.setHgrow(nameLbl, Priority.ALWAYS);
                Label distBadge = new Label(clinic.getDistance() + " km");
                distBadge.setFont(Font.font("SansSerif", FontWeight.BOLD, 11));
                distBadge.setStyle("-fx-text-fill: " + WHITE + "; -fx-background-color:" + LABEL_PINK + "; -fx-background-radius:20; -fx-padding: 3 10 3 10;");
                topRow.getChildren().addAll(nameLbl, distBadge);

                Label addrLbl = lbl(clinic.getAddress(), 12, false, MUTED);
                Label docLbl  = lbl(clinic.getDoctor(),  12, false, DARK);
                clinicCard.getChildren().addAll(topRow, addrLbl, docLbl);

                clinicCard.setOnMouseClicked(evt -> {
                    selected[0] = clinic;
                    body.getChildren().stream()
                            .filter(n -> n instanceof VBox && n != detailPanel)
                            .forEach(n -> ((VBox) n).setStyle("-fx-background-color:" + CARD_BG +
                                    "; -fx-background-radius:12; -fx-border-color:" + FIELD_GRAY +
                                    "; -fx-border-radius:12; -fx-border-width:1.5;"));

                    clinicCard.setStyle("-fx-background-color:" + WHITE +
                            "; -fx-background-radius:12; -fx-border-color:" + PURPLE +
                            "; -fx-border-radius:12; -fx-border-width:2;");

                    detailArea.setText(clinic.getDetails());
                    mapLink.setOnAction(me -> getHostServices().showDocument(clinic.getMapLink()));
                    detailPanel.setVisible(true);
                    detailPanel.setManaged(true);
                    requestBtn.setVisible(true);
                    requestBtn.setManaged(true);
                    requestBtn.setText("Αποστολή Αιτήματος → " + clinic.getName());
                });

                body.getChildren().add(clinicCard);
            }

            requestBtn.setOnAction(e -> {
                if (selected[0] == null) {
                    showError("Επιλέξτε ένα κτηνιατρείο!");
                    return;
                }
                System.out.println("-> Εστάλη αίτημα στο ιατρείο: " + selected[0].getName());
                showInfo("✓ Αίτημα στάλθηκε: " + selected[0].getName());
                window.setScene(mainScene);
            });

            body.getChildren().addAll(detailPanel, requestBtn);
            scroll.setContent(body);
            root.setTop(topBar);
            root.setCenter(scroll);
            window.setScene(new Scene(root, 420, 700));
        }

        // ── Screen 4: Support (non-medical) ───────────────────────────────────
        private void showSupportScreen(String category) {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            HBox topBar = makeTopBar("Επικοινωνία", () -> showCategoryScreen());

            VBox body = new VBox(14);
            body.setPadding(new Insets(24, 22, 24, 22));

            Label badge = new Label("Συμβάν: " + category);
            badge.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
            badge.setStyle("-fx-text-fill: " + WHITE + "; -fx-background-color:" + RED_ALERT + "; -fx-background-radius:20; -fx-padding: 6 14 6 14;");

            body.getChildren().add(badge);

            String[][] options = {
                    {"👤", "Κλήση Ιδιοκτήτη",  "Άμεση επικοινωνία με τον ιδιοκτήτη", PURPLE,  "Κλήση Ιδιοκτήτη..."},
                    {"🛡️", "petBnb Support",    "24/7 υποστήριξη από την ομάδα μας",   BUTTON_PINK,   "Κλήση petBnb Support..."}
            };

            for (String[] opt : options) {
                VBox supportCard = card(14);
                supportCard.setCursor(javafx.scene.Cursor.HAND);

                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);

                Label iconBox = new Label(opt[0]);
                iconBox.setFont(Font.font(22));
                iconBox.setStyle("-fx-background-color:" + opt[3] + "; -fx-text-fill: white; -fx-background-radius:10; -fx-padding: 10 12 10 12;");

                VBox info = new VBox(3);
                Label title = lbl(opt[1], 15, true, DARK);
                Label sub   = lbl(opt[2], 12, false, MUTED);
                info.getChildren().addAll(title, sub);

                Label arrow = lbl("→", 18, false, MUTED);
                HBox.setHgrow(info, Priority.ALWAYS);
                row.getChildren().addAll(iconBox, info, arrow);
                supportCard.getChildren().add(row);

                String logMsg   = "Επιλογή Χρήστη: " + opt[4];
                String toastMsg = opt[4];
                supportCard.setOnMouseClicked(ev -> {
                    System.out.println("-> " + logMsg);
                    showInfo(toastMsg);
                    window.setScene(mainScene);
                });
                supportCard.setOnMouseEntered(ev -> supportCard.setStyle(
                        "-fx-background-color:#F9F6FC; -fx-background-radius:14;" +
                                "-fx-border-color:" + PURPLE + "; -fx-border-radius:14; -fx-border-width:1.5;"));
                supportCard.setOnMouseExited(ev -> supportCard.setStyle(
                        "-fx-background-color:" + CARD_BG + "; -fx-background-radius:14;" +
                                "-fx-border-color:" + FIELD_GRAY + "; -fx-border-radius:14; -fx-border-width:1.5;"));
                body.getChildren().add(supportCard);
            }

            root.setTop(topBar);
            root.setCenter(body);
            window.setScene(new Scene(root, 420, 700));
        }

        // ── Shared helpers ─────────────────────────────────────────────────────
        private HBox makeTopBar(String title, Runnable backAction) {
            HBox bar = new HBox(10);
            bar.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            bar.setPadding(new Insets(14, 18, 14, 18));
            bar.setAlignment(Pos.CENTER_LEFT);
            Button back = new Button("← Πίσω");
            back.setFont(Font.font("SansSerif", FontWeight.NORMAL, 13));
            back.setStyle("-fx-text-fill: " + WHITE + "; -fx-background-color: transparent; -fx-cursor: hand;");
            back.setOnAction(e -> backAction.run());
            Label hdr = lbl(title, 17, true, WHITE);
            bar.getChildren().addAll(back, hdr);
            return bar;
        }

        private void showInfo(String msg) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            a.setHeaderText(null);
            a.setTitle("petBnb");
            a.showAndWait();
        }

        private void showError(String msg) {
            Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            a.setHeaderText(null);
            a.setTitle("Σφάλμα");
            a.showAndWait();
        }
    }
}
