/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithmehongrois;

import algorithmehongrois.Controleur.LogiqueHongrois;

/**
 *
 * @author dverstraete
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

                //JTABLE
//        String[] colonnes= new String[dimension];
//        for (int i=0;i<dimension;i++)
//        {
//            colonnes[i]=("Col : "+i);
//        }
//        JTable table = new JTable(matrice, colonnes);

        
        LogiqueHongrois algo = new LogiqueHongrois(3);
        // algo.trouverAffectationOptimale();

        int dim = 3;
        Integer[][] matriceBuffer = new Integer[dim][dim];
        matriceBuffer[0][0] = 0;
        matriceBuffer[0][1] = 0;
        matriceBuffer[0][2] = 1;
        matriceBuffer[1][0] = 0;
        matriceBuffer[1][1] = 1;
        matriceBuffer[1][2] = 0;
        matriceBuffer[2][0] = 1; // Si 0 alors c'est optimal
        matriceBuffer[2][1] = 0;
        matriceBuffer[2][2] = 1;

        algo.setMatriceBuffer(matriceBuffer);
        System.out.println(algo.estOptimale());

        // algo.trouverSolution();
        algo.trouverAffectationOptimale(); 
    }

}
