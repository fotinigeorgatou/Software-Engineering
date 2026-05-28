import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class usecase10_ui_launcher {

    public static void main(String[] args) {
        Application.launch(LiveDiaryApp.class, args);
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
    static final String ORANGE_WARN = "#E67E22";
    static final String GREEN_OK    = "#27AE60";
    static final String RED_ALERT   = "#E62828"; // Προστέθηκε το χρώμα που έλειπε!

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
        b.setCursor(javafx.scene.Cursor.HAND);
        String style = "-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-background-radius: 10; -fx-font-weight: bold;";
        String hover = "-fx-background-color: #333333; -fx-text-fill: " + fg + "; -fx-background-radius: 10; -fx-font-weight: bold;";
        b.setStyle(style);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(style));
        return b;
    }

    static Button actionIconBtn(String icon, String bg) {
        Button b = new Button(icon);
        b.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        b.setPadding(new Insets(10));
        b.setCursor(javafx.scene.Cursor.HAND);
        b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-background-radius: 50;");
        return b;
    }

    // ── App ────────────────────────────────────────────────────────────────────
    public static class LiveDiaryApp extends Application {
        private Stage window;
        private Scene mainScene;
        private LiveDiary diaryModel = new LiveDiary();
        private VBox timelineContainer;
        private Button toggleNetworkBtn;

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            window.setTitle("petBnb — Live Diary");
            createMainScreen();
            window.setScene(mainScene);
            window.show();

            // Dummy αρχικά δεδομένα
            diaryModel.addEntry(ActivityType.FOOD, "Πρωινό γεύμα - 150g ξηρά τροφή.");
            diaryModel.addEntry(ActivityType.WALK, "Βόλτα στο πάρκο (25 λεπτά).");
            refreshTimeline();
        }

        private void createMainScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color:" + BG_LIGHT + ";");

            // --- HEADER ---
            HBox topBar = new HBox(10);
            topBar.setStyle("-fx-background-color:" + PINK_HEADER + ";");
            topBar.setPadding(new Insets(14, 18, 14, 18));
            topBar.setAlignment(Pos.CENTER_LEFT);

            Label hdr = lbl("Live Diary : Bella", 17, true, WHITE);
            HBox.setHgrow(hdr, Priority.ALWAYS);
            hdr.setMaxWidth(Double.MAX_VALUE);

            toggleNetworkBtn = new Button("🟢 Online");
            toggleNetworkBtn.setStyle("-fx-background-color: " + GREEN_OK + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 11; -fx-font-weight: bold;");
            toggleNetworkBtn.setCursor(javafx.scene.Cursor.HAND);
            toggleNetworkBtn.setOnAction(e -> toggleNetwork());

            Button videoCallBtn = new Button("🎥 Καλέστε τον Ιδιοκτήτη");
            videoCallBtn.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-text-fill: white; -fx-border-radius: 20; -fx-border-width: 1.5; -fx-padding: 3 10 3 10; -fx-cursor: hand;");
            videoCallBtn.setOnAction(e -> simulateVideoCall());

            topBar.getChildren().addAll(hdr, toggleNetworkBtn, videoCallBtn);
            root.setTop(topBar);

            // --- TIMELINE ---
            timelineContainer = new VBox(12);
            timelineContainer.setPadding(new Insets(20));

            ScrollPane scroll = new ScrollPane(timelineContainer);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");
            root.setCenter(scroll);

            // --- ACTION BAR ---
            VBox bottomBar = new VBox(8);
            bottomBar.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + FIELD_GRAY + "; -fx-border-width: 1 0 0 0;");
            bottomBar.setPadding(new Insets(15, 20, 15, 20));
            bottomBar.setAlignment(Pos.CENTER);

            Label addLbl = lbl("Προσθήκη Νέας Δραστηριότητας", 11, true, MUTED);
            HBox actions = new HBox(15);
            actions.setAlignment(Pos.CENTER);

            Button btnWalk = actionIconBtn("🦮", PURPLE);
            btnWalk.setOnAction(e -> promptAction(ActivityType.WALK, "Λεπτομέρειες βόλτας:"));

            Button btnFood = actionIconBtn("🦴", BUTTON_PINK);
            btnFood.setOnAction(e -> promptAction(ActivityType.FOOD, "Λεπτομέρειες γεύματος:"));

            Button btnSleep = actionIconBtn("💤", MUTED);
            btnSleep.setOnAction(e -> {
                diaryModel.addEntry(ActivityType.SLEEP, "Ο σκύλος κοιμήθηκε.");
                refreshTimeline();
            });

            Button btnWarn = actionIconBtn("⚠️", ORANGE_WARN);
            btnWarn.setOnAction(e -> showStrangeBehaviorMenu());

            actions.getChildren().addAll(btnWalk, btnFood, btnSleep, btnWarn);
            bottomBar.getChildren().addAll(addLbl, actions);

            root.setBottom(bottomBar);
            mainScene = new Scene(root, 450, 750);
        }

        private void refreshTimeline() {
            timelineContainer.getChildren().clear();

            for (DiaryEntry entry : diaryModel.getTimeline()) {
                VBox card = new VBox(8);
                card.setStyle("-fx-background-color:" + CARD_BG + "; -fx-background-radius: 12; -fx-border-color:" + FIELD_GRAY + "; -fx-border-radius: 12; -fx-border-width:1.5;");
                card.setPadding(new Insets(12));

                HBox header = new HBox(10);
                header.setAlignment(Pos.CENTER_LEFT);

                Label icon = lbl(entry.getEmoji(), 20, false, DARK);
                Label type = lbl(entry.getType().toString(), 13, true, PINK_HEADER);
                HBox.setHgrow(type, Priority.ALWAYS);
                type.setMaxWidth(Double.MAX_VALUE);

                Label time = lbl(entry.getFormattedTime(), 11, false, MUTED);

                if (entry.getSyncStatus() == SyncStatus.PENDING) {
                    Label pendingBadge = lbl("⏳ Εκκρεμεί", 10, true, ORANGE_WARN);
                    header.getChildren().addAll(icon, type, pendingBadge, time);
                } else {
                    header.getChildren().addAll(icon, type, time);
                }

                Label details = lbl(entry.getDetails(), 13, false, DARK);
                details.setWrapText(true);

                card.getChildren().addAll(header, details);

                if (entry.getSyncStatus() == SyncStatus.SYNCED && Math.random() > 0.5) {
                    Label like = lbl("❤️ Ο ιδιοκτήτης είδε την ενημέρωση!", 11, true, LABEL_PINK);
                    card.getChildren().add(like);
                }

                timelineContainer.getChildren().add(card);
            }
        }

        private void toggleNetwork() {
            if (diaryModel.isOnline()) {
                diaryModel.setNetworkStatus(false);
                toggleNetworkBtn.setText("🔴 Offline");
                toggleNetworkBtn.setStyle("-fx-background-color: " + RED_ALERT + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 11; -fx-font-weight: bold;");
                showInfo("Η εφαρμογή μπήκε σε Offline Mode. Οι επόμενες εγγραφές θα αποθηκευτούν τοπικά.");
            } else {
                diaryModel.setNetworkStatus(true);
                toggleNetworkBtn.setText("🟢 Online");
                toggleNetworkBtn.setStyle("-fx-background-color: " + GREEN_OK + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 11; -fx-font-weight: bold;");
                showInfo("Σύνδεση αποκαταστάθηκε! Οι εκκρεμείς εγγραφές συγχρονίστηκαν με το Timeline.");
            }
            refreshTimeline();
        }

        private void promptAction(ActivityType type, String promptText) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("petBnb - Νέα Καταχώρηση");
            dialog.setHeaderText(promptText);
            dialog.showAndWait().ifPresent(details -> {
                if (!details.trim().isEmpty()) {
                    diaryModel.addEntry(type, details);
                    refreshTimeline();
                }
            });
        }

        private void showStrangeBehaviorMenu() {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Περίεργη Συμπεριφορά");
            alert.setHeaderText("Επιλέξτε πώς θέλετε να διαχειριστείτε το περιστατικό:");

            ButtonType btnLog = new ButtonType("Απλή Καταγραφή");
            ButtonType btnCall = new ButtonType("Άμεση Κλήση Ιδιοκτήτη");
            ButtonType btnEmergency = new ButtonType("Μετάβαση σε Emergency");
            ButtonType btnCancel = new ButtonType("Ακύρωση", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnLog, btnCall, btnEmergency, btnCancel);

            alert.showAndWait().ifPresent(type -> {
                if (type == btnLog) {
                    promptAction(ActivityType.STRANGE_BEHAVIOR, "Περιγράψτε την περίεργη συμπεριφορά (π.χ. νωθρότητα):");
                } else if (type == btnCall) {
                    simulateVideoCall();
                } else if (type == btnEmergency) {
                    showInfo("Ανακατεύθυνση στην οθόνη του Έκτακτης Ανάγκης (Use Case 9)...");
                }
            });
        }

        private void simulateVideoCall() {
            Alert dialing = new Alert(Alert.AlertType.INFORMATION, "Κλήση στον Ιδιοκτήτη...", ButtonType.CANCEL);
            dialing.setTitle("Βιντεοκλήση");
            dialing.setHeaderText("Σε αναμονή απάντησης...");
            dialing.showAndWait();

            diaryModel.addEntry(ActivityType.CALL_LOG, "Αναπάντητη κλήση.");
            diaryModel.addEntry(ActivityType.SYSTEM_MSG, "Μήνυμα Ιδιοκτήτη: Είμαι απασχολημένος, θα σας καλέσω σε 10 λεπτά!");
            refreshTimeline();

            showInfo("Ο Ιδιοκτήτης απέρριψε την κλήση και έστειλε αυτοματοποιημένο μήνυμα (Εναλλακτική Ροή 3). Δείτε το Timeline.");
        }

        private void showInfo(String msg) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            a.setHeaderText(null); a.setTitle("petBnb"); a.showAndWait();
        }
    }
}
