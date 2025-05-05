import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementApp extends JFrame {
    // Model class
    static class Student {
        private String id;
        private String name;
        private String grade;

        public Student(String id, String name, String grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getGrade() { return grade; }
    }

    // Components
    private JTextField idField, nameField, gradeField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private List<Student> students;

    public StudentManagementApp() {
        // Initialize data
        students = new ArrayList<>();
        
        // Set up frame
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icon.png").getImage()); // Add an icon (ensure icon.png exists in project root)

        // Create main panel with BorderLayout and gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(230, 240, 250), 0, getHeight(), new Color(209, 232, 226));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel (GridBagLayout for better control)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false); // Transparent background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input fields with modern styling
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idField = new JTextField(10);
        styleTextField(idField);
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField = new JTextField(20);
        styleTextField(nameField);
        
        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeField = new JTextField(5);
        styleTextField(gradeField);

        // Add components to input panel
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(gradeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(gradeField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        
        // Style all buttons with the same gray color
        styleButton(addButton, new Color(108, 117, 125)); // Gray
        styleButton(updateButton, new Color(108, 117, 125)); // Gray
        styleButton(deleteButton, new Color(108, 117, 125)); // Gray
        styleButton(clearButton, new Color(108, 117, 125)); // Gray
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table setup
        String[] columns = {"ID", "Name", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : new Color(230, 230, 235));
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(25);
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setShowGrid(true);

        // Style table header
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(100, 100, 100));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        // Add panels to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Action Listeners
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());

        // Table row selection
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                gradeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            }
        });

        // Keyboard shortcut (Ctrl+S to add)
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "addStudent");
        getRootPane().getActionMap().put("addStudent", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
    }

    // Style text fields
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setBackground(new Color(255, 255, 255));
        textField.setForeground(new Color(33, 33, 33));
    }

    // Style buttons
    private void styleButton(JButton button, Color baseColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String grade = gradeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(id, name, grade);
        students.add(student);
        tableModel.addRow(new Object[]{id, name, grade});
        clearFields();
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String grade = gradeField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            students.set(selectedRow, new Student(id, name, grade));
            tableModel.setValueAt(id, selectedRow, 0);
            tableModel.setValueAt(name, selectedRow, 1);
            tableModel.setValueAt(grade, selectedRow, 2);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            students.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        gradeField.setText("");
        studentTable.clearSelection();
    }

    public static void main(String[] args) {
        // Enable anti-aliasing for smoother rendering
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> {
            new StudentManagementApp().setVisible(true);
        });
    }
}