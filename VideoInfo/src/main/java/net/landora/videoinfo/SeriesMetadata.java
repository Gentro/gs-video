/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.videoinfo;

/**
 *
 * @author bdickie
 */
public interface SeriesMetadata extends VideoMetadata {
    
    public String getSeriesName();
    public String getEpisodeName();
    public String getEpisodeNumber();
    
    
}