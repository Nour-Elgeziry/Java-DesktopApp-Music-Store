/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlitechinookcw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A 'Bean' is a very simple class which basically forms a container object for
 * moving related data around an application. Beans implement the Serializable
 * interface and have public properties to get and set the data. In this case we
 * are using this class to represent the data in the database.
 *
 * @author Chris Bass
 */
public class Track implements Serializable {
    public int deleteUnique;
    public String distinguish;
    public  int trackId;
    public String name;
    public int albumId;
    public int mediaTypeId;
    public int genreId;
    public String composer;
    public int milliseconds;
    public int bytes;
    public double unitPrice;

    public Track(int trackId, String name, int albumId, int mediaTypeId, int genreId, String composer, int milliseconds, int bytes, double unitPrice) {
        this.trackId = trackId;
        this.name = name;
        this.albumId = albumId;
        this.mediaTypeId = mediaTypeId;
        this.genreId = genreId;
        this.composer = composer;
        this.milliseconds = milliseconds;
        this.bytes = bytes;
        this.unitPrice = unitPrice;
    }
    
    // used to 
    public Track(String distinguish){
    this.distinguish = distinguish;
    }
    // used to delete track using track name
    public Track(String name, int unique){
    this.name = name;
    this.deleteUnique = unique;
    }
    // used to delete track using track id
    public Track(int trackId, int unique){
    this.trackId = trackId;
    this.deleteUnique = unique;
    }
    
   
    

    public int getTrackId() {
        return trackId;
    }
    
    

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(int mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    

    @Override
    public String toString() {
        return  String.valueOf(trackId) + " | "
                + name + " | "
                + String.valueOf(albumId) + " | "
                + String.valueOf(mediaTypeId) + " | "
                + String.valueOf(genreId) + " | "
                + composer + " | "
                + String.valueOf(milliseconds) + " | "
                + String.valueOf(bytes) + " | "
                + String.valueOf(unitPrice) + "\n\r";
    }

}
