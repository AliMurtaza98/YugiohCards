package ventanas;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import daoImpl.ExistDBDAOImpl;
import daoImpl.MongoDBDAOImpl;
import interfaces.ICard;
import interfaces.IDeck;
import models.Card;
import models.Deck;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Ventana { // Clase de la ventana

	// Atributos de la clase
	private JFrame frame;
	private DefaultListModel<Card> Collection_listModel, Deck_listModel;
	private ArrayList<Card> arraylist_cards, arraylist_deck;
	private JButton btn_toCollection, btn_toDeck, btn_saveDeck, btn_loadCards, btn_randomDeck, btn_search;
	private int total_value;
	private JList<Card> list_collection, list_deck;
	private JLabel lbl_totalValue, lbl_Maximum, lbl_searchDeck;
	private JTextField tf_deckName;
	private String deck_name;
	private int deck_value;
	private boolean loaded_deck = false;

	// Main
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana window = new Ventana();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Ventana() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBounds(100, 100, 2000, 1100);

		// Cargamos la imagen de fondo
		ImageIcon icon = new ImageIcon("src/main/resources" + File.separator + "w2.jpg");
		final Image image = icon.getImage();
		JPanel contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		};
		contentPane.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Border emptyBorder = BorderFactory.createEmptyBorder();

		// Implementacion de interfaces
		ICard interface_card = new ExistDBDAOImpl();
		IDeck interface_deck = new MongoDBDAOImpl();

		// LIST MODEL COLLECTION
		Collection_listModel = new DefaultListModel<Card>();
		list_collection = new JList<Card>(Collection_listModel);
		list_collection.setSelectionForeground(new Color(255, 255, 255));

		// SCROLL PANE COLLECTION
		JScrollPane scrollPane_collection = new JScrollPane();
		scrollPane_collection.setBorder(emptyBorder);
		scrollPane_collection.setOpaque(false);
		scrollPane_collection.getViewport().setOpaque(false);
		scrollPane_collection.setViewportView(list_collection);

		// JLIST COLLECTION
		list_collection.setBackground(new Color(0, 0, 0, 0));
		list_collection.setOpaque(false);
		list_collection.setSelectionBackground(new Color(255, 153, 0));
		list_collection.setFont(new Font("Arial Black", Font.PLAIN, 12));
		list_collection.setForeground(Color.WHITE);

		// LIST MODEL DECK
		Deck_listModel = new DefaultListModel<Card>();
		list_deck = new JList<Card>(Deck_listModel);

		// SCROLL PANE DECK
		JScrollPane scrollPane_deck = new JScrollPane();
		scrollPane_deck.setBorder(emptyBorder);
		scrollPane_deck.setOpaque(false);
		scrollPane_deck.getViewport().setOpaque(false);
		scrollPane_deck.setOpaque(false);
		scrollPane_deck.setViewportView(list_deck);

		// JLIST DECK
		list_deck.setBackground(new Color(0, 0, 0, 0));
		list_deck.setSelectionBackground(new Color(255, 153, 0));
		list_deck.setOpaque(false);
		list_deck.setForeground(Color.WHITE);
		list_deck.setFont(new Font("Tahoma", Font.BOLD, 13));
		list_deck.setBackground(new Color(0, 0, 0, 0));

		// BOTONES

		// Boton Load Cards
		btn_loadCards = new JButton("Load Cards");
		btn_loadCards.setBackground(new Color(226, 132, 24));
		btn_loadCards.setForeground(new Color(0, 0, 0));
		btn_loadCards.setBorder(new BevelBorder(BevelBorder.RAISED));
		btn_loadCards.setFont(new Font("Tahoma", Font.BOLD, 18));
		btn_loadCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CARGAMOS LAS CARTAS EN JLIST LLAMANDO AL METODO ATRAVES DE SU INTERFAZ
				btn_loadCards.setEnabled(false);
				btn_search.setEnabled(true);
				arraylist_cards = interface_card.getAllCards();
				for (int i = 0; i < arraylist_cards.size(); i++) {
					Collection_listModel.add(i, arraylist_cards.get(i));
					btn_randomDeck.setEnabled(true);
				}

			}
		});

		// Boton RANDOM DECK
		btn_randomDeck = new JButton("Random Deck");
		btn_randomDeck.setBorder(new BevelBorder(BevelBorder.RAISED));
		btn_randomDeck.setBackground(new Color(226, 132, 24));
		btn_randomDeck.setForeground(new Color(0, 0, 0));
		btn_randomDeck.setFont(new Font("Tahoma", Font.BOLD, 18));
		btn_randomDeck.setEnabled(false);
		btn_randomDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int valor;
				int nuevovalor = 0;
				((DefaultListModel) list_deck.getModel()).clear();
				((DefaultListModel) list_collection.getModel()).clear();
				total_value = 0;
				boolean repetir = true;
				// HAGO UN RANDOM ENTRE 0 Y 1, SI ES 1 Y EL VALOR DE ESA CARTA NO SUPERA LA
				// SUMA, LA INTRODUCIMOS EN EL JLIST DE BARAJA, Y SI SUPERA EL LIMITE O EL
				// RANDOM ES 0, LA INTRODUCIMOS EN EL JLIST DE CARTAS
				while (repetir) {
					for (int i = 0; i < arraylist_cards.size(); i++) {
						int random = ((int) Math.round(Math.random()));
						if (random == 1) {
							valor = arraylist_cards.get(i).getValue();
							if (total_value + valor <= 20) {
								total_value = total_value + valor;
								if (!Deck_listModel.contains(arraylist_cards.get(i))) {
									Deck_listModel.addElement(arraylist_cards.get(i));
								
								}
								nuevovalor = nuevovalor + arraylist_cards.get(i).getValue();
								if (total_value == 20) {
									repetir=false;
									lbl_totalValue.setText("Total Value: " + total_value);
									lbl_Maximum.setText("[MAXIMUM]");

								} else {
									lbl_totalValue.setText("Total Value: " + total_value);
									lbl_Maximum.setText("");

								}
							} else {
								if (!Collection_listModel.contains(arraylist_cards.get(i))) {
									Collection_listModel.addElement(arraylist_cards.get(i));
								}

							}
						} else {

							if (!Collection_listModel.contains(arraylist_cards.get(i))) {
								Collection_listModel.addElement(arraylist_cards.get(i));
							}
						}
					}
				}
			}
		});

		// Boton -->
		btn_toDeck = new JButton();
		btn_toDeck.setBorderPainted(false);
		btn_toDeck.setContentAreaFilled(false);
		btn_toDeck.setFocusPainted(false);
		btn_toDeck.setOpaque(false);
		btn_toDeck.setIcon(new ImageIcon("src/main/resources" + File.separator + "right.png"));

		btn_toDeck.setBorder(new EmptyBorder(0, 0, 0, 0));
		btn_toDeck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// HAGO UN ARRAY PARA COGER TODAS LAS CARTAS SELECIONADAS, RECORRO EL ARRAY
				// DESDE LA ULTIMA POSICION PARA QUE NO DE PROBLEMAS SI QUEREMOS INTRODUCIR UNA
				// CARTA QUE ESTA EN LA POSICION DIFERENTE AL QUE ESTA ARRIBA. COGEMOS EL
				// ELEMENTO Y LO CONVIERTO EN UN STRING Y HACER UN SPLIT PARA COGER SU ULTIMO
				// VALOR Y ESTABLECER EL LIMITE DE 20 PUNTOS.
				String selected_element = null;
				String[] array_split;
				if (list_collection.getSelectedIndices().length > 0) {
					int[] selectedIndices = list_collection.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						selected_element = list_collection.getModel().getElementAt(selectedIndices[i]).toString();
						array_split = selected_element.split(" - ");
						int valor = Integer.parseInt(array_split[4]);
						if (total_value + valor <= 20) {
							total_value = total_value + valor;
							Deck_listModel
									.addElement((Card) list_collection.getModel().getElementAt(selectedIndices[i]));
							lbl_totalValue.setText("Total Value: " + total_value);
							if (total_value == 20) {
								lbl_Maximum.setText("[MAXIMUM]");
							}
							Collection_listModel.removeElementAt(selectedIndices[i]);

						}

					}

				}
			}
		});

		// Boton <--
		btn_toCollection = new JButton();
		btn_toCollection.setBorderPainted(false);
		btn_toCollection.setContentAreaFilled(false);
		btn_toCollection.setFocusPainted(false);
		btn_toCollection.setOpaque(false);

		btn_toCollection.setIcon(new ImageIcon("src/main/resources" + File.separator + "left.png"));
		btn_toCollection.setBorder(new EmptyBorder(0, 0, 0, 0));
		btn_toCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// HAGO UN ARRAY PARA COGER TODAS LAS CARTAS SELECIONADAS, RECORRO EL ARRAY
				// DESDE LA ULTIMA POSICION PARA QUE NO DE PROBLEMAS SI QUEREMOS INTRODUCIR UNA
				// CARTA QUE ESTA EN LA POSICION DIFERENTE AL QUE ESTA ARRIBA. COGEMOS EL
				// ELEMENTO Y LO CONVIERTO EN UN STRING Y HACER UN SPLIT PARA COGER SU ULTIMO
				// VALOR Y RESTAR LOS PUNTOS QUE TENIA LA CARTA QUE SE VUELVE A INTRODUCIR A LA
				// COLECCION DE CARTAS.
				String selected_element = null;
				String[] array_split;
				if (list_deck.getSelectedIndices().length > 0) {
					int[] selectedIndices = list_deck.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						Collection_listModel.addElement((Card) list_deck.getModel().getElementAt(selectedIndices[i]));
						selected_element = list_deck.getModel().getElementAt(selectedIndices[i]).toString();
						array_split = selected_element.split(" - ");
						int valor = Integer.parseInt(array_split[4]);
						total_value = total_value - valor;
						lbl_totalValue.setText("Total Value: " + total_value);
						if (total_value == 20) {
							lbl_Maximum.setText("[MAXIMUM]");
						} else {
							lbl_totalValue.setText("Total Value: " + total_value);
							lbl_Maximum.setText("");
						}
						Deck_listModel.removeElementAt(selectedIndices[i]);
					}

				}
			}
		});

		// Boton Save
		btn_saveDeck = new JButton("SAVE");
		btn_saveDeck.setFont(new Font("Segoe UI", Font.BOLD, 26));
		btn_saveDeck.setForeground(new Color(255, 255, 255));
		btn_saveDeck.setHorizontalAlignment(SwingConstants.RIGHT);
		btn_saveDeck.setIcon(new ImageIcon("src/main/resources" + File.separator + "save.png"));
		btn_saveDeck.setBorder(new EmptyBorder(0, 0, 0, 0));
		btn_saveDeck.setBorderPainted(false);
		btn_saveDeck.setContentAreaFilled(false);
		btn_saveDeck.setFocusPainted(false);
		btn_saveDeck.setOpaque(false);
		btn_saveDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// PEDIMOS EL NOMBRE PARA GUARDAR LA BARAJA, SI ES VACIO VUELVE A PEDIR, SI LO
				// CANCELA NO HACEMOS NADA. GUARDAMOS UN OBJETO DECK CON EL ARRAYLIST DE CARTAS,
				// EL NOMBRE Y SU VALOR TOTA EN EL METODO Y LLAMAMOS AL METODO PARA HACER
				// GUARDAR ESA BARAJA.

				boolean ask = true;
				if (total_value <= 20) {
					while (ask) {
						deck_name = JOptionPane.showInputDialog(frame, "Enter name of the new deck");
						if (deck_name != null) {
							if (!deck_name.isEmpty()) {
								ask = false;
								if (interface_deck.loadDeck(deck_name) == null) {
									arraylist_deck = new ArrayList<Card>();
									total_value = 0;
									for (int i = 0; i < Deck_listModel.size(); i++) {
										arraylist_deck.add(Deck_listModel.get(i));
										deck_value = deck_value + list_deck.getModel().getElementAt(i).getValue();
									}
									Deck deck = new Deck(deck_name, deck_value, arraylist_deck);
									interface_deck.saveDeck(deck);
									Deck_listModel.clear();
									Collection_listModel.clear();
									arraylist_cards = interface_card.getAllCards();
									for (int i = 0; i < arraylist_cards.size(); i++) {
										Collection_listModel.add(i, arraylist_cards.get(i));

									}
									JOptionPane.showMessageDialog(frame, "New Deck Has Been Saved Successfully.",
											"Data Saved", JOptionPane.INFORMATION_MESSAGE);
									deck_value = 0;
									total_value = 0;
									lbl_Maximum.setText("");
									lbl_totalValue.setText("Total Value: 0");
								} else if (loaded_deck == true) {
									int opc;
									opc = JOptionPane.showConfirmDialog(frame,
											"Are you sure you want to save the changes?", "Confirm Changes",
											JOptionPane.YES_NO_OPTION);
									if (opc == 0) {
										arraylist_deck = new ArrayList<Card>();
										total_value = 0;
										deck_value = 0;
										for (int i = 0; i < Deck_listModel.size(); i++) {
											arraylist_deck.add(Deck_listModel.get(i));
											deck_value = deck_value + list_deck.getModel().getElementAt(i).getValue();
										}
										Deck deck = new Deck(deck_name, deck_value, arraylist_deck);
										interface_deck.updateDeck(deck);
										Deck_listModel.clear();
										Collection_listModel.clear();
										loaded_deck=false;
										arraylist_cards = interface_card.getAllCards();
										for (int i = 0; i < arraylist_cards.size(); i++) {
											Collection_listModel.add(i, arraylist_cards.get(i));

										}
										total_value = 0;
										deck_value = 0;
										lbl_Maximum.setText("");
										lbl_totalValue.setText("Total Value: 0");

										JOptionPane.showMessageDialog(frame, "Changes Has Been Saved Successfully.",
												"Data Saved", JOptionPane.INFORMATION_MESSAGE);
									}

								} else if (!(interface_deck.loadDeck(deck_name) == null) && loaded_deck == false) {
									JOptionPane.showMessageDialog(frame, "ERROR ! Deck With This Name Already Exists.",
											"Error", JOptionPane.ERROR_MESSAGE);
								}
							}

						} else {
							ask = false;
						}
					}
				}
			}

		});

		// Boton Search
		btn_search = new JButton("Load");
		btn_search.setFont(new Font("Tahoma", Font.BOLD, 16));
		btn_search.setBorder(new BevelBorder(BevelBorder.RAISED));
		btn_search.setBackground(new Color(226, 132, 24));
		btn_search.setForeground(new Color(0, 0, 0));
		btn_search.setEnabled(false);

		btn_search.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// EL USUARIO INTRODUCE UN NOMBRE EN EL TEXTFIELD, SI SE ENCUENTRA LA BARAJA, LA
				// CARGAMOS EN EL JLIST DE BARAJA Y SI NO LA ENCUENTRA, SACA UN MENSAJE DE ERROR
				// DE QUE NO HAY ESA BARAJA.
				if (!(tf_deckName == null)) {
					Deck deck = interface_deck.loadDeck(tf_deckName.getText());
					Deck_listModel.removeAllElements();
					if (!(deck == null)) {
						loaded_deck = true;
						total_value = 0;
						arraylist_deck = deck.getArraylist_Cards();
						for (int i = 0; i < arraylist_deck.size(); i++) {
							Deck_listModel.addElement(arraylist_deck.get(i));
							total_value = total_value + arraylist_deck.get(i).getValue();
						}
						if (total_value == 20) {
							lbl_totalValue.setText("Total Value: " + String.valueOf(total_value));
							lbl_Maximum.setText("[MAXIMUM]");
						} else {
							lbl_totalValue.setText("Total Value: " + String.valueOf(total_value));
							lbl_Maximum.setText("");
						}
					} else {
						lbl_totalValue.setText("Total Value: 0");
						lbl_Maximum.setText("");
						JOptionPane.showMessageDialog(frame, "ERROR ! Deck Doesn't Exist");

					}
					tf_deckName.setText("");
				}
			}
		});

		JPanel panel_left = new JPanel();
		panel_left.setBackground(new Color(0, 0, 0, 0));

		// Label Valor Total
		lbl_totalValue = new JLabel("Total Value: 0");
		lbl_totalValue.setForeground(Color.white);
		lbl_totalValue.setFont(new Font("Segoe Print", Font.BOLD, 18));

		// Label Titulo Maximo
		lbl_Maximum = new JLabel("");
		lbl_Maximum.setForeground(Color.RED);
		lbl_Maximum.setFont(new Font("Segoe Print", Font.BOLD, 18));

		// TextField Buscar Deck
		tf_deckName = new JTextField();
		tf_deckName.setHorizontalAlignment(SwingConstants.CENTER);
		tf_deckName.setFont(new Font("Calisto MT", Font.BOLD, 20));
		tf_deckName.setColumns(10);
		// Si se presiona "ENTER" despues de introducir el nombre de la baraja, hace la
		// misma funcion que el boton de buscar.
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!(tf_deckName == null)) {
					Deck deck = interface_deck.loadDeck(tf_deckName.getText());
					Deck_listModel.removeAllElements();
					if (!(deck == null)) {
						loaded_deck = true;
						total_value = 0;
						arraylist_deck = deck.getArraylist_Cards();
						for (int i = 0; i < arraylist_deck.size(); i++) {
							Deck_listModel.addElement(arraylist_deck.get(i));
							total_value = total_value + arraylist_deck.get(i).getValue();
						}
						if (total_value == 20) {
							lbl_totalValue.setText("Total Value: " + String.valueOf(total_value));
							lbl_Maximum.setText("[MAXIMUM]");
						} else {
							lbl_totalValue.setText("Total Value: " + String.valueOf(total_value));
							lbl_Maximum.setText("");
						}
					} else {
						lbl_totalValue.setText("Total Value: 0");
						lbl_Maximum.setText("");
						JOptionPane.showMessageDialog(frame, "No existe esta baraja.");

					}
					tf_deckName.setText("");
				}
			}
		};
		tf_deckName.setAction(action);

		// Label Buscar Deck
		lbl_searchDeck = new JLabel("Search a Deck");
		lbl_searchDeck.setIcon(new ImageIcon("src/main/resources" + File.separator + "lupa.png"));
		lbl_searchDeck.setForeground(new Color(204, 153, 102));
		lbl_searchDeck.setFont(new Font("Gungsuh", Font.BOLD, 22));

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addGap(26)
				.addComponent(panel_left, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btn_saveDeck, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
								.addGap(180))
						.addGroup(groupLayout.createSequentialGroup().addGap(116)
								.addComponent(scrollPane_collection, GroupLayout.PREFERRED_SIZE, 354,
										GroupLayout.PREFERRED_SIZE)
								.addGap(148)
								.addGroup(
										groupLayout.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(btn_toCollection, Alignment.LEADING, 0, 0,
														Short.MAX_VALUE)
												.addComponent(btn_toDeck, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														159, Short.MAX_VALUE))
								.addGap(129)
								.addComponent(scrollPane_deck, GroupLayout.PREFERRED_SIZE, 355,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
										Alignment.TRAILING,
										groupLayout.createSequentialGroup().addGroup(groupLayout
												.createParallelGroup(Alignment.TRAILING)
												.addComponent(tf_deckName, GroupLayout.PREFERRED_SIZE, 227,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(groupLayout.createSequentialGroup()
														.addComponent(lbl_searchDeck, GroupLayout.PREFERRED_SIZE, 204,
																GroupLayout.PREFERRED_SIZE)
														.addGap(14)))
												.addGap(30))
										.addGroup(Alignment.TRAILING,
												groupLayout.createSequentialGroup()
														.addComponent(btn_search, GroupLayout.PREFERRED_SIZE, 97,
																GroupLayout.PREFERRED_SIZE)
														.addGap(87))))))
				.addGroup(groupLayout.createSequentialGroup().addContainerGap(1117, Short.MAX_VALUE)
						.addComponent(lbl_totalValue, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lbl_Maximum, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
						.addGap(491)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(94)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lbl_Maximum, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(lbl_totalValue))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
								.createSequentialGroup()
								.addComponent(btn_saveDeck, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
								.addGap(109).addComponent(lbl_searchDeck).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tf_deckName, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btn_search, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
								.addGap(122)
								.addComponent(btn_toDeck, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
								.addComponent(panel_left, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup().addGap(120)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(scrollPane_collection, GroupLayout.PREFERRED_SIZE, 342,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(scrollPane_deck, GroupLayout.PREFERRED_SIZE, 342,
														GroupLayout.PREFERRED_SIZE))))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btn_toCollection, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(325, Short.MAX_VALUE)));

		GroupLayout gl_panel_left = new GroupLayout(panel_left);
		gl_panel_left.setHorizontalGroup(gl_panel_left.createParallelGroup(Alignment.LEADING)
				.addComponent(btn_loadCards, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
				.addGroup(gl_panel_left.createSequentialGroup()
						.addComponent(btn_randomDeck, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel_left.setVerticalGroup(gl_panel_left.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_left.createSequentialGroup()
						.addComponent(btn_loadCards, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addGap(39)
						.addComponent(btn_randomDeck, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(118, Short.MAX_VALUE)));
		panel_left.setLayout(gl_panel_left);

		frame.getContentPane().setLayout(groupLayout);
	}
}
