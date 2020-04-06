/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlitechinookcw;

import java.io.Serializable;

/**
 *
 * @author nour
 */
public class Parcel implements Serializable{

    private Track track;
    private Genre genre;
    private boolean isTrack;

    public Parcel(Track track, Genre genre, boolean isTrack) {

        this.track = track;
        this.genre = genre;
        this.isTrack= isTrack;
    }

    public Track getTrack() {

        return track;
    }

    public Genre getGenre() {

        return genre;
    }
    
    public boolean getIsTrack(){
    return isTrack;
    }
    
    public void setIsTrack(boolean isTrack){
    this.isTrack = isTrack;
    }

}
