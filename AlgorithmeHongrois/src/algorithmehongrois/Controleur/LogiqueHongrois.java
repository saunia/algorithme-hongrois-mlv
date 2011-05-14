/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithmehongrois.Controleur;

import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author dverstraete
 */
public class LogiqueHongrois
{

    private Integer[][] matrice;
    private Integer[][] matriceBuffer;
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
        afficheMatriceBuffer();
        reduireColonnes();
        afficheMatriceBuffer();
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
