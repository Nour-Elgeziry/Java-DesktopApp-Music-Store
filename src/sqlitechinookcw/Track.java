/*
 * Written by Nourelrahman Elgeziry  SID: 7593776
 */
package sqlitechinookcw;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class is used to initialise the track's constructors, getters , setters
 * and overriding the to string function
 *
 * @author nour
 *
 * /**
 * A 'track' is a very simple class which basically forms a container object for
 * moving related data around an application. Beans implement the Serializable
 * interface and have public properties to get and set the data. In this case we
 * are using this class to represent the data in the database.
 *
 * @author Chris Bass
 */
public class Track implements Serializable {

    private int trackId;
    private String name;
    private int albumId;
    private int mediaTypeId;
    private int genreId;
    private String composer;
    private int milliseconds;
    private int bytes;
    private double unitPrice;
    private String genreSearchName;
    private int upperBound;
    private int randomNumber;// previous variables used to store information relatedd to their names
    private boolean isEdit; // used to determine if edit function is intended
    private boolean isDelete; // used to determine if deletefunction is intended
    private boolean isDeleteName; // used to determine if delete using name function is intended
    private boolean isViewTracks; // used to determine if view tracks function is intended
    private boolean isSearch; // used to determine if search track by genre function is intended
    private boolean isRandom; // used to determine if random track function is intended
    private boolean isSetUpperBound;// used to determine if setting upper bound functionality is intended

    // used when adding/viewing track
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

    // used to edit tracks
    public Track(int trackId, String name, int albumId, int mediaTypeId, int genreId, String composer, int milliseconds, int bytes, double unitPrice, boolean isEdit) {
        this.trackId = trackId;
        this.name = name;
        this.albumId = albumId;
        this.mediaTypeId = mediaTypeId;
        this.genreId = genreId;
        this.composer = composer;
        this.milliseconds = milliseconds;
        this.bytes = bytes;
        this.unitPrice = unitPrice;
        this.isEdit = isEdit;

    }

    // used to view tracks
    public Track(boolean viewTracks) {
        this.isViewTracks = viewTracks;
    }

    // used to delete track using track name
    public Track(boolean isDelete, String name, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.name = name;
        this.isDeleteName = isDeleteName;
    }

    // used to delete track using track id
    public Track(boolean isDelete, int trackId, boolean isDeleteName) {
        this.isDelete = isDelete;
        this.trackId = trackId;
        this.isDeleteName = isDeleteName;
    }
    // used to seacrc track by genre
    public Track(String genreName, boolean isSearch) {
        this.genreSearchName = genreName;
        this.isSearch = isSearch;
    }
    // used to get random track
    public Track(boolean isRandom, int randomNumber) {
        this.isRandom = isRandom;
        this.randomNumber = randomNumber;
    }
    // used to set upper bound
    public Track(int upperBound, boolean isSetUpperBound) {
        this.isSetUpperBound = isSetUpperBound;
        this.upperBound = upperBound;
    }

    //following is a list of setters and getters
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

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Boolean getisDeleteName() {
        return isDeleteName;
    }

    public void setisDeleteName(Boolean isDeleteName) {
        this.isDeleteName = isDeleteName;
    }

    public Boolean getisDelete() {
        return isDelete;
    }

    public void setisDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getisViewTracks() {
        return isViewTracks;
    }

    public void setisViewTracks(boolean isViewTracks) {
        this.isViewTracks = isViewTracks;
    }

    public String getGenreSearchName() {
        return genreSearchName;
    }

    public void setGenreSearchName(String genreName) {
        this.genreSearchName = genreName;
    }

    public boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public boolean getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(boolean isRandom) {
        this.isRandom = isRandom;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public boolean getIsSetUpperBound() {
        return isSetUpperBound;
    }

    public void setIsSetUpperBound(boolean isSetUpperBound) {
        this.isSetUpperBound = isSetUpperBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
// overriding tostring function to display to user
    @Override
    public String toString() {
        return String.valueOf(trackId) + " | "
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
