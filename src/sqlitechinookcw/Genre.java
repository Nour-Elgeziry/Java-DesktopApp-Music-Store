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

    private int genreId;
    private String name;
    private boolean isViewGenre;
    private boolean isEdit;
    private boolean isDelete;
    private boolean isDeleteName;

    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }
    
     public Genre(int genreId, String name, boolean isEdit) {
        this.genreId = genreId;
        this.name = name;
        this.isEdit = isEdit;
    }

    public Genre(boolean viewGenre) {
        this.isViewGenre = viewGenre;
    }
    
     // used to delete track using track name
    public Genre(boolean isDelete,String name, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.name = name;
        this.isDeleteName = isDeleteName;
    }
    
     public Genre(boolean isDelete,int genreId, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.genreId = genreId;
        this.isDeleteName = isDeleteName;
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

    public boolean getIsViewGenre() {
        return isViewGenre;
    }

    public void setGenre(boolean setGenre) {
        this.isViewGenre = setGenre;
    }
    
    public boolean getIsEdit(){
        return isEdit;
    }
    
    public void setIsEdit(boolean isEdit){
    this.isEdit = isEdit;
    }
    
    public boolean getIsDelete(){
        return isDelete;
    }
    
    public void setIsDelete(boolean isDelete){
    this.isDelete = isDelete;
    }
    
    public boolean getIsDeleteName(){
        return isDeleteName;
    }
    
    public void setIsDeleteName(boolean isDeleteName){
    this.isDeleteName = isDeleteName;
    }

    @Override
    public String toString() {
        return String.valueOf(genreId) + " | "
                + name + "\n\r";
    }

}
