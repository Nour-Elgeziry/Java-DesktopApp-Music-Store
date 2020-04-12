/*
 * Written by Nourelrahman Elgeziry  SID: 7593776
 */
package sqlitechinookcw;

import java.io.Serializable;

/**
 *This class is used to determine which object to send either tracks or genres. It contains both of them using composition and implements Serializable
 * @author nour
 */
public class Parcel implements Serializable{

    private Track track; // composed track object
    private Genre genre; //composed genre object
    private boolean isTrack;// variable to determine which object is used

    // default constructor
    public Parcel(Track track, Genre genre, boolean isTrack) {

        this.track = track;
        this.genre = genre;
        this.isTrack= isTrack;
    }
    //used to access track obect
    public Track getTrack() {

        return track;
    }
    // used to access genre object
    public Genre getGenre() {

        return genre;
    }
    // used to get value to determine which object is used
    public boolean getIsTrack(){
    return isTrack;
    }
    // used to set value to determine which object is used
    public void setIsTrack(boolean isTrack){
    this.isTrack = isTrack;
    }

}
