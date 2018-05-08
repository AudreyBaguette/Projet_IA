package Questionnaire;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.util.TreeMap;

public class Fenetres {

	private JFrame frame;
	private File fichierQuestionnaire;
	private ArrayList<ArrayList<String>> questionnaire;
	private static TreeMap<Integer, String> reponse;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reponse = new TreeMap<Integer, String>();
					Fenetres window = new Fenetres();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fenetres() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 554, 214);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblAcceuil = new JLabel("Bonjour! Bienvenue dans ce questionnaire automatis\u00E9.");
		lblAcceuil.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblAcceuil.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblAcceuil, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JLabel lblMessageintro = new JLabel("Veuillez r\u00E9pondre \u00E0 toutes les questions de fa\u00E7on sinc\u00E8re.");
		lblMessageintro.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessageintro.setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.getContentPane().add(lblMessageintro, BorderLayout.CENTER);
		
		JButton btnCommencer = new JButton("Commencer");
		btnCommencer.setEnabled(false);
		btnCommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				activeQuestionnaire(questionnaire, 0);
			}
		});
		splitPane.setRightComponent(btnCommencer);
		
		JButton btnCharger = new JButton("Charger un questionnaire");
		btnCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Tenter de charger le fichier
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(btnCharger) == JFileChooser.APPROVE_OPTION) {
					  File fichierQuestionnaire = fileChooser.getSelectedFile();
					  if(fichierQuestionnaire.getName().endsWith("csv")){
						  questionnaire = CSVParser.importCSV(fichierQuestionnaire.getPath());
						  lblMessageintro.setText("Fichier chargé : " + fichierQuestionnaire.getName());
						  btnCommencer.setEnabled(true);
					  } else {
						  lblMessageintro.setText("Le fichier " + fichierQuestionnaire.getName() + " n'a pas le bon format.");
					  }
					}
				} catch(Exception exAjouter){
					lblMessageintro.setText("Fichier invalide");
				}
			}
		});
		splitPane.setLeftComponent(btnCharger);
		
		
	}
	
	private void activeQuestionnaire(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne){
		String commentaire = "";
		String premier_element = "";
		int initialisation = 0;
		if ((numLigne > initialisation) & (commentaire.length() > 0)){
			commentaire = "";
		}
		if(nomQuestionnaire.get(numLigne).size() > 0){
			premier_element = nomQuestionnaire.get(numLigne).get(0);
		}
		//La ligne est une question
		System.out.println(premier_element);
		if((premier_element.indexOf(":") >= 0) | (premier_element.indexOf("?") >= 0)){
			String case3 = nomQuestionnaire.get(numLigne).get(3);
			//Il s'agit d'une question a entree
			if(case3.equals("[TEXTE]")){
				questionTexte(nomQuestionnaire, numLigne, commentaire);
			} else {
				questionChoixMultiples(nomQuestionnaire, numLigne, commentaire);
			}
		
		//La ligne est un commentaire a garder en debut de question
		} else if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
			//On cherche le nombre de lignes sur lesquelles le commentaire est effectif
			int index_par = premier_element.indexOf("(");
			commentaire = premier_element.substring(0, index_par);
			int nb_lignes = Character.getNumericValue(premier_element.charAt(index_par + 1));
			initialisation = numLigne + nb_lignes;
		//La laigne est ignoree
		} else {
			activeQuestionnaire(nomQuestionnaire, numLigne + 1);
		}
				
		
	}
	
	private void questionChoixMultiples(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String com){
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(com + " " + nomQuestionnaire.get(numLigne).get(0));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JComboBox<String> cbReponse = new JComboBox<String>();
		question.add(cbReponse, BorderLayout.CENTER);
		
		DefaultComboBoxModel<String> cbReponseModel = new DefaultComboBoxModel<String>();
		// Ajout des possibilites
		for (int element = 3; element < infosQuestion.size(); element ++){
			if(!infosQuestion.get(element).equals("")){
				cbReponseModel.addElement(infosQuestion.get(element));
			}
			
		}
		cbReponse.setModel(cbReponseModel);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Chercher et enregistrer la reponse
				String choix = cbReponse.getSelectedItem().toString();
				reponse.put(numLigne, choix);
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne + 1);
				
			}
		});
		splitPane.setRightComponent(btnSuivant);
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1);
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
	}
	
	private void questionTexte(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String com){
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(com + " " + nomQuestionnaire.get(numLigne).get(0));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.setEnabled(false);
		splitPane.setRightComponent(btnSuivant);
		
		JTextField jtfReponse = new JTextField();
		jtfReponse.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void removeUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void insertUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void activeBouton() {
				String choix = jtfReponse.getText();
				if(choix.length() > 0){
						btnSuivant.setEnabled(true);
				}
			}  
		});
		
		question.add(jtfReponse);
		
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Chercher et enregistrer la reponse
				String choix = jtfReponse.getText();
				reponse.put(numLigne, choix);
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne + 1);
				
			}
		});
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1);
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
		
	}
	
	private void questionAjout(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String com){
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(com + " " + nomQuestionnaire.get(numLigne).get(0));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.setEnabled(false);
		splitPane.setRightComponent(btnSuivant);
		
		JTextField jtfReponse = new JTextField();
		jtfReponse.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void removeUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void insertUpdate(DocumentEvent e) {
				activeBouton();
			}
			public void activeBouton() {
				String choix = jtfReponse.getText();
				if(choix.length() > 0){
						btnSuivant.setEnabled(true);
				}
			}  
		});
		
		question.add(jtfReponse);
		
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Chercher et enregistrer la reponse
				String choix = jtfReponse.getText();
				reponse.put(numLigne, choix);
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne + 1);
				
			}
		});
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1);
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
		
	}

}
