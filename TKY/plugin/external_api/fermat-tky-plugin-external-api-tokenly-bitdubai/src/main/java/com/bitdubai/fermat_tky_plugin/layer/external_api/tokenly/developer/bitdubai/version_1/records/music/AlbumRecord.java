package com.bitdubai.fermat_tky_plugin.layer.external_api.tokenly.developer.bitdubai.version_1.records.music;

import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.Album;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/03/16.
 */
public class AlbumRecord implements Album {

    String id;
    String name;
    String description;
    int songCount;

    /**
     * Constructor with parameters.
     * @param id
     * @param name
     * @param description
     * @param songCount
     */
    public AlbumRecord(String id, String name, String description, int songCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songCount = songCount;
    }

    /**
     * This method returns the Album id.
     * @return
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * This method returns the Album name.
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the Album description.
     * @return
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * This method returns the Album song count.
     * @return
     */
    @Override
    public int getSongCount() {
        return this.songCount;
    }

    @Override
    public String toString() {
        return "AlbumRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", songCount=" + songCount +
                '}';
    }
}
