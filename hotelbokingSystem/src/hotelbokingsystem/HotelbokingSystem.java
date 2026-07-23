/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelbokingsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Data Structures & Algorithms Concepts Used:
// 1. Hash Table (HashMap) - For storing rooms and bookings
// 2. TreeMap - For sorted room display
// 3. Queue - For booking waitlist
// 4. Stack - For navigation history
// 5. Linked List - For managing multiple bookings
// 6. Sorting Algorithms - For room sorting
// 7. Searching Algorithms - For room search

// Room class representing a hotel room
class Room {
    private int roomNumber;
    private String type;
    private double price;
    private boolean isAvailable;
    private List<String> amenities;
    
    public Room(int roomNumber, String type, double price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
        this.amenities = new LinkedList<>();
    }
    
    public void addAmenity(String amenity) {
        amenities.add(amenity);
    }
    
    // Getters and setters
    public int getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public List<String> getAmenities() { return amenities; }
    
    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - $" + price + 
               " - " + (isAvailable ? "Available" : "Booked");
    }
}

// Booking class representing a hotel booking
class Booking {
    private int bookingId;
    private String guestName;
    private int roomNumber;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalPrice;
    
    public Booking(int bookingId, String guestName, int roomNumber, 
                   Date checkInDate, Date checkOutDate, double totalPrice) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
    }
    
    // Getters
    public int getBookingId() { return bookingId; }
    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
    
    @Override
    public String toString() {
        return "Booking #" + bookingId + ": " + guestName + " - Room " + roomNumber;
    }
}

// Main Hotel Management System Class
public class HotelbokingSystem extends JFrame {
    // Data Structures
    private HashMap<Integer, Room> rooms; // Hash Table for O(1) room access
    private TreeMap<Integer, Room> sortedRooms; // Sorted rooms by number
    private HashMap<Integer, Booking> bookings; // Hash Table for bookings
    private Queue<Booking> waitlist; // Queue for booking waitlist
    private Stack<String> navigationHistory; // Stack for navigation
    private LinkedList<Booking> bookingHistory; // Linked List for booking history
    
    // GUI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable roomsTable, bookingsTable;
    private DefaultTableModel roomsTableModel, bookingsTableModel;
    private JTextField searchField, guestNameField, roomNumberField;
    private JComboBox<String> roomTypeFilter;
    private JButton backButton;
    private Color primaryColor = new Color(0, 102, 204); // Blue
    private Color secondaryColor = new Color(240, 248, 255); // Light blue
    private Color accentColor = new Color(255, 140, 0); // Orange
    
    // System variables
    private int nextBookingId = 1;
    
    public HotelbokingSystem() {
        // Initialize data structures
        rooms = new HashMap<>();
        sortedRooms = new TreeMap<>();
        bookings = new HashMap<>();
        waitlist = new LinkedList<>();
        navigationHistory = new Stack<>();
        bookingHistory = new LinkedList<>();
        
        // Initialize sample data
        initializeSampleData();
        
        // Set up the main window
        setTitle("Hotel Booking Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set up the main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(secondaryColor);
        
        // Create and add different panels (screens)
        mainPanel.add(createDashboardPanel(), "Dashboard");
        mainPanel.add(createRoomsPanel(), "Rooms");
        mainPanel.add(createBookingsPanel(), "Bookings");
        mainPanel.add(createBookingFormPanel(), "BookingForm");
        mainPanel.add(createSearchPanel(), "Search");
        
        // Add main panel to frame
        add(mainPanel);
        
        // Show dashboard initially
        showScreen("Dashboard");
        
        // Make the window visible
        setVisible(true);
    }
    
    // Initialize sample data for demonstration
    private void initializeSampleData() {
        // Create sample rooms
        Room room101 = new Room(101, "Standard", 100.0, true);
        room101.addAmenity("WiFi");
        room101.addAmenity("TV");
        rooms.put(101, room101);
        sortedRooms.put(101, room101);
        
        Room room102 = new Room(102, "Deluxe", 150.0, true);
        room102.addAmenity("WiFi");
        room102.addAmenity("TV");
        room102.addAmenity("Mini Bar");
        rooms.put(102, room102);
        sortedRooms.put(102, room102);
        
        Room room201 = new Room(201, "Suite", 250.0, false);
        room201.addAmenity("WiFi");
        room201.addAmenity("TV");
        room201.addAmenity("Mini Bar");
        room201.addAmenity("Jacuzzi");
        rooms.put(201, room201);
        sortedRooms.put(201, room201);
        
        Room room202 = new Room(202, "Standard", 100.0, true);
        room202.addAmenity("WiFi");
        room202.addAmenity("TV");
        rooms.put(202, room202);
        sortedRooms.put(202, room202);
        
        Room room301 = new Room(301, "Deluxe", 150.0, true);
        room301.addAmenity("WiFi");
        room301.addAmenity("TV");
        room301.addAmenity("Mini Bar");
        rooms.put(301, room301);
        sortedRooms.put(301, room301);
        
        // Create sample bookings
        Booking booking1 = new Booking(nextBookingId++, "John Smith", 201, 
                                       new Date(), new Date(System.currentTimeMillis() + 86400000), 500.0);
        bookings.put(booking1.getBookingId(), booking1);
        bookingHistory.add(booking1);
        
        // Add to waitlist
        waitlist.add(new Booking(nextBookingId++, "Emma Johnson", 0, 
                                new Date(), new Date(System.currentTimeMillis() + 172800000), 300.0));
    }
    
    // Create the dashboard panel
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Hotel Booking System Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel with statistics and navigation
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(secondaryColor);
        
        // Statistics cards
        centerPanel.add(createStatCard("Total Rooms", String.valueOf(rooms.size()), primaryColor));
        centerPanel.add(createStatCard("Available Rooms", 
                         String.valueOf(countAvailableRooms()), new Color(0, 153, 76)));
        centerPanel.add(createStatCard("Total Bookings", 
                         String.valueOf(bookings.size()), accentColor));
        centerPanel.add(createStatCard("Waitlist", 
                         String.valueOf(waitlist.size()), new Color(204, 0, 0)));
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Navigation buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        String[] buttonLabels = {"Manage Rooms", "View Bookings", "New Booking", "Search Rooms"};
        String[] screenNames = {"Rooms", "Bookings", "BookingForm", "Search"};
        
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createStyledButton(buttonLabels[i]);
            final String screen = screenNames[i];
            button.addActionListener(e -> showScreen(screen));
            buttonPanel.add(button);
        }
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create a statistics card
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    // Create the rooms management panel
    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        
        backButton = createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Create rooms table
        String[] columns = {"Room No", "Type", "Price", "Status", "Amenities"};
        roomsTableModel = new DefaultTableModel(columns, 0);
        roomsTable = new JTable(roomsTableModel);
        roomsTable.setRowHeight(30);
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Populate table with rooms data
        updateRoomsTable();
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(secondaryColor);
        
        JButton sortByPriceButton = createStyledButton("Sort by Price");
        sortByPriceButton.addActionListener(e -> sortRoomsByPrice());
        
        JButton sortByRoomNumberButton = createStyledButton("Sort by Room No");
        sortByRoomNumberButton.addActionListener(e -> sortRoomsByNumber());
        
        JButton toggleStatusButton = createStyledButton("Toggle Availability");
        toggleStatusButton.addActionListener(e -> toggleRoomStatus());
        
        controlPanel.add(sortByPriceButton);
        controlPanel.add(sortByRoomNumberButton);
        controlPanel.add(toggleStatusButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create the bookings panel
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        
        backButton = createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Booking Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Create bookings table
        String[] columns = {"Booking ID", "Guest Name", "Room No", "Check-in", "Check-out", "Total Price"};
        bookingsTableModel = new DefaultTableModel(columns, 0);
        bookingsTable = new JTable(bookingsTableModel);
        bookingsTable.setRowHeight(30);
        bookingsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Populate table with bookings data
        updateBookingsTable();
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(secondaryColor);
        
        JButton cancelBookingButton = createStyledButton("Cancel Booking");
        cancelBookingButton.addActionListener(e -> cancelBooking());
        
        JButton viewWaitlistButton = createStyledButton("View Waitlist");
        viewWaitlistButton.addActionListener(e -> viewWaitlist());
        
        controlPanel.add(cancelBookingButton);
        controlPanel.add(viewWaitlistButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create the booking form panel
    private JPanel createBookingFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        
        backButton = createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("New Booking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBackground(secondaryColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        // Guest name field
        formPanel.add(createFormLabel("Guest Name:"));
        guestNameField = new JTextField();
        formPanel.add(guestNameField);
        
        // Room number field
        formPanel.add(createFormLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);
        
        // Check-in date
        formPanel.add(createFormLabel("Check-in Date (DD/MM/YYYY):"));
        JTextField checkInField = new JTextField();
        formPanel.add(checkInField);
        
        // Check-out date
        formPanel.add(createFormLabel("Check-out Date (DD/MM/YYYY):"));
        JTextField checkOutField = new JTextField();
        formPanel.add(checkOutField);
        
        // Available rooms list
        formPanel.add(createFormLabel("Available Rooms:"));
        JTextArea availableRoomsArea = new JTextArea(getAvailableRoomsText());
        availableRoomsArea.setEditable(false);
        availableRoomsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(availableRoomsArea);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        formPanel.add(scrollPane);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Submit button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(secondaryColor);
        JButton submitButton = createStyledButton("Submit Booking");
        submitButton.addActionListener(e -> {
            String guestName = guestNameField.getText();
            String roomNumberText = roomNumberField.getText();
            
            if (guestName.isEmpty() || roomNumberText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int roomNumber = Integer.parseInt(roomNumberText);
                if (bookRoom(guestName, roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Booking successful!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    guestNameField.setText("");
                    roomNumberField.setText("");
                    checkInField.setText("");
                    checkOutField.setText("");
                    availableRoomsArea.setText(getAvailableRoomsText());
                    updateRoomsTable();
                    updateBookingsTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Room is not available. Added to waitlist.", 
                        "Waitlist", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid room number", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(submitButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create the search panel
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        
        backButton = createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Search Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Search controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(secondaryColor);
        
        searchPanel.add(createFormLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        searchPanel.add(createFormLabel("Filter by Type:"));
        String[] roomTypes = {"All", "Standard", "Deluxe", "Suite"};
        roomTypeFilter = new JComboBox<>(roomTypes);
        searchPanel.add(roomTypeFilter);
        
        JButton searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);
        
        JButton resetButton = createStyledButton("Reset");
        resetButton.addActionListener(e -> {
            searchField.setText("");
            roomTypeFilter.setSelectedIndex(0);
            updateRoomsTable();
        });
        searchPanel.add(resetButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Results table
        String[] columns = {"Room No", "Type", "Price", "Status", "Amenities"};
        roomsTableModel = new DefaultTableModel(columns, 0);
        roomsTable = new JTable(roomsTableModel);
        roomsTable.setRowHeight(30);
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        updateRoomsTable();
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Helper method to create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }
    
    // Helper method to create a back button
    private JButton createBackButton() {
        JButton button = new JButton("← Back");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.addActionListener(e -> {
            if (!navigationHistory.isEmpty()) {
                String previousScreen = navigationHistory.pop();
                showScreen(previousScreen);
            } else {
                showScreen("Dashboard");
            }
        });
        return button;
    }
    
    // Helper method to create form labels
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }
    
    // Navigation method
    private void showScreen(String screenName) {
        // Save current screen to navigation history
        String currentScreen = getCurrentScreen();
        if (currentScreen != null && !currentScreen.equals(screenName)) {
            navigationHistory.push(currentScreen);
        }
        
        cardLayout.show(mainPanel, screenName);
    }
    
    // Get current screen name
    private String getCurrentScreen() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp.getName();
            }
        }
        return null;
    }
    
    // Update rooms table with current data
    private void updateRoomsTable() {
        roomsTableModel.setRowCount(0);
        
        // Use TreeMap for sorted iteration
        for (Room room : sortedRooms.values()) {
            String amenities = String.join(", ", room.getAmenities());
            roomsTableModel.addRow(new Object[]{
                room.getRoomNumber(),
                room.getType(),
                "$" + room.getPrice(),
                room.isAvailable() ? "Available" : "Booked",
                amenities
            });
        }
    }
    
    // Update bookings table with current data
    private void updateBookingsTable() {
        bookingsTableModel.setRowCount(0);
        
        for (Booking booking : bookings.values()) {
            bookingsTableModel.addRow(new Object[]{
                booking.getBookingId(),
                booking.getGuestName(),
                booking.getRoomNumber(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                "$" + booking.getTotalPrice()
            });
        }
    }
    
    // Count available rooms
    private int countAvailableRooms() {
        int count = 0;
        for (Room room : rooms.values()) {
            if (room.isAvailable()) {
                count++;
            }
        }
        return count;
    }
    
    // Get text representation of available rooms
    private String getAvailableRoomsText() {
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms.values()) {
            if (room.isAvailable()) {
                sb.append(room.toString()).append("\n");
            }
        }
        return sb.toString();
    }
    
    // Book a room
    private boolean bookRoom(String guestName, int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.isAvailable()) {
            // Create booking
            Booking booking = new Booking(nextBookingId++, guestName, roomNumber,
                                        new Date(), 
                                        new Date(System.currentTimeMillis() + 86400000),
                                        room.getPrice() * 2); // Assume 2 days
            
            bookings.put(booking.getBookingId(), booking);
            bookingHistory.add(booking);
            room.setAvailable(false);
            
            return true;
        } else {
            // Add to waitlist
            Booking waitlistBooking = new Booking(nextBookingId++, guestName, roomNumber,
                                                new Date(), 
                                                new Date(System.currentTimeMillis() + 86400000),
                                                0);
            waitlist.add(waitlistBooking);
            return false;
        }
    }
    
    // Cancel a booking
    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int bookingId = (int) bookingsTableModel.getValueAt(selectedRow, 0);
            Booking booking = bookings.get(bookingId);
            
            if (booking != null) {
                // Free up the room
                Room room = rooms.get(booking.getRoomNumber());
                if (room != null) {
                    room.setAvailable(true);
                }
                
                // Remove booking
                bookings.remove(bookingId);
                
                // Check waitlist
                if (!waitlist.isEmpty()) {
                    Booking waitlistBooking = waitlist.poll();
                    JOptionPane.showMessageDialog(this, 
                        "Room freed. Next in waitlist: " + waitlistBooking.getGuestName(),
                        "Waitlist", JOptionPane.INFORMATION_MESSAGE);
                }
                
                updateRoomsTable();
                updateBookingsTable();
                
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel",
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // View waitlist
    private void viewWaitlist() {
        StringBuilder sb = new StringBuilder("Current Waitlist:\n\n");
        int position = 1;
        
        // Using Queue iteration
        for (Booking booking : waitlist) {
            sb.append(position++).append(". ").append(booking.getGuestName())
              .append(" - Requested Room: ").append(booking.getRoomNumber()).append("\n");
        }
        
        if (waitlist.isEmpty()) {
            sb.append("Waitlist is empty");
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), 
                                    "Booking Waitlist", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Sort rooms by price (Bubble Sort algorithm)
    private void sortRoomsByPrice() {
        List<Room> roomList = new ArrayList<>(rooms.values());
        
        // Bubble Sort implementation
        for (int i = 0; i < roomList.size() - 1; i++) {
            for (int j = 0; j < roomList.size() - i - 1; j++) {
                if (roomList.get(j).getPrice() > roomList.get(j + 1).getPrice()) {
                    // Swap rooms
                    Room temp = roomList.get(j);
                    roomList.set(j, roomList.get(j + 1));
                    roomList.set(j + 1, temp);
                }
            }
        }
        
        // Update sorted rooms TreeMap
        sortedRooms.clear();
        for (Room room : roomList) {
            sortedRooms.put(room.getRoomNumber(), room);
        }
        
        updateRoomsTable();
        JOptionPane.showMessageDialog(this, "Rooms sorted by price (ascending)",
                                    "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Sort rooms by room number (already sorted by TreeMap)
    private void sortRoomsByNumber() {
        // TreeMap already maintains sorted order by key (room number)
        updateRoomsTable();
        JOptionPane.showMessageDialog(this, "Rooms sorted by room number",
                                    "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Toggle room availability status
    private void toggleRoomStatus() {
        int selectedRow = roomsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomNumber = (int) roomsTableModel.getValueAt(selectedRow, 0);
            Room room = rooms.get(roomNumber);
            
            if (room != null) {
                room.setAvailable(!room.isAvailable());
                updateRoomsTable();
                
                String status = room.isAvailable() ? "available" : "booked";
                JOptionPane.showMessageDialog(this, 
                    "Room " + roomNumber + " is now " + status,
                    "Status Updated", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room",
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Perform search based on criteria
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) roomTypeFilter.getSelectedItem();
        
        roomsTableModel.setRowCount(0);
        
        for (Room room : sortedRooms.values()) {
            boolean matchesSearch = searchText.isEmpty() ||
                                   String.valueOf(room.getRoomNumber()).contains(searchText) ||
                                   room.getType().toLowerCase().contains(searchText);
            
            boolean matchesType = "All".equals(selectedType) ||
                                 room.getType().equals(selectedType);
            
            if (matchesSearch && matchesType) {
                String amenities = String.join(", ", room.getAmenities());
                roomsTableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getType(),
                    "$" + room.getPrice(),
                    room.isAvailable() ? "Available" : "Booked",
                    amenities
                });
            }
        }
        
        if (roomsTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No rooms found matching the criteria",
                                        "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Main method to run the application
    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new HotelbokingSystem();
        });
    }
}