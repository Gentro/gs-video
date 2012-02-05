/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.videofilebrowser;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.landora.filestate.data.LocalPathManager;
import net.landora.filestate.data.SharedDirectory;
import net.landora.gsuiutils.ComparisionUtils;
import net.landora.gsuiutils.ExplorerManagerWrapper;
import net.landora.gsuiutils.UIUtils;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.landora.videofilebrowser//FileBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "FileBrowserTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "net.landora.videofilebrowser.FileBrowserTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_FileBrowserAction",
preferredID = "FileBrowserTopComponent")
public final class FileBrowserTopComponent extends TopComponent {

    public FileBrowserTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(FileBrowserTopComponent.class, "CTL_FileBrowserTopComponent"));
        setToolTipText(NbBundle.getMessage(FileBrowserTopComponent.class, "HINT_FileBrowserTopComponent"));

        
        
        view = new BeanTreeView();
        view.setRootVisible(false);
        view.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnlFolders.add(new ExplorerManagerWrapper(view, folderManager), BorderLayout.CENTER);
        folderLookup = ExplorerUtils.createLookup(folderManager, getActionMap());
        
        List<Node> nodes = new ArrayList<Node>();
        for(SharedDirectory dir: LocalPathManager.getInstance().getAvaliableLocalPaths()) {
            nodes.add(FSNodeFactory.createNode(LocalPathManager.getInstance().getLocalPath(dir), dir.getName()));
        }
        
        AbstractNode node = new AbstractNode(new Children.Array());
        node.getChildren().add(nodes.toArray(new Node[nodes.size()]));
        
        
        folderManager.setRootContext(node);
        
        folderLookupResult = folderLookup.lookupResult(File.class);
        
        
        fileModel = new FileTableModel();
        tblFiles.setModel(fileModel);
        
        
        associateLookup(new AbstractLookup(fileContext));
        
        tblFiles.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tblFilesSelectionValueChanged(e);
            }
        });
        tblFiles.addMouseListener(new PopupHandler());
    }
    
    private BeanTreeView view;
    private final ExplorerManager folderManager = new ExplorerManager();
    private Lookup folderLookup;
    private Result<File> folderLookupResult;
    
    
    private InstanceContent fileContext = new InstanceContent();
    
    private FileTableModel fileModel;
    private List<VideoFile> selectedVideo;
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        pnlFolders = new javax.swing.JPanel();
        pnlFiles = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnlFolders.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlFolders.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(pnlFolders);

        pnlFiles.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(tblFiles);

        pnlFiles.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(pnlFiles);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnlFiles;
    private javax.swing.JPanel pnlFolders;
    private javax.swing.JTable tblFiles;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        folderLookupResult.addLookupListener(folderListener);
        folderLookupChanged(new LookupEvent(folderLookupResult));
    }

    @Override
    public void componentClosed() {
        folderLookupResult.removeLookupListener(folderListener);
    }
    
    
    private LookupListener folderListener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent le) {
            folderLookupChanged(le);
        }
    };

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    
    
    private void folderLookupChanged(LookupEvent le) {
        
        Collection<? extends File> files = folderLookupResult.allInstances();
        File selectedFile = (files.isEmpty() ? null : files.iterator().next());
        
        setFolder(selectedFile);
    }
    
    
    private File folder;
    
    private void setFolder(File folder) {
        if (!ComparisionUtils.equals(this.folder, folder)) {
            fileModel.setFolder(folder);
            this.folder = folder;
        }
    }
    
    private void tblFilesSelectionValueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int[] rows = tblFiles.getSelectedRows();
            List<VideoFile> selectedFiles = new ArrayList<VideoFile>(rows.length);
            for(int row: rows)
                selectedFiles.add(fileModel.getFile(tblFiles.convertRowIndexToModel(row)));
            setSelectedVideo(selectedFiles);
        }
    }
    
    
    
    private List<Object> currentContentObjects = new ArrayList<Object>();
    
    public void setSelectedVideo(List<VideoFile> infos) {
        if (!Utilities.compareObjects(this.selectedVideo, infos)) {
            for(Object obj: currentContentObjects)
                fileContext.remove(obj);
            currentContentObjects.clear();
            
            this.selectedVideo = infos;
            
            for(VideoFile info: infos) {
                UIUtils.addContentObject(info, currentContentObjects);
                
            }
                
            for(Object obj: currentContentObjects)
                fileContext.add(obj);
        }
    }
    
    
    private void maybePoup(MouseEvent e) {
        if (!e.isPopupTrigger())
            return;
        
        Action[] actions = UIUtils.getActions(getLookup());
        
        JPopupMenu menu = Utilities.actionsToPopup(actions, tblFiles);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
    
    private class PopupHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            maybePoup(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybePoup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybePoup(e);
        }
    }
}