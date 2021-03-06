package domaine.reseau;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class GestionnaireReseau  implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -1809017684776724805L;


	private Carte m_carte = new Carte();


	private ZoomModel m_zoom = new ZoomModel();
	private Echelle m_echelle = new Echelle(m_zoom);
	private Grille m_grille = new Grille(m_echelle);
	transient private Image m_imageDeFond = null;

	
	public void initialiseMap(Image imageDeFond, float dimReel)
	{
		m_imageDeFond = imageDeFond;
		BufferedImage img = (BufferedImage)m_imageDeFond;
		
		int max = Math.max(img.getWidth(), img.getHeight());
		m_echelle.initialiseMap(dimReel/max);
		this.updaterCarte();
		
	}
	
	public void effacerTout()
	{
		m_carte.effacerTout();
	}
	public void effacerImageDeFond()
	{
		m_imageDeFond = null;
	}
	public void resetReseau()
	{
		m_carte.resetEtatNoeud();
	}
	
	public float reqMetreParStep() 
	{
		return m_echelle.reqMetreParStep();
	}
	
	public void asgMetreParStep(int value) 
	{
		
		if (value > 0 && m_echelle.reqMetreParStep() != value) 
		{
			m_echelle.setMetreParStep(value);
			updaterCarte();
		}
	}
	
	public Carte reqCarte() {
		return m_carte;
	}
	public void asgCarte(Carte carte)
	{
		m_carte = carte;
	}
	
	public Noeud ajouterNoeud(int positionX, int positionY) 
	{
		Position position = new Position((float) positionX, (float) positionY);
		position = this.reqPositionEnMetre(position);
		Noeud noeudALaPosition = m_carte.reqNoeud(position);
		if (noeudALaPosition == null) 
		{
			return m_carte.ajouterNoeud(position);
		}
		else
		{
			return noeudALaPosition;
		}
		

	}
	
	public Position reqPositionEnMetre(Position positionEnPixel) {
		
			return m_grille.reqPositionEnMetre(positionEnPixel);
		
	}

	public Arc ajouterArc(Noeud noeudSource, Noeud noeudDest) {
		return m_carte.ajouterArc(noeudSource, noeudDest);
	}
	
	
	
	public void deplacerNoeud(Noeud noeud, int positionX, int positionY) 
	{
		Position nouvellePosition = new Position((float) positionX,
				(float) positionY);
		nouvellePosition = m_grille.reqPositionEnMetre(nouvellePosition);

		if (nouvellePosition.reqPositionX() >= 0
				&& nouvellePosition.reqPositionY() >= 0) 
		{
			m_carte.deplacerNoeud(noeud, nouvellePosition);
		}
	}
	
	public void enleverNoeud(Noeud noeud){
		m_carte.enleverNoeud(noeud);
	}
	public void modifierPositionPreciseNoeud(Noeud noeud,float positionX, float positionY){
		
		Position nouvellePosition = new Position(positionX, positionY);
		
		if (noeud != null)
		{
			m_carte.deplacerNoeud(noeud, nouvellePosition);
		}
	}
	
	public void updaterCarte() 
	{
		for (Noeud noeud : m_carte.reqListeNoeuds()) 
		{
			m_carte.deplacerNoeud(noeud,
					m_grille.reqUpdatedPosition(noeud.reqPosition()));
		}
	}
	public Noeud reqNoeud(int positionX, int positionY)
	{
		for (Noeud noeud : m_carte.reqListeNoeuds()) {
			Position noeud_position = this.reqPositionEnPixel(noeud.reqPosition());

			if (Math.round(noeud_position.reqPositionX()) == positionX
					&& Math.round(noeud_position.reqPositionY()) == positionY) {

				return noeud;
			} else {
				int x = positionX;
				int y = positionY;
				int c1 = Math.round(noeud_position.reqPositionX());
				int c2 = Math.round(noeud_position.reqPositionY());
				float circle = (x - c1) * (x - c1) + (y - c2) * (y - c2);

				if (circle <= 10 * 10) {
					return noeud;
				}

			}
		}

		return null;
	}
	public Arc reqArc(int positionX, int positionY) 
	{
		for(Arc arc: m_carte.reqListeArcs()){
			float x1 = this.reqPositionEnPixel(arc.reqNoeudSource().reqPosition()).reqPositionX();
			float x2 = this.reqPositionEnPixel(arc.reqNoeudDest().reqPosition()).reqPositionX();
			float y1 = this.reqPositionEnPixel(arc.reqNoeudSource().reqPosition()).reqPositionY();
			float y2 = this.reqPositionEnPixel(arc.reqNoeudDest().reqPosition()).reqPositionY();
			float a = (y2-y1)/(x2-x1);
			float b = y1 - (x1 * a);
			
			float droite = positionY - a*positionX -b;
			
			
			if(Math.round(droite) == 0){
				return arc;
			}
			
			float c2 = a * positionX + b;
			
			
			float c1 = positionX;
			
			float circle = (positionX - c1) * (positionX - c1) + (positionY - c2) * (positionY - c2);
			if (circle <= 3 * 3)
			{
				return arc;
			}
			else{
				c1 = (positionY - b)/a;
				c2 = positionY;
				circle = (positionX-c1)*(positionX-c1) + (positionY-c2)*(positionY-c2);
				if(circle <= 3 * 3){
					return arc;
				}
			}
			
		}
		return null;
	}
	
	public void enleverArc(Arc arc)
	{
		m_carte.enleverArc(arc);
	}
	
	public boolean existeComponent(int positionX, int positionY) 
	{
		if (this.reqNoeud(positionX, positionY) != null
				|| this.reqArc(positionX, positionY) != null) 
		{
			return true;
		}

		else 
		{
			return false;
		}
	}
	
	
	public String reqPositionString(int posX, int posY) 
	{
		Position position = m_grille
				.reqPositionEnMetre(new Position(posX, posY));
		int positionX = Math.round(position.reqPositionX());
		int positionY = Math.round(position.reqPositionY());

		if (positionX / 1000 > 1 || positionY / 1000 > 1) {
			return "<html>Abscisse : " + positionX / 1000

					+ "Km<br/> Ordonn�e : " + positionY / 1000 + "Km</html>";
		}
		return "<html>Abscisse : " + positionX + "m<br/> Ordonn�e : "

				+ positionY + "m</html>";
	}
	
	public Grille reqGrille() 
	{
		return m_grille;
	}
	
	public float reqZoom() 
	{
		return m_zoom.reqZoom();
	}
	
	public String augmenteZoom() 
	{
		return (int) (m_zoom.augmenteZoom() * 100) + "%";
	}

	public String diminueZoom() 
	{
		return (int) (m_zoom.diminueZoom() * 100) + "%";
	}
	
	public void toggleGrille()
	{
		m_grille.toggle();
	}
	
	public boolean isGrilleActive()
	{
		return m_grille.isActive();
	}

	public Position reqPositionEnPixel(Position positionEnMetre) {
			return m_grille.reqPositionEnPixel(positionEnMetre);

	}

	public float reqPixelParStep() {
		
		return m_echelle.reqPixelParStep();
	}
	
	public Image reqImageDeFond() {
		return this.m_imageDeFond;
	}
	private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        if(m_imageDeFond != null)
        {
        	out.writeInt(2); 
        	ImageIO.write((BufferedImage)m_imageDeFond, "png", out);
        }
        else
        {
        	out.writeInt(1);
        }
     
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if(in.readInt() == 2)
          m_imageDeFond =(Image)ImageIO.read(in);
    
        }
   
}
