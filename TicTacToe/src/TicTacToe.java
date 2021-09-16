import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe implements ActionListener {

    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[] buttons = new JButton[9];
    boolean player1Turn;
    int columns = 3; // expandability????

    public TicTacToe() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);

        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            buttons[i].setEnabled(false); // Fix funky behavior with clicking before title screen done
        }

        title_panel.add(textfield);
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);

        init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // yo fun fact e.getSource just literally gives you the button

        // we have to cast to JButton constantly because we have to keep assuring the compiler we are getting JButton because e.getSource() returns Object
        if (!((JButton) e.getSource()).getText().equals("")) return; // fancy way to check if empty
        if (player1Turn) {
            ((JButton) e.getSource()).setForeground(new Color(255, 0, 0));
            ((JButton) e.getSource()).setText("X");
            textfield.setText("O turn");
        } else {
            ((JButton) e.getSource()).setForeground(new Color(0, 0, 255));
            ((JButton) e.getSource()).setText("O");
            textfield.setText("X turn");
        }
        checkForWin();
        player1Turn = !player1Turn;
    }

    private void init() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        for (JButton i : buttons) i.setEnabled(true);

        player1Turn = random.nextInt(2) == 0 ? true : false;
        if (player1Turn) textfield.setText("X turn");
        else textfield.setText("O turn");
    }

    // "Wow this is so complex ill take hardcoding thanks"
    // well this maybe could scale in a square
    private void checkForWin() {
        int moduloNum = buttons.length / columns;
        for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
            String playerString = playerIndex == 0 ? "X" : "O"; // if this condition ? do this : else this
            int[] colHits = new int[moduloNum]; // this is most likely not the best way but eh
            for (int i = 0; i < colHits.length; i++) colHits[i] = 0;
            int rowProg = 0;
            for (int index = 0; index < buttons.length; index++) {
                if (index % moduloNum == 0) rowProg = 0;
                if (buttons[index].getText().equals(playerString)) { // do string comparisons with .equals()
                    colHits[index % moduloNum]++; // bro how tf did i think of this
                    rowProg++;
                }
                for (int i = 0; i < colHits.length; i++)
                    if (colHits[i] >= moduloNum) updateAfterWin(playerIndex, WinType.COL, i);
                if (rowProg >= moduloNum) updateAfterWin(playerIndex, WinType.ROW, index - (moduloNum - 1));
            }
            boolean winning = true;
            for (int index = 0; index < buttons.length; index += columns + 1) {
                winning = winning && buttons[index].getText().equals(playerString);
            }
            if (winning) updateAfterWin(playerIndex, WinType.DIA, 0);

            winning = true;
            for (int index = columns - 1; index < buttons.length - 1; index += columns - 1) {
                System.out.println(index);

                winning = winning && buttons[index].getText().equals(playerString);
            }
            if (winning) updateAfterWin(playerIndex, WinType.DIA, columns - 1);
        }
    }

    private void updateAfterWin(int playerIndex, WinType type, int windex) {
        for (JButton i : buttons) i.setEnabled(false);

        System.out.println(type.getName());

        switch (type) {
            case COL -> {
                for (int i = windex; i < buttons.length; i += buttons.length / columns)
                    buttons[i].setBackground(Color.GREEN);
            }
            case DIA -> {
                if (windex == 0) {
                    for (int i = 0; i < buttons.length; i += columns + 1) {
                        buttons[i].setBackground(Color.GREEN);
                    }
                } else {
                    for (int i = columns - 1; i < buttons.length - 1; i += columns - 1) {
                        buttons[i].setBackground(Color.GREEN);
                    }
                }
            }
            case ROW -> {
                for (int i = windex; i < windex + buttons.length / columns; i++)
                    buttons[i].setBackground(Color.GREEN);
            }
        }

        textfield.setText(playerIndex == 0 ? "X" : "O" + " wins!");
    }

    private enum WinType {
        ROW("row"),
        COL("col"),
        DIA("dia");

        String name;

        WinType(String dia) {
            this.name = dia;
        }

        public String getName() {
            return name;
        }
    }
}
