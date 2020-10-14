package ie.mcwebdeveloper.project.repositories;

import ie.mcwebdeveloper.project.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById(long id);
    List<Book> findAllByUserid(long userid); // To find current loans
    @Modifying
    @Query("delete from Book b where b.id = ?1")
    int deleteById(long id);
    @Query("select b from Book b where lower(b.title) like lower(concat('%', ?1,'%')) or lower(b.author) like lower(concat('%', ?1,'%'))")
    List<Book> findAllByTitleOrAuthorLike(String author);
    @Transactional
    @Modifying
    @Query("update Book b set b.duedate = ?1 where b.id = ?2")
    int renewLoan(Date date, long id);
    @Transactional
    @Modifying
    @Query("update Book b set b.userid = ?1, b.available = false where b.id = ?2")
    int newLoanee(long uid, long bid);
    @Transactional
    @Modifying
    @Query("update Book b set b.reservedid = ?1, b.reserved = true, b.available = false where b.id = ?2")
    int newReservee(long uid, long bid);
    @Transactional
    @Modifying
    @Query("update Book b set b.userid = ?1, b.duedate = ?3, b.available = false where b.id = ?2")
    int loanToMember(long loaneeID, long bid, Date dueDate);
    @Transactional
    @Modifying
    @Query("update Book b set b.reservedid = ?1, b.reserved = true, b.available = false where b.id = ?2")
    int reserveToMember(long reserveeID, long bid);
    @Transactional
    @Modifying
    @Query("update Book b set b.available = true, b.userid = ?3, b.duedate = ?2 where b.id = ?1")
    int returnBook(long bid, Date date, Long ID);
}