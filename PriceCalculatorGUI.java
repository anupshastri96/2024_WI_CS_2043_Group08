import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PriceCalculatorGUI extends JFrame {
    private double totalPrice = 0.0;
    private JLabel priceLabel;

    public PriceCalculatorGUI() {
        setTitle("Price Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        // Create labels
        JLabel totalLabel = new JLabel("Total Price:");
        priceLabel = new JLabel("$" + totalPrice);

        // Set font for totalLabel
        Font totalFont = totalLabel.getFont();
        totalLabel.setFont(new Font(totalFont.getName(), Font.BOLD, totalFont.getSize()));

        // Create buttons
        JButton childButton = new JButton("Child (0 - 12)");
        JButton adultButton = new JButton("Adult (12 - 65)");
        JButton seniorButton = new JButton("Senior (65+)");

        // Add action listeners to buttons
        childButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalPrice = 5.0;
                updatePriceLabel();
            }
        });

        adultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalPrice = 10.0;
                updatePriceLabel();
            }
        });

        seniorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalPrice = 15.0;
                updatePriceLabel();
            }
        });

        // Add labels and buttons to the frame
        setLayout(new GridLayout(5, 1)); // 5 rows, 1 column
        add(totalLabel);
        add(priceLabel);
        add(childButton);
        add(adultButton);
        add(seniorButton);
    }

    private void updatePriceLabel() {
        priceLabel.setText("$" + String.format("%.2f", totalPrice));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PriceCalculatorGUI().setVisible(true);
            }
        });
    }
}
