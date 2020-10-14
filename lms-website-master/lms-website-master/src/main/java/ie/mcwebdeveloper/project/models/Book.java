package ie.mcwebdeveloper.project.models;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private boolean available = true;
    private boolean reserved = false;
    @Column(nullable = true)
    private Long userid = null;
    @Column(nullable = true)
    private Long reservedid = null;
    @Column(nullable = true)
    private Date duedate = null;

    protected Book() {}

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(String title, String author, boolean available, boolean reserved, long userid, long reservedid, Date dueDate) {
        this.title = title;
        this.author = author;
        this.available = available;
        this.reserved = reserved;
        this.userid = userid;
        this.reservedid = reservedid;
        this.duedate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getReservedid() {
        return reservedid;
    }

    public void setReservedid(Long reservedid) {
        this.reservedid = reservedid;
    }


    public Date getDuedate() {
        return duedate;
    }
    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }
}