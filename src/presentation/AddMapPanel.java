package presentation;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domaine.simulateur.Simulateur;


public class AddMapPanel extends JPanel {
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private JFormattedTextField largeurMap;
	private JFormattedTextField longueurMap;

	
	public AddMapPanel(Simulateur simulateur, boolean isMap){
		setLayout(null);
		setMinimumSize(new Dimension(200, 180));
		setPreferredSize(new Dimension(300, 180));
		JLabel lblDimensionMap = new JLabel();
		
		
		lblDimensionMap.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 17));
		lblDimensionMap.setBounds(35, 6, 193, 21);
		add(lblDimensionMap);
		
		JLabel lblVitesseDuVhicule = new JLabel("Largeur (Km) : ");
		lblVitesseDuVhicule.setBounds(19, 93, 91, 16);
		add(lblVitesseDuVhicule);
		largeurMap = new JFormattedTextField();
		largeurMap.setHorizontalAlignment(SwingConstants.CENTER);
		lblVitesseDuVhicule.setLabelFor(largeurMap);
		largeurMap.setText("500");
		largeurMap.setBounds(134, 89, 56, 23);
		add(largeurMap);
		
		
		
		JLabel lblLongueurkm = new JLabel("Longueur (Km) : ");
		lblLongueurkm.setBounds(19, 121, 110, 16);
		add(lblLongueurkm);
		longueurMap = new JFormattedTextField();
		longueurMap.setHorizontalAlignment(SwingConstants.CENTER);
		lblVitesseDuVhicule.setLabelFor(longueurMap);
		longueurMap.setText("500");
		longueurMap.setBounds(134, 124, 56, 23);
		add(longueurMap);
		
		JLabel lblQuelleDimension = new JLabel("<html><body>Quelles sont les dimensions réelles de votre carte?</body></html>");
		lblQuelleDimension.setBounds(6, 39, 288, 32);
		
		if(isMap){
			lblDimensionMap.setText("Dimension de la carte");
			lblVitesseDuVhicule.setText("Largeur (Km) : ");
			lblLongueurkm.setText("Longueur (Km) : ");
			lblQuelleDimension.setText("<html><body>Quelles sont les dimensions réelles de votre carte?</body></html>");
		}
		else
		{
			lblDimensionMap.setText("Nouvelle position");
			lblVitesseDuVhicule.setText("Position X (m):");
			lblLongueurkm.setText("Position Y (m):");
			lblQuelleDimension.setText("<html><body>Quelles sont les nouvelles positions du noeud?</body></html>");
		}
		add(lblQuelleDimension);
	}	
	public AddMapPanel(Simulateur simulateur, boolean isMap, float positionX, float positionY)
	{
		this(simulateur, isMap);
		longueurMap.setText(Float.toString(positionY));
		largeurMap.setText(Float.toString(positionX));
		
	}
	public float reqLongueurMap()
	{
		return Float.parseFloat(longueurMap.getText());
	}
	public float reqLargeurMap()
	{
		return Float.parseFloat(largeurMap.getText());
	}
	
		
	
}
