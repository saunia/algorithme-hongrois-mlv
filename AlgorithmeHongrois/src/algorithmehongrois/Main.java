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
        algo.trouverAffectationOptimale();
    }

}
