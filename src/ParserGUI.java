import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParserGUI {
    private LL1Parser ll1Parser;
    private RDPParser rdpParser;
    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea resultArea;
    private JComboBox<String> parserSelector;
    private JLabel statusLabel;

    public ParserGUI() {
        ll1Parser = new LL1Parser();
        rdpParser = new RDPParser();
        frame = new JFrame("Parser GUI");
        inputArea = new JTextArea(5, 20);
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        parserSelector = new JComboBox<>(new String[]{"LL(1) Parser", "RDP Parser"});
        statusLabel = new JLabel("Ready");

        JButton parseButton = new JButton("Parse");
        parseButton.setToolTipText("Parse the input using the selected parser");
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputArea.getText().trim();
                boolean result = false;
                String selectedParser = (String) parserSelector.getSelectedItem();
                if ("LL(1) Parser".equals(selectedParser)) {
                    result = ll1Parser.parse(input);
                } else if ("RDP Parser".equals(selectedParser)) {
                    result = rdpParser.parse(input);
                }
                resultArea.setText("Result: " + (result ? "Accepted" : "Rejected"));
                statusLabel.setText("Parsing completed");
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clear the input and result areas");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
                resultArea.setText("");
                statusLabel.setText("Cleared");
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new TitledBorder("Input"));
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new TitledBorder("Controls"));
        controlPanel.add(parserSelector);
        controlPanel.add(parseButton);
        controlPanel.add(clearButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("Result"));
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new TitledBorder("Status"));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        frame.add(inputPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        frame.add(controlPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.5;
        frame.add(resultPanel, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0.1;
        frame.add(statusPanel, gbc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500); // Set preferred size
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ParserGUI();
            }
        });
    }
}