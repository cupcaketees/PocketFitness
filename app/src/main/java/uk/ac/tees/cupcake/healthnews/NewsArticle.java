package uk.ac.tees.cupcake.healthnews;

public class NewsArticle {

    private String articleName;
    private String articleDesc;
    private String articleDate;
    private String articleURLName;
    private String articleURLLink;
    private String articleImageURL;

    /**
     * All the parameters a News Article will require.
     *
     * @param articleName     - Name of article
     * @param articleDesc     - Description of article
     * @param articleURLLink  - Article URL
     * @param articleDate     - Article date
     * @param articleImageURL - Link to Article Image URL
     * @param articleURLName  - Name of Website
     */
    public NewsArticle(String articleName, String articleDesc, String articleDate, String articleURLName, String articleImageURL, String articleURLLink) {
        this.articleName = articleName;
        this.articleDesc = articleDesc;
        this.articleDate = articleDate;
        this.articleURLName = articleURLName;
        this.articleImageURL = articleImageURL;
        this.articleURLLink = articleURLLink;
    }


    public String getArticleURLLink() { return articleURLLink; }

    public String getArticleURLName() {
        return articleURLName;
    }

    public String getArticleImageURL() {
        return articleImageURL;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleDate() {
        return articleDate;
    }

}
