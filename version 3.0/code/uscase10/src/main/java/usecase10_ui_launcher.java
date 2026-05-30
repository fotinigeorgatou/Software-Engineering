import javafx.application.Application;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class usecase10_ui_launcher {

    public static void main(String[] args) {
        Application.launch(LiveDiaryApp.class, args);
    }

    // ══════════════════════════════════════════════════════════════════
    // COLOUR PALETTE
    // ══════════════════════════════════════════════════════════════════
    static final String ROSE_HEADER     = "#E8455A";
    static final String ROSE_HEADER_END = "#C73060";
    static final String BG_LIGHT        = "#F7F0F5";
    static final String BUTTON_PINK     = "#E8566B";
    static final String BUTTON_PINK_HOV = "#C73558";
    static final String LABEL_ROSE      = "#E8556A";
    static final String FIELD_GRAY      = "#E8DDE6";
    static final String PURPLE          = "#9C72CC";
    static final String PURPLE_HOV      = "#7A52A8";
    static final String CARD_BG         = "#FFFFFF";
    static final String CARD_BORDER     = "#F0E2EC";
    static final String DARK            = "#2A1F26";
    static final String MUTED           = "#9A8A96";
    static final String MUTED_HOV       = "#6E5E6A";
    static final String WHITE           = "#FFFFFF";
    static final String ORANGE_WARN     = "#E07828";
    static final String ORANGE_HOV      = "#B85E18";
    static final String GREEN_OK        = "#2E9E6A";
    static final String GREEN_HOV       = "#1E7A50";
    static final String RED_ALERT       = "#D42A3A";
    static final String TEAL_ACCENT     = "#2AA8A0";
    static final String TIMELINE_LINE   = "#E0C8D6";

    // ══════════════════════════════════════════════════════════════════
    // TYPOGRAPHY
    // ══════════════════════════════════════════════════════════════════
    static final String FONT_BODY  = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";

    // ══════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ══════════════════════════════════════════════════════════════════

    static Label lbl(String text, int size, boolean bold, String color) {
        Label l = new Label(text);
        l.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-family: %s; -fx-font-size: %dpx; -fx-font-weight: %s;",
                color, FONT_BODY, size, bold ? "bold" : "normal"
        ));
        return l;
    }

    static Button styledBtn(String text, String bgNormal, String bgHover, String fg,
                            int fontSize, int vPad, int hPad, int radius) {
        Button b = new Button(text);
        b.setPadding(new Insets(vPad, hPad, vPad, hPad));
        b.setCursor(javafx.scene.Cursor.HAND);

        String base = buildBtnStyle(bgNormal, fg, fontSize, radius, false);
        String hover = buildBtnStyle(bgHover,  fg, fontSize, radius, false);
        String press = buildBtnStyle(bgHover,  fg, fontSize, radius, true);

        b.setStyle(base);

        b.setOnMouseEntered(e  -> b.setStyle(hover));
        b.setOnMouseExited(e   -> b.setStyle(base));
        b.setOnMousePressed(e  -> {
            b.setStyle(press);
            ScaleTransition sc = new ScaleTransition(Duration.millis(80), b);
            sc.setToX(0.96); sc.setToY(0.96); sc.play();
        });
        b.setOnMouseReleased(e -> {
            b.setStyle(hover);
            ScaleTransition sc = new ScaleTransition(Duration.millis(80), b);
            sc.setToX(1.0);  sc.setToY(1.0);  sc.play();
        });

        return b;
    }

    static String buildBtnStyle(String bg, String fg, int sz, int r, boolean pressed) {
        String shadow = pressed
                ? "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 2, 0, 0, 1);"
                : "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 6, 0, 0, 2);";
        return String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-background-radius: %d;" +
                        "-fx-font-weight: bold; -fx-font-family: %s; -fx-font-size: %dpx; %s",
                bg, fg, r, FONT_BODY, sz, shadow
        );
    }

    static String getSafeEmoji(ActivityType type) {
        switch (type) {
            case FOOD:             return "🍽";
            case WALK:             return "🐕";
            case TOILET:           return "🚽";
            case MEDS:             return "💊";
            case SLEEP:            return "🌙";
            case STRANGE_BEHAVIOR: return "❗";
            case CALL_LOG:         return "🎥";
            case SYSTEM_MSG:       return "📌";
            default:               return "📌";
        }
    }

    static void fadeIn(javafx.scene.Node node, int delayMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(350), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.play();
    }

    static void slideIn(javafx.scene.Node node) {
        node.setOpacity(0);
        node.setTranslateY(-14);
        FadeTransition ft = new FadeTransition(Duration.millis(280), node);
        ft.setFromValue(0); ft.setToValue(1);
        TranslateTransition tt = new TranslateTransition(Duration.millis(280), node);
        tt.setFromY(-14); tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(ft, tt).play();
    }

    // ── App ────────────────────────────────────────────────────────────────────
    public static class LiveDiaryApp extends Application {

        private Stage  window;
        private Scene  mainScene;
        private Scene  chatScene;

        private VBox   timelineContainer;
        private Button toggleNetworkBtn;
        private boolean isSleeping = false;

        private VBox chatMessagesContainer;
        private ScrollPane chatScroll;

        private Booking               booking;
        private EntryController       entryCtrl;
        private NotificationController notifCtrl;
        private CommunicationManager  commMgr;

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            window.setTitle("petBnb — Live Diary & Chat");

            initializeDomainModels();
            createMainScreen();
            createChatScreen();

            window.setScene(mainScene);
            window.show();

            refreshTimeline(true);
        }

        private void initializeDomainModels() {
            Host  host  = new Host("H001", "Σπύρος (Host)");
            Owner owner = new Owner("O001", "Μαρία (Owner)", "token_xyz");
            Pet   pet   = new Pet("Bella", "Labrador", 3);

            booking   = new Booking("B100", host, owner, pet);
            notifCtrl = new NotificationController();
            entryCtrl = new EntryController(booking.getDiary(), notifCtrl, owner.getDeviceToken());
            commMgr   = new CommunicationManager();

            entryCtrl.createEntry(ActivityType.FOOD, "Πρωινό γεύμα · 150g ξηρά τροφή", 0, null);
            DiaryEntry walkEntry = entryCtrl.createEntry(ActivityType.WALK, "Βόλτα στο πάρκο", 25, "park_photo.jpg");
            owner.sendReaction(walkEntry, ReactionType.HEART, notifCtrl, host.getHostId());
        }

        private void createMainScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

            root.setTop(buildHeader());
            root.setCenter(buildScrollArea());
            root.setBottom(buildActionBar());

            mainScene = new Scene(root, 480, 800);
        }

        private HBox buildHeader() {
            HBox bar = new HBox(10);
            bar.setAlignment(Pos.CENTER_LEFT);
            bar.setPadding(new Insets(0, 18, 0, 18));
            bar.setMinHeight(68);
            bar.setStyle("-fx-background-color: linear-gradient(to right, " + ROSE_HEADER + ", " + ROSE_HEADER_END + "); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 10, 0, 0, 3);");

            StackPane avatar = new StackPane();
            Circle circ = new Circle(20);
            circ.setFill(Color.web("#ffffff30"));
            circ.setStroke(Color.web("#ffffff60"));
            circ.setStrokeWidth(1.5);
            Label avatarLbl = new Label("🐾");
            avatarLbl.setStyle("-fx-font-family: \"Segoe UI Emoji\"; -fx-font-size: 18px; -fx-text-fill: white;");
            avatar.getChildren().addAll(circ, avatarLbl);

            VBox titleBlock = new VBox(1);
            Label petName = lbl("Bella", 17, true, WHITE);
            Label subLine = lbl("Live Diary", 11, false, "#FFD8E2");
            titleBlock.getChildren().addAll(petName, subLine);
            HBox.setHgrow(titleBlock, Priority.ALWAYS);

            toggleNetworkBtn = buildPillBtn("🟢", GREEN_OK, GREEN_HOV, 12);
            toggleNetworkBtn.setOnAction(e -> toggleNetwork());
            toggleNetworkBtn.setPadding(new Insets(6, 10, 6, 10));

            Button chatBtn = new Button("💬");
            chatBtn.setCursor(javafx.scene.Cursor.HAND);
            chatBtn.setPadding(new Insets(6, 10, 6, 10));
            chatBtn.setStyle("-fx-background-color: #ffffff25; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            chatBtn.setOnMouseEntered(e -> chatBtn.setStyle("-fx-background-color: #ffffff40; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;"));
            chatBtn.setOnMouseExited(e -> chatBtn.setStyle("-fx-background-color: #ffffff25; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;"));
            chatBtn.setOnAction(e -> {
                refreshChat();
                window.setScene(chatScene);
            });

            Button videoCallBtn = new Button("🎥");
            videoCallBtn.setCursor(javafx.scene.Cursor.HAND);
            videoCallBtn.setPadding(new Insets(6, 10, 6, 10));
            videoCallBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;");
            videoCallBtn.setOnMouseEntered(e -> videoCallBtn.setStyle("-fx-background-color: #ffffff25; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;"));
            videoCallBtn.setOnMouseExited(e -> videoCallBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #FFFFFF; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5; -fx-font-family: " + FONT_BODY + "; -fx-font-weight: bold; -fx-font-size: 12px;"));
            videoCallBtn.setOnAction(e -> simulateVideoCall());

            bar.getChildren().addAll(avatar, titleBlock, toggleNetworkBtn, chatBtn, videoCallBtn);
            fadeIn(bar, 0);
            return bar;
        }

        private Button buildPillBtn(String text, String bg, String hov, int fontSize) {
            Button b = new Button(text);
            b.setCursor(javafx.scene.Cursor.HAND);
            b.setPadding(new Insets(6, 14, 6, 14));
            String base = buildPillStyle(bg, fontSize);
            String hover = buildPillStyle(hov, fontSize);
            b.setStyle(base);
            b.setOnMouseEntered(e -> b.setStyle(hover));
            b.setOnMouseExited(e  -> b.setStyle(base));
            return b;
        }

        private String buildPillStyle(String bg, int fontSize) {
            return String.format(
                    "-fx-background-color: %s; -fx-text-fill: white; -fx-background-radius: 20;" +
                            "-fx-font-weight: bold; -fx-font-family: %s; -fx-font-size: %dpx;",
                    bg, FONT_BODY, fontSize
            );
        }

        private ScrollPane buildScrollArea() {
            timelineContainer = new VBox(10);
            timelineContainer.setPadding(new Insets(18, 16, 10, 16));

            ScrollPane scroll = new ScrollPane(timelineContainer);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            return scroll;
        }

        private VBox buildActionBar() {
            VBox bar = new VBox(12);
            bar.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.5 0 0 0; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, -2);");
            bar.setPadding(new Insets(14, 14, 20, 14));
            bar.setAlignment(Pos.CENTER);

            Label addLbl = lbl("Νέα Δραστηριότητα", 11, true, MUTED);
            addLbl.setStyle(addLbl.getStyle() + " -fx-letter-spacing: 0.08em; -fx-padding: 0 0 2 0;");

            Separator sep = new Separator();
            sep.setStyle("-fx-background-color: " + FIELD_GRAY + ";");

            FlowPane actions = new FlowPane(Orientation.HORIZONTAL, 8, 8);
            actions.setAlignment(Pos.CENTER);

            Button btnWalk  = styledBtn("🐕  Βόλτα",    PURPLE,      PURPLE_HOV,  WHITE, 12, 9, 16, 22);
            // ΑΛΛΑΓΗ: Ενημέρωση κειμένου prompt
            Button btnFood  = styledBtn("🍽  Φαγητό",   BUTTON_PINK, BUTTON_PINK_HOV, WHITE, 12, 9, 16, 22);
            Button btnSleep = buildSleepButton();
            Button btnOther = styledBtn("➕  Άλλη…",    MUTED,       MUTED_HOV,   WHITE, 12, 9, 16, 22);
            Button btnWarn  = styledBtn("❗  Κίνδυνος", ORANGE_WARN, ORANGE_HOV,  WHITE, 12, 9, 16, 22);

            btnWalk.setOnAction(e -> promptAction(ActivityType.WALK,  "Λεπτομέρειες βόλτας (Προαιρετικό):"));
            btnFood.setOnAction(e -> promptAction(ActivityType.FOOD,  "Λεπτομέρειες γεύματος (Προαιρετικό):"));
            btnOther.setOnAction(e -> showOtherActivityMenu());
            btnWarn.setOnAction(e  -> showStrangeBehaviorMenu());

            actions.getChildren().addAll(btnWalk, btnFood, btnSleep, btnOther, btnWarn);
            bar.getChildren().addAll(addLbl, sep, actions);
            fadeIn(bar, 80);
            return bar;
        }

        private Button buildSleepButton() {
            final String[] state = {"sleep"};

            Button b = new Button("🌙  Ύπνος");
            b.setPadding(new Insets(9, 16, 9, 16));
            b.setCursor(javafx.scene.Cursor.HAND);
            applySleepStyle(b, false);

            b.setOnMouseEntered(e -> {
                boolean awake = state[0].equals("awake");
                b.setStyle(buildBtnStyle(awake ? GREEN_HOV : MUTED_HOV, WHITE, 12, 22, false));
            });
            b.setOnMouseExited(e -> applySleepStyle(b, state[0].equals("awake")));

            b.setOnMousePressed(e -> {
                boolean awake = state[0].equals("awake");
                b.setStyle(buildBtnStyle(awake ? GREEN_HOV : MUTED_HOV, WHITE, 12, 22, true));
                ScaleTransition sc = new ScaleTransition(Duration.millis(80), b);
                sc.setToX(0.96); sc.setToY(0.96); sc.play();
            });
            b.setOnMouseReleased(e -> {
                boolean awake = state[0].equals("awake");
                b.setStyle(buildBtnStyle(awake ? GREEN_HOV : MUTED_HOV, WHITE, 12, 22, false));
                ScaleTransition sc = new ScaleTransition(Duration.millis(80), b);
                sc.setToX(1.0);  sc.setToY(1.0);  sc.play();
            });

            b.setOnAction(e -> {
                if (!isSleeping) {
                    isSleeping = true;
                    state[0] = "awake";
                    entryCtrl.createEntry(ActivityType.SLEEP, "Το κατοικίδιο κοιμήθηκε.", 0, null);
                    b.setText("☀️  Ξύπνησε");
                    applySleepStyle(b, true);
                } else {
                    isSleeping = false;
                    state[0] = "sleep";
                    entryCtrl.createEntry(ActivityType.SLEEP, "Το κατοικίδιο ξύπνησε και είναι ενεργό!", 0, null);
                    b.setText("🌙  Ύπνος");
                    applySleepStyle(b, false);
                }
                refreshTimeline(true);
            });

            return b;
        }

        private void applySleepStyle(Button b, boolean awake) {
            b.setStyle(buildBtnStyle(awake ? GREEN_OK : MUTED, WHITE, 12, 22, false));
        }

        private void refreshTimeline(boolean animate) {
            timelineContainer.getChildren().clear();

            Label sectionHdr = lbl("Σήμερα", 11, true, MUTED);
            sectionHdr.setStyle(sectionHdr.getStyle() + " -fx-padding: 0 0 4 4; -fx-letter-spacing: 0.1em;");
            timelineContainer.getChildren().add(sectionHdr);

            List<DiaryEntry> entries = booking.getDiary().getTimeline();
            for (int i = 0; i < entries.size(); i++) {
                DiaryEntry entry = entries.get(i);

                HBox row = buildTimelineRow(entry);
                timelineContainer.getChildren().add(1, row);

                if (animate) {
                    fadeIn(row, i * 45);
                } else {
                    slideIn(row);
                }
            }
        }

        private HBox buildTimelineRow(DiaryEntry entry) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.TOP_CENTER);

            VBox dotCol = new VBox(4);
            dotCol.setAlignment(Pos.TOP_CENTER);
            dotCol.setMinWidth(30);

            String emojiStr = resolveEmoji(entry);
            Label emojiIcon = new Label(emojiStr);
            emojiIcon.setStyle("-fx-text-fill: " + DARK + "; -fx-font-family: \"Segoe UI Emoji\"; -fx-font-size: 22px;");

            if (entry.getType() == ActivityType.STRANGE_BEHAVIOR) {
                DropShadow glow = new DropShadow(8, Color.web(ORANGE_WARN));
                emojiIcon.setEffect(glow);
            }

            Region line = new Region();
            line.setMinWidth(2);
            line.setMaxWidth(2);
            line.setStyle("-fx-background-color: " + TIMELINE_LINE + "; -fx-background-radius: 1;");
            VBox.setVgrow(line, Priority.ALWAYS);

            dotCol.getChildren().addAll(emojiIcon, line);

            VBox card = buildEntryCard(entry);
            HBox.setHgrow(card, Priority.ALWAYS);

            row.getChildren().addAll(dotCol, card);
            return row;
        }

        private VBox buildEntryCard(DiaryEntry entry) {
            VBox card = new VBox(6);
            card.setStyle(buildCardStyle(entry));
            card.setPadding(new Insets(12, 14, 12, 14));

            HBox hdr = new HBox(8);
            hdr.setAlignment(Pos.CENTER_LEFT);

            Label typeLbl = lbl(resolveDisplayLabel(entry), 13, true, getAccentColor(entry.getType()));
            HBox.setHgrow(typeLbl, Priority.ALWAYS);
            typeLbl.setMaxWidth(Double.MAX_VALUE);

            HBox badgeBox = new HBox(6);
            badgeBox.setAlignment(Pos.CENTER_RIGHT);

            if (entry.getSyncStatus() == SyncStatus.PENDING) {
                badgeBox.getChildren().add(buildBadge("⏳ Εκκρεμεί", ORANGE_WARN + "22", ORANGE_WARN));
            }

            Label timeLbl = lbl(entry.getFormattedTime(), 11, false, MUTED);
            badgeBox.getChildren().add(timeLbl);

            hdr.getChildren().addAll(typeLbl, badgeBox);

            Label details = lbl(entry.getDetails(), 13, false, DARK);
            details.setWrapText(true);
            details.setStyle(details.getStyle() + " -fx-line-spacing: 2;");

            HBox meta = new HBox(10);
            meta.setAlignment(Pos.CENTER_LEFT);

            if (entry.getDurationMinutes() > 0) {
                meta.getChildren().add(buildBadge("⏱ " + entry.getDurationMinutes() + " λεπτά", PURPLE + "18", PURPLE));
            }
            if (entry.hasPhoto()) {
                meta.getChildren().add(buildBadge("📷 Φωτογραφία", TEAL_ACCENT + "18", TEAL_ACCENT));
            }

            card.getChildren().addAll(hdr, details);
            if (!meta.getChildren().isEmpty()) card.getChildren().add(meta);

            if (entry.getOwnerReaction() != null) {
                HBox reactionRow = new HBox(6);
                reactionRow.setAlignment(Pos.CENTER_LEFT);
                Label reactionLbl = new Label(entry.getOwnerReaction().getEmoji());
                reactionLbl.setStyle("-fx-text-fill: " + DARK + "; -fx-font-family: \"Segoe UI Emoji\"; -fx-font-size: 15px;");
                Label reactionTxt = lbl("Ο ιδιοκτήτης αντέδρασε  ·  " + entry.getOwnerReaction().getFormattedTime(), 11, false, LABEL_ROSE);
                reactionRow.getChildren().addAll(reactionLbl, reactionTxt);

                Separator rs = new Separator();
                rs.setStyle("-fx-background-color: " + FIELD_GRAY + "; -fx-padding: 2 0 0 0;");
                card.getChildren().addAll(rs, reactionRow);
            }

            return card;
        }

        private String buildCardStyle(DiaryEntry entry) {
            String border = entry.getType() == ActivityType.STRANGE_BEHAVIOR
                    ? ORANGE_WARN
                    : entry.getSyncStatus() == SyncStatus.PENDING
                      ? ORANGE_WARN + "88"
                      : CARD_BORDER;
            return String.format(
                    "-fx-background-color: %s; -fx-background-radius: 14;" +
                            "-fx-border-color: %s; -fx-border-radius: 14; -fx-border-width: 1.5;" +
                            "-fx-effect: dropshadow(gaussian, rgba(180,80,120,0.07), 8, 0, 0, 2);",
                    CARD_BG, border
            );
        }

        private Label buildBadge(String text, String bg, String fgColor) {
            Label badge = new Label(text);
            badge.setStyle(String.format(
                    "-fx-background-color: %s; -fx-text-fill: %s; -fx-background-radius: 10;" +
                            "-fx-padding: 2 8 2 8; -fx-font-family: %s; -fx-font-size: 10px; -fx-font-weight: bold;",
                    bg, fgColor, FONT_BODY
            ));
            return badge;
        }

        private String getAccentColor(ActivityType type) {
            switch (type) {
                case FOOD:             return BUTTON_PINK;
                case WALK:             return PURPLE;
                case TOILET:           return TEAL_ACCENT;
                case MEDS:             return "#5588DD";
                case SLEEP:            return MUTED;
                case STRANGE_BEHAVIOR: return ORANGE_WARN;
                case CALL_LOG:         return ROSE_HEADER;
                case SYSTEM_MSG:       return MUTED;
                default:               return MUTED;
            }
        }

        private String resolveEmoji(DiaryEntry entry) {
            if (entry.getType() == ActivityType.SLEEP && entry.getDetails().contains("ξύπνησε"))
                return "☀️";
            return getSafeEmoji(entry.getType());
        }

        private String resolveDisplayLabel(DiaryEntry entry) {
            if (entry.getType() == ActivityType.SYSTEM_MSG && !entry.getDetails().contains("Μήνυμα Ιδιοκτήτη")) {
                return "Άλλη Δραστηριότητα";
            }
            return entry.getDisplayLabel();
        }

        private void toggleNetwork() {
            LiveDiary diary = booking.getDiary();
            if (diary.isOnline()) {
                diary.setNetworkStatus(false);
                toggleNetworkBtn.setText("🔴");
                String style = buildPillStyle(RED_ALERT, 12);
                toggleNetworkBtn.setStyle(style);
                toggleNetworkBtn.setOnMouseEntered(e -> toggleNetworkBtn.setStyle(buildPillStyle("#AA1A28", 12)));
                toggleNetworkBtn.setOnMouseExited(e  -> toggleNetworkBtn.setStyle(style));
                showInfo("Η εφαρμογή μπήκε σε Offline Mode.\nΟι επόμενες εγγραφές θα αποθηκευτούν τοπικά.");
            } else {
                diary.setNetworkStatus(true);
                toggleNetworkBtn.setText("🟢");
                String style = buildPillStyle(GREEN_OK, 12);
                toggleNetworkBtn.setStyle(style);
                toggleNetworkBtn.setOnMouseEntered(e -> toggleNetworkBtn.setStyle(buildPillStyle(GREEN_HOV, 12)));
                toggleNetworkBtn.setOnMouseExited(e  -> toggleNetworkBtn.setStyle(style));
                showInfo("Σύνδεση αποκαταστάθηκε!\nΟι εκκρεμείς εγγραφές συγχρονίστηκαν.");
            }
            refreshTimeline(false);
        }

        // ---------------------------------------------------------------------
        // ΦΙΞ: Η Βόλτα και το Φαγητό πλέον δέχονται κενό σχόλιο και αυτό-συμπληρώνονται!
        // ---------------------------------------------------------------------
        private void promptAction(ActivityType type, String promptText) {
            TextInputDialog dialog = new TextInputDialog();
            styleDialog(dialog);
            dialog.setTitle("petBnb · Νέα Καταχώρηση");
            dialog.setHeaderText(promptText);

            // Έλεγχος αν πρόκειται για Βόλτα ή Φαγητό
            if (type == ActivityType.WALK || type == ActivityType.FOOD) {
                dialog.setContentText("Σχόλιο (Προαιρετικό):");
            } else {
                dialog.setContentText("Σχόλιο:");
            }

            dialog.showAndWait().ifPresent(details -> {
                String finalDetails = details.trim();

                // Αυτόματη συμπλήρωση αν αφεθεί κενό
                if (type == ActivityType.WALK && finalDetails.isEmpty()) {
                    finalDetails = "Βόλτα";
                } else if (type == ActivityType.FOOD && finalDetails.isEmpty()) {
                    finalDetails = "Φαγητό";
                }

                if (!finalDetails.isEmpty()) {
                    entryCtrl.createEntry(type, finalDetails, 0, null);
                    refreshTimeline(true);
                }
            });
        }

        private void showOtherActivityMenu() {
            List<String> choices = Arrays.asList("Πήγε Τουαλέτα", "Φάρμακα", "Άλλο");
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Πήγε Τουαλέτα", choices);
            styleDialog(dialog);
            dialog.setTitle("petBnb · Άλλη Δραστηριότητα");
            dialog.setHeaderText("Επιλέξτε τον τύπο της δραστηριότητας:");
            dialog.setContentText("Δραστηριότητα:");
            dialog.showAndWait().ifPresent(choice -> {
                switch (choice) {
                    case "Πήγε Τουαλέτα":
                        entryCtrl.createEntry(ActivityType.TOILET, "Το κατοικίδιο πήγε τουαλέτα.", 0, null);
                        refreshTimeline(true);
                        break;
                    case "Φάρμακα":
                        promptAction(ActivityType.MEDS, "Ποιο φάρμακο χορηγήσατε;");
                        break;
                    case "Άλλο":
                        promptAction(ActivityType.SYSTEM_MSG, "Εισάγετε περιγραφή:");
                        break;
                }
            });
        }

        private void showStrangeBehaviorMenu() {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            styleDialog(alert);
            alert.setTitle("petBnb · Περίεργη Συμπεριφορά");
            alert.setHeaderText("Πώς θέλετε να διαχειριστείτε το περιστατικό;");

            ButtonType btnLog       = new ButtonType("Απλή Καταγραφή");
            ButtonType btnCall      = new ButtonType("Άμεση Κλήση");
            ButtonType btnEmergency = new ButtonType("🚨 Emergency");
            ButtonType btnCancel    = new ButtonType("Ακύρωση", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnLog, btnCall, btnEmergency, btnCancel);

            alert.showAndWait().ifPresent(type -> {
                if      (type == btnLog)       promptAction(ActivityType.STRANGE_BEHAVIOR, "Περιγράψτε τη συμπεριφορά:");
                else if (type == btnCall)      simulateVideoCall();
                else if (type == btnEmergency) showInfo("Ανακατεύθυνση στην οθόνη Έκτακτης Ανάγκης (Use Case 9)…");
            });
        }

        private void simulateVideoCall() {
            Alert dialing = new Alert(Alert.AlertType.INFORMATION, "Κλήση στον Ιδιοκτήτη…", ButtonType.CANCEL);
            styleDialog(dialing);
            dialing.setTitle("petBnb · Βιντεοκλήση");
            dialing.setHeaderText("Σε αναμονή απάντησης…");
            dialing.showAndWait();

            VideoCall call = commMgr.routeCall(booking.getHost().getHostId(), booking.getOwner().getOwnerId());
            String busyMsg = Message.getPredefinedBusyMessages()[0];
            commMgr.rejectCallWithMessage(call, booking, booking.getOwner().getOwnerId(), busyMsg, true);

            entryCtrl.createEntry(ActivityType.CALL_LOG, call.getCallSummary(), 0, null);
            entryCtrl.createEntry(ActivityType.SYSTEM_MSG, "Μήνυμα Ιδιοκτήτη: " + busyMsg, 0, null);

            refreshTimeline(true);
            refreshChat();
            showInfo("Ο Ιδιοκτήτης απέρριψε την κλήση και έστειλε αυτοματοποιημένο μήνυμα (Εναλλακτική Ροή 3).");
        }

        // ══════════════════════════════════════════════════════════════════
        // CHAT SCREEN
        // ══════════════════════════════════════════════════════════════════
        private void createChatScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: " + BG_LIGHT + ";");

            HBox topBar = new HBox(15);
            topBar.setAlignment(Pos.CENTER_LEFT);
            topBar.setPadding(new Insets(0, 18, 0, 10));
            topBar.setMinHeight(68);
            topBar.setStyle("-fx-background-color: linear-gradient(to right, " + ROSE_HEADER + ", " + ROSE_HEADER_END + "); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 10, 0, 0, 3);");

            Button backBtn = new Button("←");
            backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
            backBtn.setOnAction(e -> {
                refreshTimeline(false);
                window.setScene(mainScene);
            });

            VBox titleBlock = new VBox(1);
            Label ownerName = lbl("Συνομιλία", 16, true, WHITE);
            Label subLine = lbl("Ιστορικό Μηνυμάτων", 11, false, "#FFD8E2");
            titleBlock.getChildren().addAll(ownerName, subLine);

            topBar.getChildren().addAll(backBtn, titleBlock);
            root.setTop(topBar);

            chatMessagesContainer = new VBox(15);
            chatMessagesContainer.setPadding(new Insets(20));

            chatScroll = new ScrollPane(chatMessagesContainer);
            chatScroll.setFitToWidth(true);
            chatScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
            root.setCenter(chatScroll);

            VBox bottomContainer = new VBox();
            bottomContainer.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + CARD_BORDER + "; -fx-border-width: 1.5 0 0 0;");

            HBox hostInputBar = new HBox(10);
            hostInputBar.setPadding(new Insets(10));
            hostInputBar.setAlignment(Pos.CENTER);

            Label hostLbl = lbl("Host:", 12, true, PURPLE);
            TextField hostMsgField = new TextField();
            hostMsgField.setPromptText("Μήνυμα ως Host...");
            hostMsgField.setStyle("-fx-background-color: " + BG_LIGHT + "; -fx-background-radius: 20; -fx-padding: 8 15; -fx-font-family: " + FONT_BODY + "; -fx-font-size: 13px;");
            HBox.setHgrow(hostMsgField, Priority.ALWAYS);

            Button hostSendBtn = styledBtn("➤", PURPLE, PURPLE_HOV, WHITE, 12, 6, 12, 20);
            hostSendBtn.setOnAction(e -> {
                if (!hostMsgField.getText().trim().isEmpty()) {
                    commMgr.sendMessage(booking, booking.getHost().getHostId(), hostMsgField.getText().trim(), false);
                    hostMsgField.clear();
                    refreshChat();
                }
            });
            hostMsgField.setOnAction(e -> hostSendBtn.fire());
            hostInputBar.getChildren().addAll(hostLbl, hostMsgField, hostSendBtn);

            HBox ownerInputBar = new HBox(10);
            ownerInputBar.setPadding(new Insets(0, 10, 15, 10));
            ownerInputBar.setAlignment(Pos.CENTER);

            Label ownerLbl = lbl("Owner:", 12, true, MUTED);
            TextField ownerMsgField = new TextField();
            ownerMsgField.setPromptText("Μήνυμα ως Owner...");
            ownerMsgField.setStyle("-fx-background-color: " + BG_LIGHT + "; -fx-background-radius: 20; -fx-padding: 8 15; -fx-font-family: " + FONT_BODY + "; -fx-font-size: 13px;");
            HBox.setHgrow(ownerMsgField, Priority.ALWAYS);

            Button ownerSendBtn = styledBtn("➤", MUTED, MUTED_HOV, WHITE, 12, 6, 12, 20);
            ownerSendBtn.setOnAction(e -> {
                if (!ownerMsgField.getText().trim().isEmpty()) {
                    commMgr.sendMessage(booking, booking.getOwner().getOwnerId(), ownerMsgField.getText().trim(), false);
                    ownerMsgField.clear();
                    refreshChat();
                }
            });
            ownerMsgField.setOnAction(e -> ownerSendBtn.fire());
            ownerInputBar.getChildren().addAll(ownerLbl, ownerMsgField, ownerSendBtn);

            bottomContainer.getChildren().addAll(hostInputBar, ownerInputBar);
            root.setBottom(bottomContainer);

            chatScene = new Scene(root, 480, 800);
        }

        private void refreshChat() {
            chatMessagesContainer.getChildren().clear();

            for (Message msg : booking.getMessages()) {
                boolean isHost = msg.getSenderId().equals(booking.getHost().getHostId());

                HBox row = new HBox();
                row.setAlignment(isHost ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                VBox bubble = new VBox(4);
                bubble.setPadding(new Insets(10, 14, 10, 14));
                bubble.setMaxWidth(300);

                String bgCol = isHost ? PURPLE : WHITE;
                String fgCol = isHost ? WHITE : DARK;
                String radius = isHost ? "15 15 2 15" : "15 15 15 2";
                String shadow = isHost ? "" : "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);";
                String border = isHost ? "" : "-fx-border-color: " + FIELD_GRAY + "; -fx-border-radius: " + radius + "; -fx-border-width: 1;";

                bubble.setStyle("-fx-background-color: " + bgCol + "; -fx-background-radius: " + radius + "; " + border + shadow);

                Label text = lbl(msg.getContent(), 13, false, fgCol);
                text.setWrapText(true);

                Label time = lbl(msg.getFormattedTime(), 10, false, isHost ? "#E0C8D6" : MUTED);
                time.setAlignment(isHost ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                time.setMaxWidth(Double.MAX_VALUE);

                bubble.getChildren().addAll(text, time);
                row.getChildren().add(bubble);

                chatMessagesContainer.getChildren().add(row);
            }

            // Auto-scroll
            AnimationTimer timer = new AnimationTimer() {
                int frameCount = 0;
                @Override
                public void handle(long now) {
                    chatScroll.setVvalue(1.0);
                    if (frameCount++ > 2) {
                        this.stop();
                    }
                }
            };
            timer.start();
        }

        private void showInfo(String msg) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            styleDialog(a);
            a.setHeaderText(null);
            a.setTitle("petBnb");
            a.showAndWait();
        }

        private void styleDialog(Dialog<?> d) {
            d.getDialogPane().setStyle("-fx-font-family: " + FONT_BODY + "; -fx-font-size: 13px; -fx-background-color: " + WHITE + ";");
        }
    }
}