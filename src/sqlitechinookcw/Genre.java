/*
 
 *Written by Nourelrahman Elgeziry  SID: 7593776
 */
package sqlitechinookcw;

import java.io.Serializable;

/**
 * this class is used to initialise the genre 's constructors, getters , setters
 * and overriding the to string function
 *
 * @author nour
 *
 * /**
 * A 'Genre' is a very simple class which basically forms a container object for
 * moving related data around an application. Beans implement the Serializable
 * interface and have public properties to get and set the data. In this case we
 * are using this class to represent the data in the database.
 *
 * @author Chris Bass
 */
public class Genre implements Serializable {

    private int genreId; // stoes genre id
    private String name; // stores genre name
    private boolean isViewGenre;// stores boolean used to determine if view genre function intended
    private boolean isEdit;// stores boolean used to determine if edit genre function intended
    private boolean isDelete;// stores boolean used to determine ifdelete genre function intended
    private boolean isDeleteName;// stores boolean used to determine if delete by name genre function intended

//constructor used when viewing genre
    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    //constructor used when editingg genre

    public Genre(int genreId, String name, boolean isEdit) {
        this.genreId = genreId;
        this.name = name;
        this.isEdit = isEdit;
    }

    // used to determine if view genre functionality intended

    public Genre(boolean isViewGenre) {
        this.isViewGenre = isViewGenre;
    }

    // used to delete track using track name
    public Genre(boolean isDelete, String name, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.name = name;
        this.isDeleteName = isDeleteName;
    }

    // used to delete track using trackid

    public Genre(boolean isDelete, int genreId, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.genreId = genreId;
        this.isDeleteName = isDeleteName;
    }

//following is a list of getters and setter
    public int getGenreId() {
        return genreId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsViewGenre() {
        return isViewGenre;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public boolean getIsDeleteName() {
        return isDeleteName;
    }

// overiding the tostring function to output to the user
    @Override
    public String toString() {
        return String.valueOf(genreId) + " | "
                + name + "\n\r";
    }

}
