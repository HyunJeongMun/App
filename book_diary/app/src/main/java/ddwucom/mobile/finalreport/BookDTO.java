package ddwucom.mobile.finalreport;
import java.io.Serializable;

public class BookDTO implements Serializable {

    private long _id;
    private String title;
    private String author;
    private int price;
    private String publishDate;
    private String publisher;
    private int cover;

    public BookDTO(long _id, String title, String author, int price, String publishDate, String publisher, int cover){
        this._id = _id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.cover = cover;
    }

    public BookDTO(String title, String author, int price, String publishDate, String publisher, int cover){
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.cover = cover;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }
}
