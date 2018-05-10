package Questionnaire;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class Fenetres {

	private JFrame frame;
	private ArrayList<ArrayList<String>> questionnaire;
	private static TreeMap<Integer, Vector<String>> reponse;
	private Vector<String> listeQuestionnaires;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reponse = new TreeMap<Integer, Vector<String>>();
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
				System.out.println(questionnaire.toString());
				activeQuestionnaire(questionnaire, 0, 0);
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
				} catch(Exception exChargerFichier){
					lblMessageintro.setText("Fichier invalide");
				}
			}
		});
		splitPane.setLeftComponent(btnCharger);
		
		
	}
	
	private void activeQuestionnaire(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, int initialisation){
		String commentaire = "";
		String premier_element = nomQuestionnaire.get(numLigne).get(0);;
		if(!FormCreationHelper.fincode(nomQuestionnaire.get(numLigne))){
			//Initialisation du commentaire
			if ((numLigne > initialisation) & (commentaire.length() > 0)){
				commentaire = "";
			}
			System.out.println(premier_element);
			//Si il a possibilite d'ajouter plusieurs reponses
			if(premier_element.endsWith("++1")){
				questionAjout(nomQuestionnaire, numLigne, commentaire);
			//Il y a une page dediee et on ignore
			}else if(premier_element.endsWith("[PAGE]")){
				activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
			//La ligne est un commentaire a garder en debut de question
			} else if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
				//On cherche le nombre de lignes sur lesquelles le commentaire est effectif
				int index_par = premier_element.indexOf("(");
				commentaire = premier_element.substring(0, index_par);
				int nb_lignes = Character.getNumericValue(premier_element.charAt(index_par + 1));
				activeQuestionnaire(nomQuestionnaire, numLigne + 1, numLigne + nb_lignes);
			//Si la ligne est une question
			} else if((premier_element.indexOf(":") >= 0) | (premier_element.indexOf("?") >= 0)){
				questionSimple(nomQuestionnaire, numLigne, commentaire);
			//La la ligne est ignoree
			} else {
				activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
			}
		}		
	}
	
	private void questionSimple(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String com){
		String case3 = nomQuestionnaire.get(numLigne).get(3);
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(com + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		splitPane.setRightComponent(btnSuivant);
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				//Si la question precedente est une question avec ajouts
				if(nomQuestionnaire.get(numLigne-1).get(0).endsWith("++1")){
					//On cherche le debut du bloc et on recommence
					int ligne_a_reprendre = numLigne-2;
					while(! nomQuestionnaire.get(ligne_a_reprendre).get(0).endsWith("++1")){
						ligne_a_reprendre -= 1;
					}
					String commentaire = "";
					String premier_element = nomQuestionnaire.get(ligne_a_reprendre - 1).get(0);
					//Si la ligne precedente est un commentaire a reprendre
					if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
						commentaire = premier_element;
					}
					questionAjout(nomQuestionnaire, numLigne, commentaire);
				//Sinon, on revient a la question precedente
				} else {
					activeQuestionnaire(nomQuestionnaire, numLigne - 1, 0);
				}
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
		//Il s'agit d'une question a entree
		if(case3.equals("[TEXTE]")){
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
			
			btnSuivant.setEnabled(false);
			btnSuivant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Chercher et enregistrer la reponse
					String choix = jtfReponse.getText();
					Vector<String> v_choix = new Vector<String>();
					v_choix.addElement(choix);
					reponse.put(numLigne, v_choix);
					//On ouvre une nouvelle fenetre et on ferme la fenetre courante
					question.dispose();
					activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
					
				}
			});
			
		//Il s'agit d<une question a choix multiples
		} else {
			JComboBox<String> cbReponse = new JComboBox<String>();
			question.add(cbReponse, BorderLayout.CENTER);
			
			DefaultComboBoxModel<String> cbReponseModel = new DefaultComboBoxModel<String>();
			// Ajout des possibilites
			for (int element = 3; element < infosQuestion.size(); element ++){
				if(!infosQuestion.get(element).equals("")){
					cbReponseModel.addElement(FormCreationHelper.retireMarqueurs(infosQuestion.get(element)));
				}
				
			}
			cbReponse.setModel(cbReponseModel);
			
			btnSuivant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Chercher et enregistrer la reponse
					String choix = cbReponse.getSelectedItem().toString();
					//Si le choix initial a un marqueur, on le recupere
					String choix_complet = infosQuestion.get(cbReponse.getSelectedIndex());
					if(choix_complet.indexOf("*") >= 0){
						listeQuestionnaires.addElement(choix_complet.substring(choix_complet.indexOf("*")+1));
					}
					//Si "autre" est choisi, on demande le nom
					if(choix.equals("autre")){
						String autre_nom = JOptionPane.showInputDialog("Nom donne : ");
						if(autre_nom.equals("")){
							choix = "Non specifie";
						}
					}
					Vector<String> v_choix = new Vector<String>();
					v_choix.addElement(choix);
					reponse.put(numLigne, v_choix);
					//On ouvre une nouvelle fenetre et on ferme la fenetre courante
					question.dispose();
					activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
					
				}
			});
		}
		
	}
	
	private void questionAjout(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String com){
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		Vector<String> v_choix = new Vector<String>();
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(com + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)));
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
		
		JLabel lblListe = new JLabel("Aucun prenom rentré");
		lblListe.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblListe.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblListe, BorderLayout.CENTER);
		
		JButton btnRetirer = new JButton("Retirer");
		btnRetirer.setHorizontalAlignment(SwingConstants.CENTER);
		btnRetirer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRetirer.setEnabled(false);
		btnRetirer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Demander le nom a ajouter
				String nom = JOptionPane.showInputDialog("Nom a retirer :");
				if(v_choix.contains(nom)){
					v_choix.remove(nom);
				}
				//Mettre la liste a jour
				lblListe.setText("Noms entres : " + v_choix.toString());
				//Si la liste est vide, desactiver btnRetirer et btnSuivant
				if(v_choix.size() == 0){
					btnRetirer.setEnabled(false);
					btnSuivant.setEnabled(false);
					lblListe.setText("Aucun prenom rentré");
				}
			}
		});
		question.add(btnRetirer, BorderLayout.WEST);
		
		JButton btnAjouter = new JButton("Ajouter");
		btnAjouter.setHorizontalAlignment(SwingConstants.CENTER);
		btnAjouter.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Demander le nom a ajouter
				String nouveauNom = JOptionPane.showInputDialog("Nom a ajouter :");
				if(!v_choix.contains(nouveauNom)){
					v_choix.addElement(nouveauNom);
				}
				//Afficher le nom dans la liste
				lblListe.setText("Noms entres : " + v_choix.toString());
				//Activer Suivant et retirer
				btnRetirer.setEnabled(true);
				btnSuivant.setEnabled(true);
			}
		});
		question.add(btnAjouter, BorderLayout.EAST);
		
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Chercher et enregistrer la reponse
				reponse.put(numLigne, v_choix);
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				questionsRepetees(nomQuestionnaire, numLigne+1, com, numLigne+1, 0, new Vector<String>(), v_choix);
				
			}
		});
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				activeQuestionnaire(nomQuestionnaire, numLigne - 1, 0);
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
	}
	
	private void questionsRepetees(ArrayList<ArrayList<String>> nomQuestionnaire, int numLigne, String commentaire,
			int ligneDebut, int id_nom_courant, Vector<String> v_choix, Vector<String> v_noms){
		String case3 = nomQuestionnaire.get(numLigne).get(3);
		ArrayList<String> infosQuestion = nomQuestionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel("Pour " + v_noms.get(id_nom_courant) + ", " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)));
		lblQuestion.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
		question.add(lblQuestion, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		question.getContentPane().add(splitPane, BorderLayout.SOUTH);
		
		JButton btnSuivant = new JButton("Suivant");
		btnSuivant.setHorizontalAlignment(SwingConstants.CENTER);
		btnSuivant.setFont(new Font("Tahoma", Font.PLAIN, 11));
		splitPane.setRightComponent(btnSuivant);
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				//Si on est a la premiere ligne
				if(numLigne == ligneDebut){
					//On revient a l'ajout de noms
					questionAjout(nomQuestionnaire, numLigne - 1, commentaire);
				//Si on est au premier nom de la liste
				} else if(id_nom_courant == 0) {
					int nouvel_id = v_noms.size()-1;
					Vector<String> nouveau_v_choix = reponse.get(numLigne-1);
					//On retire le dernier element du vecteur
					nouveau_v_choix.remove(nouveau_v_choix.size() - 1);
					questionsRepetees(nomQuestionnaire, numLigne-1, commentaire, ligneDebut, nouvel_id, nouveau_v_choix, v_noms);
				//Sinon
				} else {
					Vector<String> nouveau_v_choix = v_choix;
					nouveau_v_choix.remove(nouveau_v_choix.size() - 1);
					questionsRepetees(nomQuestionnaire, numLigne, commentaire, ligneDebut, id_nom_courant-1, nouveau_v_choix, v_noms);
				}
				
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
		//Il s'agit d'une question a entree
		if(case3.equals("[TEXTE]")){
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
			
			btnSuivant.setEnabled(false);
			btnSuivant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Chercher et enregistrer la reponse
					String choix = jtfReponse.getText();
					v_choix.addElement(choix);
					//On ouvre une nouvelle fenetre et on ferme la fenetre courante
					question.dispose();
					//Si on est au dernier nom de la liste
					if(id_nom_courant == v_noms.size() - 1){
						//On enregistre le vecteur de reponses
						reponse.put(numLigne, v_choix);
						//Si on est a la derniere question a poser plusieurs fois
						if(infosQuestion.get(0).endsWith("++1")){
							//On passe a la prochaine question
							activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
						} else {
							//On passe a la prochaine question
							questionsRepetees(nomQuestionnaire, numLigne + 1, commentaire, ligneDebut, 0, new Vector<String> (), v_noms);
						}
					//On incremente le nom courant
					} else {
						questionsRepetees(nomQuestionnaire, numLigne, commentaire, ligneDebut, id_nom_courant + 1, v_choix, v_noms);
					}
				}
			});
			
		//Il s'agit d<une question a choix multiples
		} else {
			JComboBox<String> cbReponse = new JComboBox<String>();
			question.add(cbReponse, BorderLayout.CENTER);
			
			DefaultComboBoxModel<String> cbReponseModel = new DefaultComboBoxModel<String>();
			// Ajout des possibilites
			for (int element = 3; element < infosQuestion.size(); element ++){
				if(!infosQuestion.get(element).equals("")){
					cbReponseModel.addElement(FormCreationHelper.retireMarqueurs(infosQuestion.get(element)));
				}
				
			}
			cbReponse.setModel(cbReponseModel);
			
			btnSuivant.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Chercher et enregistrer la reponse
					String choix = cbReponse.getSelectedItem().toString();
					v_choix.addElement(choix);
					//Si le choix initial a un marqueur, on le recupere
					String choix_complet = infosQuestion.get(cbReponse.getSelectedIndex());
					if(choix_complet.indexOf("*") >= 0){
						listeQuestionnaires.addElement(choix_complet.substring(choix_complet.indexOf("*")+1));
					}
					//on ferme la fenetre courante
					question.dispose();
					//Si on est au dernier nom de la liste
					if(id_nom_courant == v_noms.size() - 1){
						//On enregistre le vecteur de reponses
						reponse.put(numLigne, v_choix);
						//Si on est a la derniere question a poser plusieurs fois
						if(infosQuestion.get(0).endsWith("++1")){
							//On passe a la prochaine question
							activeQuestionnaire(nomQuestionnaire, numLigne + 1, 0);
						} else {
							//On passe a la prochaine question
							questionsRepetees(nomQuestionnaire, numLigne + 1, commentaire, ligneDebut, 0, new Vector<String> (), v_noms);
						}
					//On incremente le nom courant
					} else {
						questionsRepetees(nomQuestionnaire, numLigne, commentaire, ligneDebut, id_nom_courant + 1, v_choix, v_noms);
					}
					
				}
			});
		}
	}

}
