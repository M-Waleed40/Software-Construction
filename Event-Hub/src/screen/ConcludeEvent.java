import javax.swing.*;
import java.awt.*;
import javax.swing.border.AbstractBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ConcludeEvent extends JFrame {

    private JPanel uploadPanel; // Panel for multiple image previews
    private JLabel placeholderLabel; // Label to show "Upload Photo" text
    private boolean hasUploaded = false; // Track if placeholder should be removed
    private JTextArea newsArea; // Text area for news input

    public ConcludeEvent() {
        setTitle("Event Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Event Name Label
        JLabel eventLabel = new JLabel("Event Name: Innovating the Future");
        eventLabel.setFont(new Font("Arial", Font.BOLD, 24));
        eventLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(eventLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        // 2. Upload Image Panel
        JPanel uploadCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
            }
        };
        uploadCard.setPreferredSize(new Dimension(400, 200));
        uploadCard.setMaximumSize(new Dimension(400, 200));
        uploadCard.setBackground(new Color(255, 230, 235));
        uploadCard.setLayout(new BorderLayout());
        uploadCard.setBorder(new RoundedBorder(15, Color.PINK));

        uploadPanel = new JPanel();
        uploadPanel.setOpaque(false);
        uploadPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Placeholder label
        placeholderLabel = new JLabel("Upload Photo");
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        placeholderLabel.setForeground(Color.GRAY);
        uploadPanel.add(placeholderLabel);

        uploadCard.add(uploadPanel, BorderLayout.CENTER);
        uploadCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(uploadCard);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 80)));

        // Enable image upload on click
        uploadCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openFileChooser();
            }
        });

        // 3. Add News Section
        JLabel newsLabel = new JLabel("Add News:");
        newsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        newsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(newsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        newsArea = new JTextArea(3, 30); // Moved to class level
        newsArea.setLineWrap(true);
        newsArea.setWrapStyleWord(true);
        newsArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(newsArea);
        scrollPane.setMaximumSize(new Dimension(400, 80));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // 4. Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(100, 180, 255));
        submitButton.setFocusPainted(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(e -> {
            // Validation check
            if (!hasUploaded || newsArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Upload photos and add news!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show success dialog
            JOptionPane.showMessageDialog(this, "Photos and News have been submitted!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear uploaded images
            uploadPanel.removeAll();
            uploadPanel.add(placeholderLabel); // Reset placeholder
            hasUploaded = false;

            // Clear news area
            newsArea.setText("");

            // Refresh UI
            uploadPanel.revalidate();
            uploadPanel.repaint();
        });

        mainPanel.add(submitButton);

        add(mainPanel);
        setVisible(true);
    }

    // File chooser to select multiple image files
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image(s)");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(true);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            if (selectedFiles.length > 0) {
                displayImages(selectedFiles);
            }
        }
    }

    // Append images to the upload panel
    private void displayImages(File[] imageFiles) {
        if (!hasUploaded) {
            uploadPanel.remove(placeholderLabel); // remove only once
            hasUploaded = true;
        }

        for (File file : imageFiles) {
            try {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(120, 80, Image.SCALE_SMOOTH));
                    JLabel imageLabel = new JLabel(icon);
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    uploadPanel.add(imageLabel);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        uploadPanel.revalidate();
        uploadPanel.repaint();
    }

    // Custom border class
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius;
            return insets;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConcludeEvent::new);
    }
}
