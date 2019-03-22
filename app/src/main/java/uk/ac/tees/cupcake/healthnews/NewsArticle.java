package uk.ac.tees.cupcake.healthnews;

import java.util.Date;

public class NewsArticle {

    private String articleName;
    private String articleDesc;
    private int articleImage;
    private String articleDate;

    /**
     * All the parameters a News Article will require.
     *
     * @param articleName - Name of article
     * @param articleDesc - Description of article
     * @param articleImage - Article image
     * @param articleDate - Article date
     */
    public NewsArticle(String articleName, String articleDesc, int articleImage, String articleDate) {
        this.articleName = articleName;
        this.articleDesc = articleDesc;
        this.articleImage = articleImage;
        this.articleDate = articleDate;
    }

    public int getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(int articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }
}
