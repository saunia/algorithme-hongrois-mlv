/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithmehongrois.Controleur;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author dverstraete
 */
public class LogiqueHongrois
{

    private Integer[][] matrice;
    private Integer[][] matriceBuffer;
    private Integer[] indicesColonnesBarrees;
    private Integer[] indicesLignesBarrees;
    private int dimension;


    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Integer[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(Integer[][] matrice) {
        this.matrice = matrice;
    }

    public Integer[][] getMatriceBuffer() {
        return matriceBuffer;
    }

    public void setMatriceBuffer(Integer[][] matriceBuffer) {
        this.matriceBuffer = matriceBuffer;
    }
    

    public LogiqueHongrois()
    {
        matrice=null;
        matriceBuffer=null;
        dimension=0;
    }

    public LogiqueHongrois(int dim)
    {
        this.dimension = dim;
        this.matrice = new Integer[dimension][dimension];

        
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                matrice[i][j] = i+j;
            }
        }

        //copie la matrice dans une matrice buffer
        this.matriceBuffer = new Integer[dim][dim];
        for(int i=0; i < dim; i++){
            this.matriceBuffer[i] = (Integer[]) matrice[i].clone();
        }
    }

    public LogiqueHongrois(Integer[][] matrice)
    {
        this.matrice = matrice;

        int dim = matrice.length; 
        this.dimension = dim; 

        //copie la matrice dans une matrice buffer
        this.matriceBuffer = new Integer[dim][dim];
        for(int i=0; i < dim; i++){
            this.matriceBuffer[i] = (Integer[]) matrice[i].clone();
        }
    }

    /**
     * L'algorithme fonctionne en mode décroissant,
     * la plus petite valeur est la plus forte
     * @return
     */
    public ArrayList<Couple> trouverAffectationOptimale(){
        //couples solution du problème
        ArrayList<Couple> solution = new ArrayList<Couple>();
        //TODO : inverserModeDécroissant
        reduireLignes();
        reduireColonnes();
        while(!estOptimale()){
            rayerLignesColonnes();
            ajouterOuSoustraire();
        }
        trouverSolution();
        return null;
    }

    private void reduireLignes(){
        for(int ligne = 0; ligne<dimension; ligne++){

            //recherche du minimum de la ligne
            int min = matriceBuffer[ligne][0];
            for(int i=0; i<dimension; i++){
                if(matriceBuffer[ligne][i]<min){
                    min = matriceBuffer[ligne][i];
                }
            }
            System.out.println("Ligne : " + ligne + " réduite de " + min);

            for(int colonne = 0; colonne<dimension; colonne++){
                matriceBuffer[ligne][colonne] -= min;
            }
        }
    }

    private void reduireColonnes(){
        for(int colonne = 0; colonne<dimension; colonne++){

            //recherche du minimum de la colonne
            int min = matriceBuffer[0][colonne];
            for(int i=0; i<dimension; i++){
                if(matriceBuffer[i][colonne]<min){
                    min = matriceBuffer[i][colonne];
                }
            }
            
            System.out.println("Colonne : " + colonne + " réduite de " + min);
            
            for(int ligne = 0; ligne<dimension; ligne++){
                matriceBuffer[ligne][colonne] -= min;
            }
        }
    }

    /**
     * Teste si la matrice buffer est optimale
     * @return true si la matrice buffer est optimale
     */
    public boolean estOptimale()
    {
        List<Integer> listIndicesI = new ArrayList<Integer>();
        List<Integer> listIndicesJ = new ArrayList<Integer>();
        boolean estOptimale = false;

        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(this.matriceBuffer[i][j] == 0)
                {
                    if(!listIndicesI.contains(i))
                    {
                        listIndicesI.add(i);
                    }

                    if(!listIndicesJ.contains(j))
                    {
                        listIndicesJ.add(j);
                    }
                }
            }
        }

        if(listIndicesI.size() == this.dimension && listIndicesJ.size() == this.dimension)
            estOptimale = true;

        return estOptimale;
    }

    public void trouverSolution()
    {
        Integer[][] matriceResultat = new Integer[this.dimension][this.dimension];

        // Remplissage de la matrice avec que des zéros
        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                matriceResultat[i][j] = 0;
            }
        }

        // Trouver la ligne ou la colonne avec le moins de zéros possibles
        int nbZeros = 0;
        int nbZerosMini = this.dimension;
        int numLigne = this.dimension;
        int numColonne= this.dimension;

        // Parcours ligne par ligne
        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(this.matriceBuffer[i][j] == 0)
                {
                    ++nbZeros;
                }
            }

            if(nbZerosMini > nbZeros)
            {
                nbZerosMini = nbZeros;
                numLigne = i;
            }

            nbZeros = 0;
        }

        // Parcours colonne par colonne
        if(nbZerosMini != 1) // Sinon, ligne trouvée
        {
            for (int i= 0; i < this.dimension; i++) {
                for (int j= 0; j < this.dimension; j++) {
                    if(this.matriceBuffer[j][i] == 0) // ATTENTION, variables inversées !!!
                    {
                        ++nbZeros;
                    }
                }

                if(nbZerosMini > nbZeros)
                {
                    nbZerosMini = nbZeros;
                    numColonne = i;
                }

                nbZeros = 0;
            }
        }

        int colonneZero;

        if(numColonne == this.dimension) // Si c'est une ligne qui est retenue
        {
            // Trouver la colonne où il y a un zéro
            for (int i= 0; i < this.dimension; i++) {
                if(this.matriceBuffer[numLigne][i] == 0)
                {
                    colonneZero = i;
                    i = this.dimension; // Sortie du for i
                }
            }
        }

        /*
        List<List<Couple>> listeCouplesPossibles = new ArrayList<List<Couple>>();
        List<Couple> listCourante = new ArrayList<Couple>();
        List<Couple> solution = new ArrayList<Couple>();

        // On écrit tous les couples possibles
        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(this.matriceBuffer[i][j] == 0)
                {
                    listCourante.add(new Couple(String.valueOf(i), String.valueOf(j)));
                }
            }

            listeCouplesPossibles.add(listCourante);
            listCourante.clear();
        }

        // Recherche de la liste la plus petite
        int tailleListeMini = this.dimension;
        int numListeMini = this.dimension;

        for (int i= 0; i < listeCouplesPossibles.size(); i++) {
            if(listeCouplesPossibles.get(i).size() < tailleListeMini)
            {
                tailleListeMini = listeCouplesPossibles.get(i).size();
                numListeMini = i;
            }
        }

        // On ajoute le premier élément de cette liste dans la solution
        solution.add(listeCouplesPossibles.get(numListeMini).get(0));

        // On supprime tous les couples possibles de cette ligne et de cette colonne

        */

        /*
        int nbZeros = 0; 
        int nbZerosMini = this.dimension; 
        int numLigne = this.dimension;
        int numColonne= this.dimension;
        
        // Trouver la ligne ou la colonne avec le moins de zéros possibles
        // Parcours ligne par ligne
        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(this.matriceBuffer[i][j] == 0)
                {
                    ++nbZeros;
                }
            }

            if(nbZerosMini > nbZeros)
            {
                nbZerosMini = nbZeros;
                numLigne = i;
            }

            nbZeros = 0; 
        }
        
        // Parcours colonne par colonne
        if(nbZerosMini != 1) // Sinon, ligne trouvée
        {
            for (int i= 0; i < this.dimension; i++) {
                for (int j= 0; j < this.dimension; j++) {
                    if(this.matriceBuffer[j][i] == 0) // ATTENTION, variables inversées !!!
                    {
                        ++nbZeros;
                    }
                }

                if(nbZerosMini > nbZeros)
                {
                    nbZerosMini = nbZeros;
                    numColonne = i;
                }

                nbZeros = 0;
            }
        }

        if(numColonne == this.dimension) // Si c'est une ligne qui est retenue
        {

        }
         *
         *
         */
    }

    // Si c'est pas optimale
    public void rayerLignesColonnes()
    {
        // Alban
    }

    public void ajouterOuSoustraire()
    {
        // Didier
    }

    public void afficheMatrice(){
        for(int ligne = 0; ligne<dimension; ligne++){
            for(int colonne = 0; colonne<dimension; colonne++){
                System.out.print("|" + matrice[ligne][colonne] + "|");
            }
            System.out.print("\n");
        }
    }

    public void afficheMatriceBuffer(){
        for(int ligne = 0; ligne<dimension; ligne++){
            for(int colonne = 0; colonne<dimension; colonne++){
                System.out.print("|" + matriceBuffer[ligne][colonne] + "|");
            }
            System.out.print("\n");
        }
    }

}
