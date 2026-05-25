import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class usecase9_ui_launcher {

    public static void main(String[] args) {
        Application.launch(EmergencyApp.class, args);
    }

    public static class EmergencyApp extends Application {

        private Stage window;
        private Scene mainScene;

        @Override
        public void start(Stage primaryStage) {
            this.window = primaryStage;
            window.setTitle("petBnb - Emergency System");
            createMainScreen();
            window.setScene(mainScene);
            window.show();
        }

        private void createMainScreen() {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #F9FAF3;");
            Label title = new Label("Active Hosting: Bella");
            title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            Button emergencyBtn = new Button("EMERGENCY");
            emergencyBtn.setStyle("-fx-background-color: #E62828; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 15 30; -fx-background-radius: 10; -fx-cursor: hand;");
            emergencyBtn.setOnAction(e -> showCategoryScreen());
            root.getChildren().addAll(title, emergencyBtn);
            mainScene = new Scene(root, 450, 750);
        }

        private void showCategoryScreen() {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #F9FAF3;");
            Button backBtn = new Button("←");
            backBtn.setOnAction(e -> window.setScene(mainScene));
            root.setTop(backBtn);

            VBox center = new VBox(15);
            center.setAlignment(Pos.CENTER);
            ComboBox<String> categoryBox = new ComboBox<>();
            categoryBox.getItems().addAll("Ιατρικό", "Απόδραση ζώου", "Επιθετική συμπεριφορά", "Άλλο");
            categoryBox.setValue("Ιατρικό");
            TextArea commentArea = new TextArea();
            commentArea.setPromptText("π.χ. το σκυλί έφαγε σοκολάτα...");
            commentArea.setMaxWidth(300);

            Button submitBtn = new Button("Υποβολή");
            submitBtn.setOnAction(e -> {
                String selectedCategory = categoryBox.getValue();
                String comment = commentArea.getText();

                // ---------------------------------------------------------
                // ΣΥΝΔΕΣΗ ΜΕ ΤΑ MODELS: Εκτύπωση στο terminal!
                // ---------------------------------------------------------
                EmergencyEvent event = new EmergencyEvent();
                event.startEvent(selectedCategory, comment);

                if (selectedCategory.equals("Ιατρικό")) showMapAndClinicsScreen();
                else showSupportScreen(selectedCategory);
            });

            center.getChildren().addAll(new Label("Κατηγορία:"), categoryBox, new Label("Σχόλιο:"), commentArea, submitBtn);
            root.setCenter(center);

            HBox bottom = new HBox(10);
            bottom.setAlignment(Pos.CENTER);
            bottom.setPadding(new Insets(20));
            Button callOwner = new Button("📞 Ιδιοκτήτης");
            Button callSupport = new Button("📞 petBnb Support");

            callOwner.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Πραγματοποιείται κλήση στον Ιδιοκτήτη...");
                new Alert(Alert.AlertType.INFORMATION, "Κλήση Ιδιοκτήτη...").showAndWait();
            });
            callSupport.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Πραγματοποιείται κλήση στην Υποστήριξη...");
                new Alert(Alert.AlertType.INFORMATION, "Κλήση Support...").showAndWait();
            });

            bottom.getChildren().addAll(callOwner, callSupport);
            root.setBottom(bottom);

            window.setScene(new Scene(root, 450, 750));
        }

        private void showMapAndClinicsScreen() {
            BorderPane root = new BorderPane();
            Button backBtn = new Button("←");
            backBtn.setOnAction(e -> showCategoryScreen());
            root.setTop(backBtn);

            ListView<VetClinic> listView = new ListView<>();
            listView.getItems().addAll(
                    new VetClinic("Κτηνιατρικό Κέντρο Πάτρας", "Κορίνθου 150", "2610-123456", "Δρ. Παπαδόπουλος", "http://google.com/maps", "0.8"),
                    new VetClinic("Vet Care Patras", "Μαιζώνος 55", "2610-987654", "Δρ. Γεωργίου", "http://google.com/maps", "1.5"),
                    new VetClinic("Animal Health", "Αγίου Ανδρέου 12", "2610-555666", "Δρ. Κωνσταντίνου", "http://google.com/maps", "2.2"),
                    new VetClinic("Patras Pet Clinic", "Ακρωτηρίου 20", "2610-222333", "Δρ. Νικολάου", "http://google.com/maps", "3.1"),
                    new VetClinic("City Vets", "Έλληνος Στρατιώτου 40", "2610-444555", "Δρ. Δημητρίου", "http://google.com/maps", "4.0")
            );
            listView.setMaxWidth(300);

            TextArea infoArea = new TextArea();
            infoArea.setEditable(false);
            infoArea.setMaxWidth(300);
            infoArea.setPrefHeight(120);

            Hyperlink mapLink = new Hyperlink("Άνοιγμα στο Google Maps");
            mapLink.setVisible(false);

            listView.getSelectionModel().selectedItemProperty().addListener((obs, old, newClinic) -> {
                if (newClinic != null) {
                    infoArea.setText(newClinic.getDetails());
                    mapLink.setVisible(true);
                    mapLink.setOnAction(e -> getHostServices().showDocument(newClinic.getMapLink()));
                }
            });

            Button requestBtn = new Button("Αποστολή Αιτήματος");
            requestBtn.setOnAction(e -> {
                if (listView.getSelectionModel().getSelectedItem() == null) {
                    new Alert(Alert.AlertType.ERROR, "Επιλέξτε ένα ιατρείο!").showAndWait();
                } else {
                    String clinicName = listView.getSelectionModel().getSelectedItem().getName();
                    System.out.println("-> Εστάλη αίτημα στο ιατρείο: " + clinicName);
                    new Alert(Alert.AlertType.INFORMATION, "Αίτημα προς: " + clinicName).showAndWait();
                    window.setScene(mainScene);
                }
            });

            VBox content = new VBox(10, new Label("Επιλογή Κτηνιατρείου:"), listView, infoArea, mapLink, requestBtn);
            content.setAlignment(Pos.CENTER);
            root.setCenter(content);
            window.setScene(new Scene(root, 450, 750));
        }

        private void showSupportScreen(String category) {
            BorderPane root = new BorderPane();
            Button backBtn = new Button("←");
            backBtn.setOnAction(e -> showCategoryScreen());
            root.setTop(backBtn);
            VBox center = new VBox(20);
            center.setAlignment(Pos.CENTER);
            Button callOwner = new Button("📞 Κλήση Ιδιοκτήτη");
            Button callSupport = new Button("📞 Κλήση petBnb Support");

            callOwner.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Πραγματοποιείται κλήση στον Ιδιοκτήτη...");
                new Alert(Alert.AlertType.INFORMATION, "Κλήση Ιδιοκτήτη...").showAndWait();
                window.setScene(mainScene);
            });
            callSupport.setOnAction(e -> {
                System.out.println("-> Επιλογή Χρήστη: Πραγματοποιείται κλήση στην Υποστήριξη...");
                new Alert(Alert.AlertType.INFORMATION, "Κλήση Support...").showAndWait();
                window.setScene(mainScene);
            });

            center.getChildren().addAll(new Label("Επίλεξε για: " + category), callOwner, callSupport);
            root.setCenter(center);
            window.setScene(new Scene(root, 450, 750));
        }
    }
}
