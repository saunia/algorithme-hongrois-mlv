/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithmehongrois.Controleur;

/**
 *
 * @author Didier
 */
public class Couple {

    //test svn
    
    private String itemLigne;
    private String itemColonne;

    public Couple(){

    }

    public Couple(String itemLigne, String itemColonne){
        this.itemColonne = itemColonne;
        this.itemLigne = itemLigne;
    }

    public String getItemColonne() {
        return itemColonne;
    }

    public void setItemColonne(String itemColonne) {
        this.itemColonne = itemColonne;
    }

    public String getItemLigne() {
        return itemLigne;
    }

    public void setItemLigne(String itemLigne) {
        this.itemLigne = itemLigne;
    }

}
