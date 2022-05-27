package //Contact https://assignmentoverflow.com;

import static edu.vt.cs5044.DABGuiName.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// Academic Version: Spring 2020

public class DABPanel extends JPanel {

	private final DotsAndBoxes game;

	private final JLabel p1ScoreLabel;
	private final JLabel p2ScoreLabel;
	private final JLabel turnLabel;
	private final JComboBox<Integer> xCombo;
	private final JComboBox<Integer> yCombo;
	private final JComboBox<Direction> dirCombo;
	private final JButton drawButton;
	private final DABGrid dabGrid;

	public DABPanel(JFrame frame) {

		// Adds a menu bar to the frame that will contain this panel:
		frame.setJMenuBar(setupMenuBar());

		// Creates a new DotAndBoxes instance that will act as the game engine:
		game = new DABGame();

		// Constructs and name each user interface component
		// (Each needs a unique name for testing purposes)
		xCombo = new JComboBox<>();
		xCombo.setName(X_COMBO);

		yCombo = new JComboBox<>();
		yCombo.setName(Y_COMBO);

		dirCombo = new JComboBox<>(Direction.values());
		dirCombo.setName(DIR_COMBO);

		drawButton = new JButton("Draw!");
		drawButton.setName(DRAW_BUTTON);
		// TODO: set the text to be displayed on the draw button
		// TODO: add handleDrawButton() as an action listener via method reference

		turnLabel = new JLabel();
		turnLabel.setName(TURN_LABEL);

		p1ScoreLabel = new JLabel();
		p1ScoreLabel.setName(P1_SCORE_LABEL);

		p2ScoreLabel = new JLabel();
		p2ScoreLabel.setName(P2_SCORE_LABEL);

		dabGrid = new DABGrid(game);
		dabGrid.setName(DAB_GRID);

		// Performs layout of all the user interface components:
		setupLayout();

		// Begins a new 3x3 game by default:
		startGame(3);

	}

	private void handleDrawButton(ActionEvent ae) {
		// TODO: the handler code for the draw button goes here
		// TODO: don't forget to call updateStatus(), but ONLY if the draw was
		// successful
		boolean drawn = game.drawEdge(new Coordinate(xCombo.getSelectedIndex(), yCombo.getSelectedIndex()),
				(Direction) dirCombo.getSelectedItem());
		if (drawn) {
			updateStatus();
		}
	}

	private void updateStatus() {
		// TODO: read the game status via accessors; set each label's text accordingly
		// TODO: don't forget to disable the draw button, if the game is over
		// TODO: be sure to call repaint() at the end of this method to render any
		// changes
		Map<Player, Integer> scores = game.getScores();
		p1ScoreLabel.setText("ONE: " + scores.get(Player.ONE));
		p2ScoreLabel.setText("TWO: " + scores.get(Player.TWO));
		
		
		if(game.getCurrentPlayer() == Player.ONE)
			turnLabel.setText("Player ONE Go!");
		else if(game.getCurrentPlayer() == Player.TWO)
			turnLabel.setText("Player TWO Go!");
		else {
			turnLabel.setText("Game Over!");
			drawButton.setEnabled(false);
		}


		repaint();
	}

	private void updateCombos() {
		// TODO: update the coordinate combo box options, based on the current size of
		// the grid
		xCombo.removeAllItems();
		yCombo.removeAllItems();
		for (int i = 0; i < game.getSize(); i++) {
			xCombo.addItem(i);
			yCombo.addItem(i);
		}
	}

	private void startGame(int size) {
		// TODO: start a new game of the specified size
		// TODO: call updateCombos() and updateStatus()
		// TODO: don't forget to enable the draw button
		game.init(size);
		drawButton.setEnabled(true);
		updateCombos();
		updateStatus();
	}

	private void setupLayout() {
		// TODO: layout this panel and all its components
		// TODO: The layout must reasonably handle resizing of the frame

		setLayout(new BorderLayout());

		JPanel pnlTop = new JPanel(new GridLayout(2, 1));
		turnLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		pnlTop.add(turnLabel);
		
		

		JPanel pnlDraw = new JPanel();
		pnlDraw.add(xCombo);
		pnlDraw.add(yCombo);
		pnlDraw.add(dirCombo);
		pnlDraw.add(drawButton);
		drawButton.addActionListener((e) -> handleDrawButton(e));
		pnlTop.add(pnlDraw);
		add(pnlTop, BorderLayout.NORTH);

		add(dabGrid);

		JPanel pnlBottom = new JPanel(new GridLayout(1, 2));
		pnlBottom.add(p1ScoreLabel);
		p2ScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlBottom.add(p2ScoreLabel);

		add(pnlBottom, BorderLayout.SOUTH);

	}

	private JMenuBar setupMenuBar() {
		// TODO: create a new JMenuBar and populate it with the required items
		// TODO: use lambda expressions so the new game items call startGame() when
		// clicked
		// TODO: use a lambda expression to handle activating/deactivating interactive
		// mode
		// (Note that a method reference must be nested within the lambda expression)
		JMenuBar menuBar = new JMenuBar();

		JMenu menuGame = new JMenu("Game");

		JMenu menuNew = new JMenu("New");

		JMenuItem miSize2x2 = new JMenuItem("Size 2x2");
		miSize2x2.addActionListener((e) -> {
			startGame(2);
		});
		menuNew.add(miSize2x2);

		JMenuItem miSize3x3 = new JMenuItem("Size 3x3");
		miSize3x3.addActionListener((e) -> {
			startGame(3);
		});
		menuNew.add(miSize3x3);

		JMenuItem miSize4x4 = new JMenuItem("Size 4x4");
		miSize4x4.addActionListener((e) -> {
			startGame(4);
		});
		menuNew.add(miSize4x4);

		menuGame.add(menuNew);

		JCheckBoxMenuItem miInteractiveGrid = new JCheckBoxMenuItem("Interactive grid");
		miInteractiveGrid.addActionListener((e) -> {
			if (miInteractiveGrid.isSelected())
				dabGrid.setCallback(() -> updateStatus());
			else
				dabGrid.setCallback(null);

		});
		menuGame.add(miInteractiveGrid);

		menuBar.add(menuGame);

		return menuBar;
	}

	private static void createAndShowGUI() {
		// This is boilerplate code; please leave this exactly as-is
		JFrame frame = new JFrame("Dots And Boxes");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JComponent newContentPane = new DABPanel(frame);
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// This is boilerplate code; please leave this exactly as-is
		// Notice the use of a method reference to simplify this code
		SwingUtilities.invokeLater(DABPanel::createAndShowGUI);
	}

}
