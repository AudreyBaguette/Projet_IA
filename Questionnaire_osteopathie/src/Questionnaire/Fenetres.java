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
	private TreeMap<String, String> listeQuestionnaires;
	private Vector<String> aExclure;
	private Vector<Integer> lignesCommentaire;
	private String commentaire;
	private ArrayList<String> marqueurs = new ArrayList<String>();
	private ArrayList<String> questionairesfinals = new ArrayList<String>() ;

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
		
		JLabel lblAcceuil = new JLabel("<html>Bonjour! Bienvenue dans ce questionnaire automatis\u00E9.</html>");
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
				aExclure = new Vector<String>();
				aExclure.addElement("");
				aExclure.addElement("");
				lignesCommentaire = new Vector<Integer>();
				lignesCommentaire.addElement(0);
				lignesCommentaire.addElement(0);
				listeQuestionnaires = new TreeMap<String, String>();
				commentaire = "";
				suivantQuestionnaire(0);
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
						  lblMessageintro.setText("Fichier charg� : " + fichierQuestionnaire.getName());
						  btnCommencer.setEnabled(true);
					  } else {
						  lblMessageintro.setText("Le fichier " + fichierQuestionnaire.getName() + " n'a pas le bon format.");
					  }
					  
					}
				} catch(Exception exChargerFichier){
					lblMessageintro.setText("Fichier invalide");
					System.out.println(exChargerFichier.toString());
				}
			}
		});
		splitPane.setLeftComponent(btnCharger);
		
		
	}
	
	private void suivantQuestionnaire(int numLigne){
		System.out.println(numLigne);
		if(questionnaire.get(numLigne).size() >0 ) {
			String premier_element = questionnaire.get(numLigne).get(0);
			ArrayList<String> ligne = questionnaire.get(numLigne);
			/*if(numLigne >= lignesCommentaire.lastElement()){
				commentaire = "";
			}*/
			if(!FormCreationHelper.fincode(questionnaire.get(numLigne))){ // si c'est la fin du code
				//Si la ligne est a exclure
				/*if(premier_element.contains(aExclure.firstElement()) & !aExclure.firstElement().equals("")){
					int ligneSuivante = numLigne;
					while(!questionnaire.get(ligneSuivante).get(0).contains(aExclure.lastElement())){
						ligneSuivante++;
					}
					aExclure.set(0, "");
					aExclure.set(1, "");
					suivantQuestionnaire(ligneSuivante + 1);*/
				//Si il a possibilite d'ajouter plusieurs reponses
			
				//si la ligne contient une question
				if(FormCreationHelper.isaquestion(ligne)) {
					if(FormCreationHelper.hastoanswerquestion(marqueurs, ligne)) {
						questionSimple(numLigne);
					}
					else {
						suivantQuestionnaire(numLigne + 1);
					}
					//Si il a possibilite d'ajouter plusieurs reponses
				}else if(premier_element.endsWith("++1")){
					questionAjout(numLigne);
					//Il y a une page dediee et on ignore
				}else if(premier_element.endsWith("[PAGE]")){   //@TODO 
					suivantQuestionnaire(numLigne + 1);
					//La ligne est un commentaire a garder en debut de question
				} else if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
					//On cherche le nombre de lignes sur lesquelles le commentaire est effectif
					int index_par = premier_element.indexOf("(");
					commentaire = premier_element.substring(0, index_par);
					lignesCommentaire.set(0, numLigne);
					lignesCommentaire.set(1, numLigne + Character.getNumericValue(premier_element.charAt(index_par + 1)));
					suivantQuestionnaire(numLigne + 1);
					//Si la ligne est une question
				} else {
					suivantQuestionnaire(numLigne + 1);
				}
			}
			else {
				//si c'est la fin du code
			}
		}
		else {
			suivantQuestionnaire(numLigne + 1);
		}
	}
	
	private void precedentQuestionnaire(int numLigne){
		String premier_element = questionnaire.get(numLigne).get(0);
		if(numLigne != 0){
			//Si la ligne est a exclure
			if(premier_element.contains(aExclure.lastElement()) & !aExclure.lastElement().equals("")){
				int lignePrecedente = numLigne;
				while(!questionnaire.get(lignePrecedente).get(0).contains(aExclure.firstElement())){
					lignePrecedente--;
				}
				suivantQuestionnaire(lignePrecedente - 1);
			//Si il a possibilite d'ajouter plusieurs reponses
			}else if(premier_element.endsWith("++1")){
				//on est a la fin d'un bloc d'ajout. on doit remonter au debut
				int lignePrecedente = numLigne-1;
				while(!questionnaire.get(lignePrecedente).get(0).endsWith("++1")){
					lignePrecedente--;
				}
				questionAjout(lignePrecedente);
			//Il y a une page dediee et on ignore
			}else if(premier_element.endsWith("[PAGE]")){
				suivantQuestionnaire(numLigne - 1);
			//La ligne est un commentaire a garder en debut de question
			} else if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
				//On ignore
				suivantQuestionnaire(numLigne - 1);
			//Si la ligne est une question
			} else if((premier_element.indexOf(":") >= 0) | (premier_element.indexOf("?") >= 0)){
				questionSimple(numLigne);
			//La la ligne est ignoree
			} else {
				suivantQuestionnaire(numLigne - 1);
			}
		}		
	}
	
	private void questionSimple(int numLigne){
		String case3 = questionnaire.get(numLigne).get(3);
		ArrayList<String> infosQuestion = questionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel("<html>" + commentaire + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)) + "</html>");
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
				precedentQuestionnaire(numLigne - 1);
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
					suivantQuestionnaire(numLigne + 1);
					
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
					String choix_complet = infosQuestion.get(cbReponse.getSelectedIndex() + 3).trim();
					/*if(choix_complet.indexOf("*") >= 0){
						String marqueur = choix_complet.substring(choix_complet.indexOf("("));
						int i_marqueur2 = marqueur.substring(1).indexOf("(");
						//Si une parenthese, c'est un marqueur de questionnaire
						if(i_marqueur2 == -1){
							String nom = "";
							if(infosQuestion.get(2).length() > 0){
								int ligne = Character.getNumericValue(infosQuestion.get(2).charAt(0));
								nom = reponse.get(numLigne-ligne).lastElement();
							}
							listeQuestionnaires.put(choix_complet.substring(choix_complet.indexOf("*")+1), nom);
						//Si deux parenthese, c'est un marqueur d'exclusion de questions
						} else {
							marqueurs.add(e)
							aExclure.set(0, marqueur.substring(0, i_marqueur2));
							aExclure.set(1, marqueur.substring(i_marqueur2));
						}
					}*/
					if(choix_complet.indexOf("*") >= 0) {
						
						int i =choix_complet.indexOf("*");
						System.out.println("il y a un questionnaire index * : "+ i);
						if(choix_complet.charAt(i+1) =='(') {
							System.out.println("une ( suit l'*");
							i= i+2;
							String questionaire = "";
							while(i<choix_complet.length() && choix_complet.charAt(i)!=')') {
								System.out.println("i : "+i+" char : "+choix_complet.charAt(i));
								questionaire = questionaire + choix_complet.charAt(i);
								i++;
							}
							if(choix_complet.charAt(i)==')') {
								questionairesfinals.add(questionaire);
							}
						}
						if(i<choix_complet.length()-1 && choix_complet.charAt(i+1)=='(') {
							i=i+2;
							String marqueur = "";
							while(i<choix_complet.length() && choix_complet.charAt(i)!=')') {
								marqueur = marqueur + choix_complet.charAt(i);
								i++;
							}
							if(choix_complet.charAt(i)==')') {
								marqueurs.add(marqueur);
							}
						}
					}
					else if(choix_complet.indexOf("(") >= 0){
						int i = choix_complet.indexOf("(");
						i++;
						String marqueur = "";
						while(i<choix_complet.length() && choix_complet.charAt(i)!=')') {
							marqueur = marqueur + choix_complet.charAt(i);
							i++;
						}
						if(choix_complet.charAt(i)==')') {
							marqueurs.add(marqueur);
						}
						
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
					suivantQuestionnaire(numLigne + 1);
					
				}
			});
		}
		
	}
	
	private void questionAjout(int numLigne){
		ArrayList<String> infosQuestion = questionnaire.get(numLigne);
		Vector<String> v_choix = new Vector<String>();
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel(commentaire + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)));
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
				questionsRepetees(numLigne+1, numLigne+1, 0, new Vector<String>(), v_choix);
				
			}
		});
		
		JButton btnPrecedent = new JButton("Précédent");
		btnPrecedent.setHorizontalAlignment(SwingConstants.CENTER);
		btnPrecedent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPrecedent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On ouvre une nouvelle fenetre et on ferme la fenetre courante
				question.dispose();
				precedentQuestionnaire(numLigne - 1);
			}
		});
		splitPane.setLeftComponent(btnPrecedent);
		
	}
	
	private void questionsRepetees(int numLigne, int ligneDebut, int id_nom_courant, Vector<String> v_choix, Vector<String> v_noms){
		String case3 = questionnaire.get(numLigne).get(3);
		ArrayList<String> infosQuestion = questionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel("<html>Pour " + v_noms.get(id_nom_courant) + ", " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)) + "</html>");
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
					questionAjout(numLigne - 1);
				//Si on est au premier nom de la liste
				} else if(id_nom_courant == 0) {
					int nouvel_id = v_noms.size()-1;
					Vector<String> nouveau_v_choix = reponse.get(numLigne-1);
					//On retire le dernier element du vecteur
					nouveau_v_choix.remove(nouveau_v_choix.size() - 1);
					questionsRepetees(numLigne-1, ligneDebut, nouvel_id, nouveau_v_choix, v_noms);
				//Sinon
				} else {
					Vector<String> nouveau_v_choix = v_choix;
					nouveau_v_choix.remove(nouveau_v_choix.size() - 1);
					questionsRepetees(numLigne, ligneDebut, id_nom_courant-1, nouveau_v_choix, v_noms);
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
							suivantQuestionnaire(numLigne + 1);
						} else {
							//On passe a la prochaine question
							questionsRepetees(numLigne + 1, ligneDebut, 0, new Vector<String> (), v_noms);
						}
					//On incremente le nom courant
					} else {
						questionsRepetees(numLigne, ligneDebut, id_nom_courant + 1, v_choix, v_noms);
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
						String marqueur = choix_complet.substring(choix_complet.indexOf("("));
						int i_marqueur2 = marqueur.substring(1).indexOf("(");
						//Si une parenthese, c'est un marqueur de questionnaire
						if(i_marqueur2 == -1){
							String nom = "";
							if(infosQuestion.get(2).length() > 0){
								int ligne = Character.getNumericValue(infosQuestion.get(2).charAt(0));
								nom = reponse.get(numLigne-ligne).lastElement();
							}
							listeQuestionnaires.put(choix_complet.substring(choix_complet.indexOf("*")+1), nom);
						//Si deux parenthese, c'est un marqueur d'exclusion de questions
						} else {
							aExclure.set(0, marqueur.substring(0, i_marqueur2));
							aExclure.set(1, marqueur.substring(i_marqueur2));
							System.out.println(aExclure.toString());
						}
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
							suivantQuestionnaire(numLigne + 1);
						} else {
							//On passe a la prochaine question
							questionsRepetees(numLigne + 1, ligneDebut, 0, new Vector<String> (), v_noms);
						}
					//On incremente le nom courant
					} else {
						questionsRepetees(numLigne, ligneDebut, id_nom_courant + 1, v_choix, v_noms);
					}
					
				}
			});
		}
	}

}
