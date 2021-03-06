/**
 * Copyright (C) 2012-2014 Blake Dickie
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.landora.video.actions;

import net.landora.video.filestate.data.FileRecord;
import net.landora.video.filestate.data.LocalPathManager;
import net.landora.video.filestate.data.SharedDirectory;
import net.landora.video.filestate.data.SharedDirectoryDBA;
import net.landora.video.info.MetadataProvidersManager;
import net.landora.video.info.VideoMetadata;
import net.landora.video.info.ViewListState;
import net.landora.video.info.file.FileInfo;
import net.landora.video.info.file.FileInfoManager;
import net.landora.video.info.file.FileManager;
import net.landora.video.ui.UIAction;
import net.landora.video.utils.Touple;
import net.landora.video.utils.UIUtils;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author bdickie
 */
public class DeleteAndRemoveListFile extends UIAction<File> {

    public DeleteAndRemoveListFile() {
        super( File.class, "Delete File & Remove List", true );
    }

    @Override
    public void actionPerformed( ActionEvent evt, List<File> objects ) {
        int reply = JOptionPane.showConfirmDialog( UIUtils.getActiveWindow(),
                                                   "Are you sure you wish to delete " + objects.size() + " file(s)?",
                                                   "Delete File", JOptionPane.YES_NO_OPTION );
        if ( reply != JOptionPane.YES_OPTION ) {
            return;
        }

        LocalPathManager pathMgr = LocalPathManager.getInstance();

        for ( File file : objects ) {
            FileInfo info = FileInfoManager.getInstance().getFileInfo( file, false );
            VideoMetadata md = MetadataProvidersManager.getInstance().getMetadata( info );
            ViewListState viewListState = md.getListManager().getViewListState( md );
            if ( viewListState != null ) {
                viewListState.remove();
            }
            FileManager.getInstance().deleteFile( file );

            Touple<SharedDirectory, String> subPath = pathMgr.findSubPath( file );
            if ( subPath != null ) {
                FileRecord fileRecord = SharedDirectoryDBA.getFileRecord( subPath.getFirst(), subPath.getSecond() );
                if ( fileRecord != null ) {
                    SharedDirectoryDBA.deleteFileRecord( fileRecord );
                }
            }
        }
    }

}
