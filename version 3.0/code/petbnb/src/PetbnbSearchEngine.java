import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PetbnbSearchEngine extends JFrame {

    // --- Color Palette (Preserved Aesthetic) ---
    private static final Color PINK_HEADER = new Color(255, 94, 120);
    private static final Color BG_LIGHT = new Color(242, 242, 240);
    private static final Color CARD_BG = new Color(252, 252, 248);
    private static final Color BUTTON_PINK = new Color(255, 110, 140);
    private static final Color LABEL_PINK = new Color(255, 160, 180);
    private static final Color FIELD_GRAY = new Color(230, 230, 230);
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color MAP_BLUE = new Color(210, 230, 245);

    // Modern Framework State
    private final List<HostCardData> allHosts = new ArrayList<>();
    private JPanel cardsContainer;
    private JPanel advancedFiltersPanel;
    private JPanel mapPanel;

    // Filters State Components
    private JTextField txtLocation;
    private JComboBox<String> cbPets;
    private JSlider priceSlider;
    private JSlider radiusSlider;
    private JCheckBox chkNoPets, chkGarden, chkMeds;
    private JComboBox<String> cbRating;

    public PetbnbSearchEngine() {
        initMockData();
        initializeWindowSettings();
        buildAndAssembleUI();
        triggerSearch(); // Initial render
    }

    private void initializeWindowSettings() {
        setTitle("petbnb - Εύρεση Καταλύματος");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 780);
        setLocationRelativeTo(null);
    }

    private void buildAndAssembleUI() {
        JPanel mainLayout = new JPanel(new BorderLayout());
        mainLayout.setBackground(BG_LIGHT);

        // Sub-modules creation
        mainLayout.add(createTopBar(), BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.add(createSearchBar(), BorderLayout.NORTH);

        buildAdvancedFiltersPanel();
        contentWrapper.add(advancedFiltersPanel, BorderLayout.WEST);

        // Core interactive viewport split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createScrollableCardsPanel(), createMapPanel());
        splitPane.setDividerLocation(540);
        splitPane.setBorder(null);
        contentWrapper.add(splitPane, BorderLayout.CENTER);

        mainLayout.add(contentWrapper, BorderLayout.CENTER);
        add(mainLayout);
    }

    // --- Component Generation Modules ---

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PINK_HEADER);
        topBar.setPreferredSize(new Dimension(getWidth(), 65));
        topBar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel menuBtn = createActionLabel("≡", 32);
        JLabel logoLabel = new JLabel("petbnb Search Engine", SwingConstants.CENTER);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel profileIcon = createActionLabel("👤", 24);

        topBar.add(menuBtn, BorderLayout.WEST);
        topBar.add(logoLabel, BorderLayout.CENTER);
        topBar.add(profileIcon, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createSearchBar() {
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, FIELD_GRAY));

        txtLocation = new JTextField("Athens, GR");
        styleInputField(txtLocation, 140);

        JTextField txtDates = new JTextField("28/05 - 04/06");
        styleInputField(txtDates, 120);

        cbPets = new JComboBox<>(new String[]{"Simba (Cat)", "Sherlock (Dog)", "All Pets"});
        cbPets.setPreferredSize(new Dimension(130, 35));
        cbPets.setFont(new Font("SansSerif", Font.BOLD, 12));

        RoundedButton btnSearch = new RoundedButton("Αναζήτηση 🔍", Color.WHITE, BUTTON_PINK);
        btnSearch.addActionListener(e -> triggerSearch());

        RoundedButton btnToggleFilters = new RoundedButton("Φίλτρα 🎛", Color.WHITE, new Color(150, 150, 170));
        btnToggleFilters.addActionListener(e -> advancedFiltersPanel.setVisible(!advancedFiltersPanel.isVisible()));

        searchBar.add(new JLabel("Πού:"));   searchBar.add(txtLocation);
        searchBar.add(new JLabel("Πότε:"));  searchBar.add(txtDates);
        searchBar.add(new JLabel("Ζώο:"));   searchBar.add(cbPets);
        searchBar.add(btnSearch);
        searchBar.add(btnToggleFilters);

        return searchBar;
    }

    private void buildAdvancedFiltersPanel() {
        advancedFiltersPanel = new JPanel();
        advancedFiltersPanel.setLayout(new BoxLayout(advancedFiltersPanel, BoxLayout.Y_AXIS));
        advancedFiltersPanel.setPreferredSize(new Dimension(250, getHeight()));
        advancedFiltersPanel.setBackground(Color.WHITE);
        advancedFiltersPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, FIELD_GRAY),
                new EmptyBorder(20, 15, 20, 15)
        ));
        advancedFiltersPanel.setVisible(false);

        JLabel lblTitle = new JLabel("Προχωρημένα Φίλτρα");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(PINK_HEADER);

        JLabel lblPrice = new JLabel("Μέγιστη Τιμή: 50€ / βράδυ");
        lblPrice.setFont(new Font("SansSerif", Font.PLAIN, 12));
        priceSlider = new JSlider(10, 100, 50);
        priceSlider.setBackground(Color.WHITE);
        priceSlider.addChangeListener(e -> lblPrice.setText("Μέγιστη Τιμή: " + priceSlider.getValue() + "€ / βράδυ"));

        chkNoPets = createStyledCheckbox("Χωρίς άλλα ζώα στον χώρο");
        chkGarden = createStyledCheckbox("Μονοκατοικία με κήπο");
        chkMeds = createStyledCheckbox("Χορήγηση φαρμάκων");

        JLabel lblRating = new JLabel("Ελάχιστη Βαθμολογία:");
        cbRating = new JComboBox<>(new String[]{"Όλα", "4.5+ ★", "4.8+ ★"});

        RoundedButton btnApply = new RoundedButton("Εφαρμογή Φίλτρων", Color.WHITE, SUCCESS_GREEN);
        btnApply.addActionListener(e -> triggerSearch());

        RoundedButton btnSavePrefs = new RoundedButton("Αποθήκευση Προτιμήσεων", PINK_HEADER, Color.WHITE);
        btnSavePrefs.setBorder(BorderFactory.createLineBorder(PINK_HEADER, 1, true));
        btnSavePrefs.addActionListener(e -> JOptionPane.showMessageDialog(this, "Οι προτιμήσεις αποθηκεύτηκαν!"));

        // Layout Assembly
        advancedFiltersPanel.add(lblTitle); advancedFiltersPanel.add(Box.createVerticalStrut(15));
        advancedFiltersPanel.add(lblPrice); advancedFiltersPanel.add(priceSlider); advancedFiltersPanel.add(Box.createVerticalStrut(15));
        advancedFiltersPanel.add(chkNoPets); advancedFiltersPanel.add(chkGarden); advancedFiltersPanel.add(chkMeds); advancedFiltersPanel.add(Box.createVerticalStrut(15));
        advancedFiltersPanel.add(lblRating); advancedFiltersPanel.add(cbRating); advancedFiltersPanel.add(Box.createVerticalStrut(25));
        advancedFiltersPanel.add(btnApply); advancedFiltersPanel.add(Box.createVerticalStrut(10));
        advancedFiltersPanel.add(btnSavePrefs);
    }

    private JScrollPane createScrollableCardsPanel() {
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(BG_LIGHT);
        cardsContainer.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        return scrollPane;
    }

    private JPanel createMapPanel() {
        mapPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Map Canvas Backing
                g2.setColor(MAP_BLUE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Simulated Topography Vector Strokes
                g2.setColor(new Color(238, 238, 225));
                g2.setStroke(new BasicStroke(24, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(0, getHeight() / 4, getWidth(), getHeight() / 3);
                g2.drawLine(getWidth() / 3, 0, getWidth() / 2, getHeight());
                g2.drawLine(0, (int)(getHeight() * 0.7), getWidth(), (int)(getHeight() * 0.6));

                // Radius Zone Focal Calculation
                g2.setColor(new Color(0, 0, 0, 35));
                int radiusMetrics = radiusSlider.getValue() * 8;
                int cX = getWidth() / 2, cY = getHeight() / 2;

                g2.setClip(new Ellipse2D.Double(cX - radiusMetrics, cY - radiusMetrics, radiusMetrics * 2, radiusMetrics * 2));
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setClip(null);
                g2.dispose();
            }
        };

        // Absolute Positioning UI Overlay inside the Map Environment
        JPanel overlayControl = new JPanel(new BorderLayout());
        overlayControl.setOpaque(false);
        overlayControl.setBounds(20, 20, 240, 50);

        JLabel lblRadius = new JLabel("Ακτίνα: 5 χλμ", SwingConstants.CENTER);
        lblRadius.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRadius.setForeground(TEXT_DARK);

        radiusSlider = new JSlider(1, 15, 5);
        radiusSlider.setOpaque(false);
        radiusSlider.addChangeListener(e -> {
            lblRadius.setText("Ακτίνα: " + radiusSlider.getValue() + " χλμ");
            mapPanel.repaint();
            rearrangeMapPins(); // Dyno-placement recalculation
        });

        overlayControl.add(lblRadius, BorderLayout.NORTH);
        overlayControl.add(radiusSlider, BorderLayout.CENTER);
        mapPanel.add(overlayControl);

        return mapPanel;
    }

    // --- Business Logic & Data Process Flows ---

    private void triggerSearch() {
        String filterLoc = txtLocation.getText().trim().toLowerCase();
        int ceilingPrice = priceSlider.getValue();

        List<HostCardData> searchResults = allHosts.stream()
                .filter(host -> host.location.toLowerCase().contains(filterLoc))
                .filter(host -> host.price <= ceilingPrice)
                .filter(host -> !chkNoPets.isSelected() || host.noOtherPets)
                .filter(host -> !chkGarden.isSelected() || host.hasGarden)
                .filter(host -> !chkMeds.isSelected() || host.canGiveMeds)
                .filter(host -> {
                    if (cbRating.getSelectedIndex() == 1) return host.rating >= 4.5;
                    if (cbRating.getSelectedIndex() == 2) return host.rating >= 4.8;
                    return true;
                }).collect(Collectors.toList());

        populateCardsView(searchResults);
        rearrangeMapPins();
    }

    private void populateCardsView(List<HostCardData> structuralData) {
        cardsContainer.removeAll();
        if (structuralData.isEmpty()) {
            JLabel blankNotice = new JLabel("Δεν βρέθηκαν καταλύματα.", SwingConstants.CENTER);
            blankNotice.setFont(new Font("SansSerif", Font.ITALIC, 13));
            blankNotice.setBorder(new EmptyBorder(30, 0, 0, 0));
            cardsContainer.add(blankNotice);
        } else {
            for (HostCardData data : structuralData) {
                cardsContainer.add(createNewHostCard(data));
                cardsContainer.add(Box.createVerticalStrut(15));
            }
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private void rearrangeMapPins() {
        // Clear old tactical pins dynamically without breaking map overlays
        for (Component component : mapPanel.getComponents()) {
            if (component instanceof RoundedButton) mapPanel.remove(component);
        }

        // Programmatic dynamic distribution based on viewport calculations
        int[][] relativeMapAnchors = {{25, 20}, {55, 45}, {30, 70}};
        for (int i = 0; i < Math.min(allHosts.size(), relativeMapAnchors.length); i++) {
            HostCardData mapHost = allHosts.get(i);
            RoundedButton mapPinButton = new RoundedButton(mapHost.price + "€", Color.WHITE, PINK_HEADER);
            mapPinButton.setFont(new Font("SansSerif", Font.BOLD, 11));

            // Scaled adaptive geometry
            int pX = (mapPanel.getWidth() <= 0 ? 500 : mapPanel.getWidth()) * relativeMapAnchors[i][0] / 100;
            int pY = (mapPanel.getHeight() <= 0 ? 600 : mapPanel.getHeight()) * relativeMapAnchors[i][1] / 100;
            mapPinButton.setBounds(pX, pY, 60, 32);

            mapPinButton.addActionListener(e -> generateMiniPopup(mapHost, mapPinButton.getX(), mapPinButton.getY()));
            mapPanel.add(mapPinButton);
        }
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private void generateMiniPopup(HostCardData identity, int componentX, int componentY) {
        JPopupMenu contextualPopup = new JPopupMenu();
        contextualPopup.setBorder(BorderFactory.createLineBorder(FIELD_GRAY, 1, true));

        JPanel popupFrame = new JPanel(new BorderLayout(10, 10));
        popupFrame.setBackground(Color.WHITE);
        popupFrame.setPreferredSize(new Dimension(210, 85));
        popupFrame.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel infoText = new JLabel("<html><b>" + identity.name + "</b><br>⭐ " + identity.rating + "<br>" + identity.price + "€/νύχτα</html>");
        infoText.setFont(new Font("SansSerif", Font.PLAIN, 12));

        RoundedButton interactionBtn = new RoundedButton("Προφίλ", Color.WHITE, SUCCESS_GREEN);
        interactionBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        interactionBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Μετάβαση: " + identity.name));

        popupFrame.add(infoText, BorderLayout.CENTER);
        popupFrame.add(interactionBtn, BorderLayout.EAST);
        contextualPopup.add(popupFrame);
        contextualPopup.show(mapPanel, componentX, componentY - 90);
    }

    private JPanel createNewHostCard(HostCardData item) {
        JPanel hostCardDeck = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        hostCardDeck.setLayout(new BorderLayout(15, 0));
        hostCardDeck.setOpaque(false);
        hostCardDeck.setMaximumSize(new Dimension(500, 125));
        hostCardDeck.setPreferredSize(new Dimension(500, 125));
        hostCardDeck.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Graphic Vector Placeholder
        JPanel graphicAvatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_GRAY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        graphicAvatar.setPreferredSize(new Dimension(85, 85));
        graphicAvatar.setLayout(new BorderLayout());
        JLabel homeEmblem = new JLabel("🏡", SwingConstants.CENTER);
        homeEmblem.setFont(new Font("SansSerif", Font.PLAIN, 30));
        graphicAvatar.add(homeEmblem, BorderLayout.CENTER);
        hostCardDeck.add(graphicAvatar, BorderLayout.WEST);

        // Core Descriptor Meta Labels
        JPanel descriptions = new JPanel(new GridLayout(3, 1, 0, 3));
        descriptions.setOpaque(false);

        JLabel headerIdentity = new JLabel(item.name + " - " + item.type);
        headerIdentity.setFont(new Font("SansSerif", Font.BOLD, 15));
        JLabel contextualMeta = new JLabel("★ " + item.rating + "   •   " + item.location);
        contextualMeta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contextualMeta.setForeground(Color.GRAY);

        String inlineFlags = (item.hasGarden ? "✓ Κήπος " : "") + (item.noOtherPets ? "✓ Μόνο του " : "") + (item.canGiveMeds ? "✓ Φάρμακα" : "");
        JLabel utilityBadge = new JLabel(inlineFlags);
        utilityBadge.setFont(new Font("SansSerif", Font.ITALIC, 11));
        utilityBadge.setForeground(LABEL_PINK);

        descriptions.add(headerIdentity);
        descriptions.add(contextualMeta);
        descriptions.add(utilityBadge);
        hostCardDeck.add(descriptions, BorderLayout.CENTER);

        // Right Pricing Module
        JPanel contextualActionBlock = new JPanel(new GridLayout(2, 1, 0, 6));
        contextualActionBlock.setOpaque(false);

        JLabel costLabel = new JLabel(item.price + "€ / νύχτα", SwingConstants.RIGHT);
        costLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        costLabel.setForeground(PINK_HEADER);

        RoundedButton dynamicBookingBtn = new RoundedButton("Αίτημα 🐾", Color.WHITE, SUCCESS_GREEN);
        dynamicBookingBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        dynamicBookingBtn.addActionListener(e -> performBookingSubmission(item));

        contextualActionBlock.add(costLabel);
        contextualActionBlock.add(dynamicBookingBtn);
        hostCardDeck.add(contextualActionBlock, BorderLayout.EAST);

        return hostCardDeck;
    }

    private void performBookingSubmission(HostCardData targetHost) {
        int validationResponse = JOptionPane.showConfirmDialog(this,
                "Θέλετε να στείλετε Αίτημα Κράτησης στον Host: " + targetHost.name + ";",
                "Μετασυνθήκη - petbnb", JOptionPane.YES_NO_OPTION);
        if (validationResponse == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Το αίτημα στάλθηκε επιτυχώς!");
        }
    }

    // --- Helper Utilities & UI Stylers ---

    private void styleInputField(JTextField entryField, int graphicWidth) {
        entryField.setPreferredSize(new Dimension(graphicWidth, 35));
        entryField.setFont(new Font("SansSerif", Font.BOLD, 13));
        entryField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_GRAY, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
    }

    private JCheckBox createStyledCheckbox(String visualTitle) {
        JCheckBox baseCheck = new JCheckBox(visualTitle);
        baseCheck.setBackground(Color.WHITE);
        baseCheck.setFont(new Font("SansSerif", Font.PLAIN, 12));
        baseCheck.setFocusPainted(false);
        return baseCheck;
    }

    private JLabel createActionLabel(String content, int symbolSize) {
        JLabel activeLabel = new JLabel(content);
        activeLabel.setForeground(Color.WHITE);
        activeLabel.setFont(new Font("SansSerif", Font.PLAIN, symbolSize));
        activeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return activeLabel;
    }

    private void initMockData() {
        allHosts.add(new HostCardData("Μαρία Κ.", "Apartment", "Athens, GR", 32, 4.9, true, false, true));
        allHosts.add(new HostCardData("Γιάννης Π.", "House", "Athens, GR", 45, 4.7, true, true, false));
        allHosts.add(new HostCardData("Ελένη Φ.", "Apartment", "Athens, GR", 25, 4.4, false, false, true));
    }

    // Structures
    private static class HostCardData {
        String name, type, location;
        int price;
        double rating;
        boolean noOtherPets, hasGarden, canGiveMeds;

        public HostCardData(String name, String type, String location, int price, double rating, boolean noOtherPets, boolean hasGarden, boolean canGiveMeds) {
            this.name = name; this.type = type; this.location = location;
            this.price = price; this.rating = rating;
            this.noOtherPets = noOtherPets; this.hasGarden = hasGarden; this.canGiveMeds = canGiveMeds;
        }
    }

    // --- Reusable Modern Graphic Custom Component ---
    static class RoundedButton extends JButton {
        private final Color backgroundFill;

        public RoundedButton(String label, Color tintText, Color backgroundFill) {
            super(label);
            this.backgroundFill = backgroundFill;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(tintText);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(130, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundFill);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetbnbSearchEngine().setVisible(true));
    }
}