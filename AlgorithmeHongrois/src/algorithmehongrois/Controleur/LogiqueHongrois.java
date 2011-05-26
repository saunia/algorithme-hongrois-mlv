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
    private int dimension;
    private Integer[] indicesColonnesBarrees;
    private Integer[] indicesLignesBarrees;
    private int cptLigne=0;
    private int cptColonne=0;


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

    public LogiqueHongrois(int dim, boolean preference)
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

        indicesColonnesBarrees = new Integer[dim];
        indicesLignesBarrees = new Integer[dim];


    }

    public LogiqueHongrois(Integer[][] matrice, boolean preference)
    {
        this.matrice = matrice;

        int dim = matrice.length; 
        this.dimension = dim; 

        //copie la matrice dans une matrice buffer
        this.matriceBuffer = new Integer[dim][dim];        
        for(int i=0; i < dim; i++){
            this.matriceBuffer[i] = (Integer[]) matrice[i].clone();
        }

        //si la preference est croissante on passe la matrice en preference decroissante
        if(preference){
            ordreDecroissant();
        }

        indicesColonnesBarrees = new Integer[dim];
        indicesLignesBarrees = new Integer[dim];
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

        System.out.println("APRES REDUCTION");

        for (int i= 0; i < dimension; i++) {
            for (int j= 0; j < dimension; j++) {
                System.out.print(matriceBuffer[i][j] + " ");
            }
            System.out.print("\n");
        }

        while(!estOptimale()){
            marquerLignesColonnes();
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
        System.out.println("OPTIMALE !");

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

        System.out.println("OPTI" + estOptimale);
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

        System.out.println("SOLUTION");

        for (int i= 0; i < this.dimension; i++) {
            for (int j= 0; j < this.dimension; j++) {
                System.out.print(matrice[i][j] + " ");
            }

            System.out.print("\n");

        }

        return solution;
    }

    // Si c'est pas optimale
    public void marquerLignesColonnes()
    {

        System.out.print("MARQUER !");

        /*this.indicesColonnesBarrees=null;//Init nécessaire? Purge !
        this.indicesLignesBarrees=null;//Init nécessaire? Purge !
        int cpt=0;

        //Travail sur la matrice courante (matrice)

        //Parcours lignes (i=lignes)
        for (int i=0;i<dimension;i++) //Inf ou égal selon l'architecture de la matrice
        {
            //Parcours colonnes (j=colonnes)
            for(int j=0;j<dimension;j++) //Inf ou égal selon l'architecture de la matrice
                //if(this.matriceBuffer[i][j]==0) EN CAS DE CHANGEMENT!
                if (this.matriceBuffer[i][j]==0)
                {
                    //stockage de l'indice dans le tableau d'indice lignes
                    this.indicesLignesBarrees[cpt]=i;
                    //stockage de l'indice dans le tableau d'indice colonnes
                    this.indicesColonnesBarrees[cpt]=j;
                    //Incrémentation du compteur d'indexation pour les tableaux contenant les indices des 0 la matrice
                    cpt++;
                }
        }*/
        Integer[][] temp=matriceBuffer;
        boolean ligne=true;
        boolean firstTime=true;
        int tabNbZeroLigne[]=null;
        int tabNbZeroColonne[]=null;
        int indiceBarre=0;
        
        while(gotZero(temp))
        {
            tabNbZeroLigne=new int[dimension];
            tabNbZeroColonne=new int[dimension];
            //comptage zero des lignes
            for(int i=0;i<dimension;i++)
            {
                int comptage=0;
                for(int j=0;j<dimension;j++)
                {                    
                    if(temp[i][j]==0)
                    {
                        comptage++;
                    }                    
                }
                tabNbZeroLigne[i]=comptage;
            }
            
            //comptage zero des colonnes
            for(int i=0;i<dimension;i++)
            {
                int comptage=0;
                for(int j=0;j<dimension;j++)
                {                    
                    if(temp[j][i]==0)
                    {
                        comptage++;
                    }                    
                }
                tabNbZeroColonne[i]=comptage;
            }

            if(firstTime)
            {
              int valMax=0;
              for(int i=0;i<dimension;i++)
              {
                  if(tabNbZeroColonne[i]>=valMax)
                  {
                      valMax=tabNbZeroColonne[i];
                      indiceBarre=i;
                      ligne=false;
                  }
                  if(tabNbZeroLigne[i]>=valMax)
                  {
                      valMax=tabNbZeroLigne[i];
                      indiceBarre=i;
                      ligne=true;
                  }                     
              }
              temp=rayer(ligne,indiceBarre,temp);
              firstTime=false;          
            }
            else
            {
                int valMax=0;
                if(ligne)
                {
                   for(int i=0;i<dimension;i++)
                    {
                      if(tabNbZeroColonne[i]>=valMax)
                      {
                          valMax=tabNbZeroColonne[i];
                          indiceBarre=i;
                          ligne=false;
                      }
                    }
                }
                else
                {
                    for(int i=0;i<dimension;i++)
                    {
                      if(tabNbZeroLigne[i]>=valMax)
                      {
                          valMax=tabNbZeroLigne[i];
                          indiceBarre=i;
                          ligne=true;
                      }
                    }
                }
                temp=rayer(ligne,indiceBarre,temp);
            }
        }

        matriceBuffer = temp;

        // TEST
        System.out.println("TEST !");

        for (int i= 0; i < temp.length; i++) {
            for (int j= 0; j < temp.length; j++) {
                System.out.print(temp[i][j] + " ");
            }
            System.out.print("\n");

        }

        System.out.println("Lignes : ");
        for (int i= 0; i < indicesLignesBarrees.length; i++) {
            System.out.println(String.valueOf(indicesLignesBarrees[i]) + " ");

        }

        System.out.println("\nColonnes : ");
        for (int i = 0; i < indicesColonnesBarrees.length; i++) {
            System.out.println(String.valueOf(indicesColonnesBarrees[i]) + " ");

        }
    }
    
    public Integer[][] rayer(boolean ligne,int indiceBarre, Integer temp[][])
    {
        if(ligne)
        {
            for(int i=0;i<dimension;i++)
            {
                if(temp[indiceBarre][i]==0)
                {
                    temp[indiceBarre][i]=-1;
                }
            } 
            indicesLignesBarrees[cptLigne]=indiceBarre;
            cptLigne++;

        }
        else
        {
            for(int i=0;i<dimension;i++)
            {
                if(temp[i][indiceBarre]==0)
                {
                    temp[i][indiceBarre]=-1;
                }
            }
            indicesColonnesBarrees[cptColonne]=indiceBarre;
            cptColonne++;
        }

        

        return temp;
    }

    public void ajouterOuSoustraire()
    {
         int min = 9999999;

        //trouver la cellule non barrée de valeur minimum
        for(int ligne = 0; ligne<dimension; ligne++){
            for(int colonne = 0; colonne<dimension; colonne++){
                if(!isLigneBarree(ligne) && !isColonneBarree(colonne)){
                    if(this.matriceBuffer[ligne][colonne]<min){
                        min = this.matriceBuffer[ligne][colonne];
                    }
                }
            }
        }

        //additionner min au intersections de lignes et colonnes
        for(int ligne : this.indicesLignesBarrees){
            for(int colonne : this.indicesColonnesBarrees){
                this.matriceBuffer[ligne][colonne] += min;
            }
        }

        //soustraire min aux valeurs non couvertes
        for(int ligne = 0; ligne<dimension; ligne++){
            for(int colonne = 0; colonne<dimension; colonne++){
                if(!isLigneBarree(ligne) && !isColonneBarree(colonne)){
                    this.matriceBuffer[ligne][colonne] -= min;
                }
            }
        }
    }

    /**
     * Retourne vrai si ligne est une ligne barree, faux sinon
     * @param ligne
     * @return
     */
    public boolean isLigneBarree(int ligne){
        int i=0;
        while(i<this.indicesLignesBarrees.length && this.indicesLignesBarrees[i] != ligne){
            i++;
        }
        return i<this.indicesLignesBarrees.length;
    }

    /**
     * Retourne vrai si colonnes est une colonne barree, faux sinon
     * @param colonne
     * @return
     */
    public boolean isColonneBarree(int colonne){
        int i=0;
        while(i<this.indicesColonnesBarrees.length && this.indicesColonnesBarrees[i] != colonne){
            i++;
        }
        return i<this.indicesColonnesBarrees.length;
    }

    public void afficheMatrice(){
        for(int ligne = 0; ligne<dimension; ligne++){
            for(int colonne = 0; colonne<dimension; colonne++){
                System.out.print("|" + matrice[ligne][colonne] + "|");
            }
            System.out.print("\n");
        }
    }
    
    public boolean gotZero (Integer[][] temp)
    {
        boolean retour=false;
        for(int i=0;i<dimension;i++)
        {
            for(int j=0;j<dimension;j++)
            {
                if (temp[i][j]==0)
                {
                    retour=true;
                    return retour;
                }
            }
        }
        return retour;
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
        boolean estEncadre = false;

        for (int i= 0; i < this.dimension; i++) {
            if(matrice[numLigne][i] == 0 && !estEncadre)
            {
                matrice[numLigne][i] = -1;

                for (int j= 0; j < this.dimension; j++) {
                    if(matrice[j][i] != -1)
                        matrice[j][i] = -2;
                }

                estEncadre = true; 
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

     /**
     * Transforme matrice par ordre de préférence croissant en ordre de préférence décroissant
     */
    public void ordreDecroissant(){
        int min = valMin(this.matriceBuffer);
        int max = valMax(this.matriceBuffer);
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                matriceBuffer[i][j] = (matriceBuffer[i][j] - (max + min)) * (-1);
            }
        }

    }

    /**
     * Retourne la valeur minimale d'une matrice
     * @param matrice
     * @return
     */
    public int valMin(Integer[][] matrice){
        int min = matrice[0][0];
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                if(matrice[i][j]<min){
                    min = matrice[i][j];
                }
            }
        }
        return min;
    }

    /**
     * Retourne la valeur maximale d'une matrice
     * @param matrice
     * @return
     */
    public int valMax(Integer[][] matrice){
        int max = matrice[0][0];
        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                if(matrice[i][j]>max){
                    max = matrice[i][j];
                }
            }
        }
        return max;
    }
}
