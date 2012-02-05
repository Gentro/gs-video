/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VideoInfoPanel.java
 *
 * Created on Dec 27, 2011, 8:42:17 PM
 */
package net.landora.video.infopanel;

import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import net.landora.video.info.MovieMetadata;
import net.landora.video.info.MultiSeasonSeriesMetadata;
import net.landora.video.info.SeriesMetadata;
import net.landora.video.info.VideoMetadata;
import net.landora.video.ui.InfoPanel;
import net.landora.video.utils.UIUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class VideoInfoPanel extends InfoPanel {

    /** Creates new form VideoInfoPanel */
    public VideoInfoPanel() {
        initComponents();
        lblPicture.setText("");
    }

    @Override
    public void clearCurrentContext() {
        lblPicture.setIcon(null);
    }

    @Override
    public void loadContext(MultiValueMap context) {
        Collection col = context.getCollection(VideoMetadata.class);
        
        
        VideoMetadata md = (VideoMetadata) UIUtils.select(col);
        
        
               
        byte[] data = md.getPosterImage();
        if (data == null)
            lblPicture.setIcon(null);
        else {
            try {
                Image img = ImageIO.read(new ByteArrayInputStream(data));
                lblPicture.setIcon(new ImageIcon(img));
            } catch (IOException ex) {
                lblPicture.setIcon(null);
                LoggerFactory.getLogger(getClass()).warn("Error loading image.", ex);
            }
        }
        
        Map<String,String> values = new LinkedHashMap<String, String>();
        if (md.isMovie()) {
            MovieMetadata movie = (MovieMetadata)md;
            values.put("Movie Name", movie.getMovieName());
        } else if (md.isSeries()) {
            SeriesMetadata series = (SeriesMetadata)md;
            values.put("Series name", series.getSeriesName());
            if (md.isMultiSeasonSeries()) {
                MultiSeasonSeriesMetadata multiSeason = (MultiSeasonSeriesMetadata)md;
                values.put("Season", String.valueOf(multiSeason.getSeasonNumber()));
            }
            values.put("Episode Number", series.getEpisodeNumber());
            if (series.getEpisodeName() != null) {
                values.put("Episode Name", series.getEpisodeName());
            }
        }
        
        md.addExtraInformation(values);
        
        if (md.isAdult()) {
            values.put("Adult Content", "Yes");
        }
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("<html>");
        
        Font font = lblPicture.getFont();
        buffer.append("<head>");
        buffer.append("<style type=\"text/css\">");
        
        buffer.append(" { margin-top: 0px; margin-bottom: 0px; margin-right: 0px; margin-left: 0px;  }");
        
        buffer.append("table { border-collapse:collapse; }");
        
        buffer.append(" td { ");
        buffer.append(" font-family: \"");
        buffer.append(font.getFamily());
        buffer.append("\"; font-size: ");
        buffer.append(font.getSize() - 2);
        buffer.append("px; ");
        
        buffer.append(" }\n");
        
        buffer.append(" td.label { ");
        buffer.append("text-align: right; ");
        buffer.append("font-weight:bold; ");
        buffer.append("white-space:nowrap; ");
        
        
        buffer.append(" font-family: \"");
        buffer.append(font.getFamily());
        buffer.append("\"; font-size: ");
        buffer.append(font.getSize() - 2);
        buffer.append("px; ");
        
        buffer.append("} ");
        buffer.append("</style>");
        buffer.append("</head>");
        buffer.append("<body>");
        buffer.append("<table>");
        boolean first = true;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            buffer.append("<tr><td class=\"label\">");
            buffer.append(StringEscapeUtils.escapeHtml(entry.getKey()));
            buffer.append("</td><td>");
            buffer.append(StringEscapeUtils.escapeHtml(entry.getValue()));
            buffer.append("</td></tr>");
        }
        
        buffer.append("</table>");
        buffer.append("</body>");
        buffer.append("</html>");
        
        txtInfo.setContentType("text/html");
        txtInfo.setText(buffer.toString());
    }

    @Override
    public boolean supportsContext(MultiValueMap context) {
        Collection col = context.getCollection(VideoMetadata.class);
        return col != null && col.size() == 1;
    }

    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlDetails = new javax.swing.JPanel();
        lblPicture = new javax.swing.JLabel();
        pnlInfo = new javax.swing.JPanel();
        txtInfo = new javax.swing.JEditorPane();

        setTitle("Video");
        setLayout(new java.awt.BorderLayout());

        pnlDetails.setName("pnlDetails"); // NOI18N
        pnlDetails.setLayout(new java.awt.GridBagLayout());

        lblPicture.setText("Picture");
        lblPicture.setName("lblPicture"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlDetails.add(lblPicture, gridBagConstraints);

        pnlInfo.setName("pnlInfo"); // NOI18N
        pnlInfo.setLayout(new java.awt.BorderLayout());

        txtInfo.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtInfo.setName("txtInfo"); // NOI18N
        txtInfo.setOpaque(false);
        pnlInfo.add(txtInfo, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlDetails.add(pnlInfo, gridBagConstraints);

        add(pnlDetails, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblPicture;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JEditorPane txtInfo;
    // End of variables declaration//GEN-END:variables
}