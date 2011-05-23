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
        solution = trouverSolution();
        return solution;
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

    public ArrayList<Couple> trouverSolution()
    {
        int cpt = 0;
        int numLigne;

        Integer[][] matrice = new Integer[this.dimension][this.dimension];

        for(int i=0; i < this.dimension; i++){
            matrice[i] = (Integer[]) this.matriceBuffer[i].clone();
        }

        while(cpt != this.dimension)
        {
            numLigne = ligneMinZeros(matrice);
            matrice = barrerEncadrerZeros(numLigne, matrice);
            ++cpt;
        }

        ArrayList<Couple> solution = enregistrerSolution(matrice);

        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                System.out.print(matrice[i][j] + " ");
            }

            System.out.print("\n");

        }

        return solution;
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

    /**
     * Retourne la ligne de la matrice ayant le moins de zéros
     * @param matrice La matrice à tester
     * @return Le numéro de la ligne
     */
    private int ligneMinZeros(Integer[][] matrice) {
        int zeros = 0;
        int nbZerosMin = this.dimension;
        int numLigne = 0;

        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(matrice[i][j] == 0)
                {
                    ++zeros;
                }
            }

            if(nbZerosMin > zeros && zeros != 0)
            {
                nbZerosMin = zeros;
                numLigne = i;
            }

            zeros = 0;
        }

        return numLigne;
    }

    /**
     * Encadre les zéros et barre les zéros inutiles
     * @param numLigne Ligne à traiter
     * @param matrice Matrice à traiter
     * @return La matrice modifiée
     */
    private Integer[][] barrerEncadrerZeros(int numLigne, Integer[][] matrice) {
        for (int i= 0; i < this.dimension; i++) {
            if(matrice[numLigne][i] == 0)
            {
                matrice[numLigne][i] = -1;

                for (int j= 0; j < this.dimension; j++) {
                    if(matrice[j][i] != -1)
                        matrice[j][i] = -2;
                }
            }
            else
            {
                matrice[numLigne][i] = -2;
            }
        }

        return matrice;
    }

    private ArrayList<Couple> enregistrerSolution(Integer[][] matrice) {
        ArrayList<Couple> solution = new ArrayList<Couple>();

        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                if(matrice[i][j] == -1)
                {
                    solution.add(new Couple(String.valueOf(i), String.valueOf(j)));
                }
            }
        }

        return solution;
    }

}
