/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.video.filerenaming;

import java.util.Collection;
import net.landora.video.info.MovieMetadata;

/**
 *
 * @author bdickie
 */
public class TestMovieMetadata extends TestMetadata implements MovieMetadata {

    public TestMovieMetadata(boolean adult) {
        super(adult);
    }

    public TestMovieMetadata() {
        this(false);
    }

    @Override
    public String getMovieName() {
        return (adult ? "Test Adult Move" : "Test Movie");
    }

    @Override
    protected void addContentObjectsImpl(Collection<Object> addTo) {
        
    }
    
}
