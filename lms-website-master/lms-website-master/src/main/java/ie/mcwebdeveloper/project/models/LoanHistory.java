package ie.mcwebdeveloper.project.models;

import javax.persistence.*;

@Entity
@Table(name = "loanhistory")
public class LoanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rownumber;
    private long bookid;
    private long userid;

    protected LoanHistory() {}

    public LoanHistory(long bookid, long userid) {
        this.bookid = bookid;
        this.userid = userid;
    }

    public long getBookid() {
        return bookid;
    }
    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public long getUserid() {
        return userid;
    }
    public void setUserid(long userid) {
        this.userid = userid;
    }

}
