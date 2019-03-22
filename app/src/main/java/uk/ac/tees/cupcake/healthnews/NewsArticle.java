package uk.ac.tees.cupcake.healthnews;

public class NewsArticle {

    private String articleName;
    private String articleDesc;
    private int articleImage;

    /**
     * All the parameters a News Article will require.
     *
     * @param articleName - Name of article
     * @param articleDesc - Description of article
     * @param image       - Article image
     */
    public NewsArticle(String articleName, String articleDesc, int articleImage) {
        this.articleName = articleName;
        this.articleDesc = articleDesc;
        this.articleImage = articleImage;
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
}
