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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class Fenetres {

	private JFrame frame;
	private ArrayList<ArrayList<String>> questionnaire;
	private static TreeMap<Integer, Vector<String>> reponse;
	private Vector<String> aExclure;
	private Vector<Integer> lignesCommentaire;
	private int nbpagestitre =0;
	private String titremultipage = "";
	private ArrayList<String> marqueurs = new ArrayList<String>();
	private TreeMap<Integer, Vector<String>> questionairesfinaux = new TreeMap<Integer, Vector<String>>() ;

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
				aExclure = new Vector<String>();
				aExclure.addElement("");
				aExclure.addElement("");
				lignesCommentaire = new Vector<Integer>();
				lignesCommentaire.addElement(0);
				lignesCommentaire.addElement(0);
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
	
	private void suivantQuestionnaire(int numLigne){
		if(questionnaire.get(numLigne).size() >0 ) {
			String premier_element = questionnaire.get(numLigne).get(0);
			ArrayList<String> ligne = questionnaire.get(numLigne);
			System.out.println(numLigne + " : "+ premier_element);
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
				nbpagestitre --;
				if(nbpagestitre <1) {
					titremultipage ="";
				}
				if(premier_element.trim().length()<=0) {
					suivantQuestionnaire(numLigne + 1);
				//si c'est un titre multipage	
				}else if(FormCreationHelper.titremultipages(ligne)!= -1) {
					titremultipage = FormCreationHelper.retireMarqueurs(premier_element);
					nbpagestitre = FormCreationHelper.titremultipages(ligne);
					suivantQuestionnaire(numLigne + 1);
				//si la ligne contient une question
				}else if(FormCreationHelper.isaquestion(ligne)) {
					if(FormCreationHelper.hastoanswerquestion(marqueurs, ligne)) {
						if(FormCreationHelper.isquestiontoaskmanytimes(ligne)){
							questionAjout(numLigne);
						}
						else {
							questionSimple(numLigne);
						}
						
					}
					else {
						suivantQuestionnaire(numLigne + 1);
					}
					//Si il a possibilite d'ajouter plusieurs reponses
				}else if(FormCreationHelper.isquestiontoaskmanytimes(ligne)){
					if(FormCreationHelper.hastoanswerquestion(marqueurs, ligne)) {
						questionAjout(numLigne);
					}
					else {
						suivantQuestionnaire(numLigne + 2);
					}
					//Il y a une page dediee et on ignore
				}else if(premier_element.endsWith("[PAGE]")){   //@TODO 
					suivantQuestionnaire(numLigne + 1);
					//La ligne est un commentaire a garder en debut de question
				} /*else if((premier_element.indexOf("...") >= 0) | (premier_element.indexOf("…") >= 0)) {
					//On cherche le nombre de lignes sur lesquelles le commentaire est effectif
					int index_par = premier_element.indexOf("(");
					commentaire = premier_element.substring(0, index_par);
					lignesCommentaire.set(0, numLigne);
					lignesCommentaire.set(1, numLigne + Character.getNumericValue(premier_element.charAt(index_par + 1)));
					suivantQuestionnaire(numLigne + 1);
					//Si la ligne est une question
				} */else {
					suivantQuestionnaire(numLigne + 1);
				}
			}
			else {
				System.out.println(reponse.toString());
				System.out.println(questionairesfinaux.toString());
				afficherQuestionnaires(numLigne);
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
		ArrayList<String> infosQuestion = questionnaire.get(numLigne);
		
		JFrame question;
		question = new JFrame();
		question.setBounds(100, 100, 554, 214);
		question.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		question.setVisible(true);
		
		JLabel lblQuestion = new JLabel("<html>" + titremultipage + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)) + "</html>");
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
		if(FormCreationHelper.tocomplite(questionnaire.get(numLigne))){
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
					reponse.put(numLigne+1, v_choix);
					//On ouvre une nouvelle fenetre et on ferme la fenetre courante
					question.dispose();
					suivantQuestionnaire(numLigne + 1);
					
				}
			});
			
		//Il s'agit d'une question a choix multiples
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
					//
					if(questionairesfinaux.containsKey(numLigne)){
						questionairesfinaux.remove(numLigne);
					}
					//Chercher et enregistrer la reponse
					String choix = cbReponse.getSelectedItem().toString();
					//Si le choix initial a un marqueur, on le recupere
					String choix_complet = infosQuestion.get(cbReponse.getSelectedIndex() + 3).trim();
					if(choix_complet.indexOf("*") >= 0) {
						
						int i =choix_complet.indexOf("*");
						if(choix_complet.charAt(i+1) =='(') {
							i= i+2;
							String nomQuestionaire = "";
							while(i<choix_complet.length() && choix_complet.charAt(i)!=')') {
								nomQuestionaire = nomQuestionaire + choix_complet.charAt(i);
								i++;
							}
							if(choix_complet.charAt(i)==')') {
								String case3 = infosQuestion.get(2);
								String nom = "";
								if(case3.equals("[TEXTE]")){
									nom = JOptionPane.showInputDialog("Son nom : ");
								} else if (!case3.isEmpty()) {
									int ligneNom = Integer.parseInt(case3);
									nom = reponse.get(ligneNom).get(0);
								}
								Vector<String> vQuestionnaire = new Vector<String>();
								vQuestionnaire.addElement(nomQuestionaire);
								vQuestionnaire.addElement(nom);
								questionairesfinaux.put(numLigne, vQuestionnaire);
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
					reponse.put(numLigne + 1, v_choix);
					System.out.println("la reponse ajout�e est : "+ reponse.get(numLigne+1));
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
		
		JLabel lblQuestion = new JLabel(titremultipage + " " + FormCreationHelper.retireMarqueurs(infosQuestion.get(0)));
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
				questionsRepetees(numLigne+1, numLigne+1 , 0, new Vector<String>(), v_choix);
				
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
		//String case3 = questionnaire.get(numLigne).get(3);
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
		if(FormCreationHelper.tocomplite(infosQuestion)){
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
						if(!FormCreationHelper.isquestiontoaskmanytimes(questionnaire.get(numLigne + 1))){
							//On passe a la prochaine question
							suivantQuestionnaire(numLigne + 1);
						} else {
							//On passe a la prochaine question
							questionsRepetees(numLigne + 1, numLigne + 1, 0, new Vector<String> (), v_noms);
							
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
					if(questionairesfinaux.containsKey(numLigne)){
						questionairesfinaux.remove(numLigne);
					}
					//Chercher et enregistrer la reponse
					String choix = cbReponse.getSelectedItem().toString();
					v_choix.addElement(choix);
					//Si le choix initial a un marqueur, on le recupere
					String choix_complet = infosQuestion.get(cbReponse.getSelectedIndex()+ 3).trim();
					if(choix_complet.indexOf("*")>=0) {
						int i = choix_complet.indexOf("*");
						if(choix_complet.charAt(i+1)=='(') {
							i=i+2;
							String nomQuestionnaire ="";
							while(i<choix_complet.length() && choix_complet.charAt(i)!=')') {
								nomQuestionnaire = nomQuestionnaire + choix_complet.charAt(i);
								i++;
							}
							if(choix_complet.charAt(i)==')') {
								String case3 = infosQuestion.get(2);
								String nom = "";
								if(!case3.isEmpty()){
									nom = v_noms.get(id_nom_courant);
								}
								Vector<String> vQuestionnaire = new Vector<String>();
								vQuestionnaire.addElement(nomQuestionnaire);
								vQuestionnaire.addElement(nom);
								questionairesfinaux.put(numLigne, vQuestionnaire);
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
					//on ferme la fenetre courante
					question.dispose();
					//Si on est au dernier nom de la liste
					if(id_nom_courant == v_noms.size() - 1){
						//On enregistre le vecteur de reponses
						reponse.put(numLigne, v_choix);
						//Si on est a la derniere question a poser plusieurs fois
						if(!FormCreationHelper.isquestiontoaskmanytimes(questionnaire.get(numLigne + 1))){
							//On passe a la prochaine question
							suivantQuestionnaire(numLigne + 1);
						} else {
							//On passe a la prochaine question
							questionsRepetees(numLigne + 1, numLigne+1, 0, new Vector<String> (), v_noms);
						}
					//On incremente le nom courant
					} else {
						questionsRepetees(numLigne, ligneDebut, id_nom_courant + 1, v_choix, v_noms);
					}
					
				}
			});
		}
	}

	private void sauvegarder(JButton bouton){
		try{
			JFileChooser fileChooser = new JFileChooser();
			File fichier = new File("reponses.txt");
			if (fileChooser.showSaveDialog(bouton) == JFileChooser.APPROVE_OPTION) {
			  fichier = fileChooser.getSelectedFile();
			}
			BufferedWriter br = new BufferedWriter(new FileWriter(fichier));
			Set<Integer> cles = reponse.keySet();
			for(Iterator<Integer> it = cles.iterator(); it.hasNext(); ) {
		       br.write(reponse.get(cles.iterator().next()).toString());
			}
			br.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void afficherQuestionnaires(int ligne){
		System.out.println("avant");
		boolean ajouter = false;
		String quest = "";
		String marqueur = "";
		Vector<String> qPoses = new Vector<String>();
		String liste = "<html>Questionnaires à remplir :<br>";
		Collection<Vector<String>> qFinaux = questionairesfinaux.values();
		TreeMap<String, String> nomsQ = new TreeMap<String, String>();
		while(ligne < questionnaire.size()){
			if(questionnaire.get(ligne).size() > 0){
				if(questionnaire.get(ligne).get(0).length() > 0){
					String premier = questionnaire.get(ligne).get(0);
					System.out.println(premier);
					int index = premier.indexOf("(");
					if(index == 0){
						marqueur = premier.substring(premier.indexOf("(") + 1, premier.indexOf(")"));
						System.out.println(marqueur);
						ajouter = true;
					} else if(index == 3){
						ajouter = false;
						System.out.println("ajout dans map");
						nomsQ.put(marqueur, quest);
						quest="";
					} else if(ajouter){
						quest = quest + premier;
					}
				}
			}
			ligne ++;
		}
		/*for(Iterator<Vector<String>> it = qFinaux.iterator(); it.hasNext(); ) {
			Vector<String> listQ = it.next();
	       if(nomsQ.containsKey(listQ.firstElement())){
	    	   String nouveauQ = nomsQ.get(listQ.firstElement());
	    	   if(!listQ.lastElement().isEmpty()){
	    		   nouveauQ = nouveauQ + " pour " + listQ.lastElement();
	    	   }
	    	   if(!qPoses.contains(nouveauQ)){
	    		   liste = liste + nouveauQ + "<br>";
	    		   qPoses.add(nouveauQ);
	    	   }
			}
		}*/
		
		// Obtenir l'ensemble des entr�es
        Set set = nomsQ.entrySet();
     
        // Obtenir l'iterator pour parcourir la liste
        Iterator it = set.iterator();
     
        // Afficher les pairs cl�-valeur
        while(it.hasNext()) {
          Map.Entry mentry = (Map.Entry)it.next();
          /*System.out.print("cl�: "+mentry.getKey() + " - ");
          System.out.println("Valeur: "+mentry.getValue());*/
          int cle =Integer.parseInt((String)mentry.getKey()); 
          System.out.println("cle : "+ cle);
          if(questionairesfinaux.containsKey(cle)) {
        	  Vector<String> elem = questionairesfinaux.get(cle);
        	  if(elem.size()>1) {
        		  liste = liste + elem.get(0)+ " pour " +elem.get(1) +"<br>";
        	  }
        	  else if(elem.size()>0 ){
        		  liste = liste + elem.get(0)+"<br>";
        		  qPoses.add(elem.get(0));
        	  }
          }
        } 
		
		liste = liste + "</html>";
		JFrame listeQ;
		listeQ = new JFrame();
		listeQ.setBounds(100, 100, 554, 214);
		listeQ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		listeQ.setVisible(true);
		
		JLabel lblListe = new JLabel(liste);
		lblListe.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblListe.setHorizontalAlignment(SwingConstants.CENTER);
		listeQ.add(lblListe, BorderLayout.CENTER);
		
		JButton sauver = new JButton("Sauvegarder les réponses");
		sauver.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		sauver.setHorizontalAlignment(SwingConstants.CENTER);
		listeQ.add(sauver, BorderLayout.SOUTH);
		
		sauver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sauvegarder(sauver);
			}
		});
	}
}
