package Questionnaire;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.util.ArrayList;

public class Fenetres {

	private JFrame frame;
	private File fichierQuestionnaire;
	private ArrayList<ArrayList<String>> questionnaire;
	private static ArrayList<String> reponse;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reponse = new ArrayList<String>();
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
				try {
					// Commencer à lancer le questionnaire.
					//TODO Transforme le questionnaire en suite de fenetres et enregistre les reponses.
					questionnaire = Util.importCSV("C:/Users/UL/Documents/_Strasbourg/_IA/Projet/Notre_questionnaire.csv", ",");
					activeQuestionnaire(questionnaire, 0);					
				} catch(Exception exAjouter){
					//TODO
				}
			}
		});
		splitPane.setRightComponent(btnCommencer);
		
		JButton btnCharger = new JButton("Charger un questionnaire");
		btnCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Tenter de charger le fichier
					//TODO Pour l'instant, "charger un fichier" lit jsute le path. Il faudra verifier que le fichier est correct.
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(btnCharger) == JFileChooser.APPROVE_OPTION) {
					  File fichierQuestionnaire = fileChooser.getSelectedFile();
					  lblMessageintro.setText("Fichier chargé : " + fichierQuestionnaire.getName());
					  btnCommencer.setEnabled(true);
					  // load from file
					}
					
				} catch(Exception exAjouter){
					//TODO
				}
			}
		});
		splitPane.setLeftComponent(btnCharger);
		
		
	}
	
	private void activeQuestionnaire(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne){
		String premier_element = nomQuestionnaire.get(numLigne).get(0);
		System.out.println("Active questionnaire " + premier_element + reponse.toString());
		//La ligne est une question
		if((premier_element.indexOf(":") >= 0) | (premier_element.indexOf("?") >= 0)){
			String case3 = nomQuestionnaire.get(numLigne).get(3);
			//Il s'agit d'une question a entree
			if(case3.equals("[TEXTE]")){
				questionTexte(nomQuestionnaire, numLigne);
			} else {
				questionChoixMultiples(nomQuestionnaire, numLigne);
			}
		
		//La ligne est un commentaire a garder en debut de question
		} else {
			activeQuestionnaire(nomQuestionnaire, numLigne + 1);
		}
				
		
	}
	
	private void questionChoixMultiples(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne){
		System.out.println("QCM" + numLigne);
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(nomQuestionnaire.get(numLigne).get(0));
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
				//Chercher la reponse
				String choix = cbReponse.getSelectedItem().toString();
				//Verifier que la reponse n'a pas deja ete enregistree
				//Si oui, on ecrase, si non, on ajoute
				if(reponse.size() > numLigne){
					reponse.set(numLigne, choix);
				} else {
					reponse.add(choix);
				}
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				activeQuestionnaire(nomQuestionnaire, numLigne + 1);
				question.dispose();
				
			}
		});
		splitPane.setRightComponent(btnSuivant);
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1);

			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
	}
	
	private void questionTexte(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne){
		System.out.println("Texte" + numLigne);
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(nomQuestionnaire.get(numLigne).get(0));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JTextField jtfReponse = new JTextField();
		question.add(jtfReponse);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Chercher la reponse
				String choix = jtfReponse.getText();
				//Verifier que la reponse n'a pas deja ete enregistree
				//Si oui, on ecrase, si non, on ajoute
				if(reponse.size() > numLigne){
					reponse.set(numLigne, choix);
				} else {
					reponse.add(choix);
				}
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				//activeQuestionnaire(nomQuestionnaire, numLigne + 1);
				
			}
		});
		splitPane.setRightComponent(btnSuivant);
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1);
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
	}

}
