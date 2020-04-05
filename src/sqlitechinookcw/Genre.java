/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlitechinookcw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author nour
 */
public class Genre implements Serializable {
    public String distinguish;
    private int genreId;
    private String name;
    
    public Genre(int genreId, String name){
    this.genreId = genreId;
    this.name = name;   
    }
    
    public Genre(String distinguish){
    this.distinguish = distinguish;
    }
    
    public int getGenreId() {
        return genreId;
    }
    
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
    
     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
     @Override
    public String toString() {
        return  String.valueOf(genreId) + " | "
                + name + "\n\r";
    }
    
    
    
}
