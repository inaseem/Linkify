package softpro.bot;

/**
 * Created by Naseem on 11-12-2017.
 */

public class LinkModel {
    private String typeText;
    private String typeLink;
    private String downloadText;
    private String downloadLink;
    private String qualityLabel;
    private int itemType;


    public LinkModel(String typeText, String typeLink, String downloadText, String downloadLink, String qualityLabel,int itemType) {
        this.typeText = typeText;
        this.typeLink = typeLink;
        this.downloadText = downloadText;
        this.downloadLink = downloadLink;
        this.qualityLabel = qualityLabel;
        this.itemType=itemType;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeLink() {
        return typeLink;
    }

    public void setTypeLink(String typeLink) {
        this.typeLink = typeLink;
    }

    public String getDownloadText() {
        return downloadText;
    }

    public void setDownloadText(String downloadText) {
        this.downloadText = downloadText;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getQualityLabel() {
        return qualityLabel;
    }

    public void setQualityLabel(String qualityLabel) {
        this.qualityLabel = qualityLabel;
    }

    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "{typeText:"+typeText+",typeLink:"+typeLink+",downloadText:"+downloadText+",downloadLink:"+downloadLink+",qualityLabel:"+qualityLabel+"}";
    }
}
