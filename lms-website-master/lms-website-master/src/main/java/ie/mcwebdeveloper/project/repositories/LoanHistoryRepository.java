package ie.mcwebdeveloper.project.repositories;

import ie.mcwebdeveloper.project.models.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    @Query("select h.bookid from LoanHistory h where h.userid = ?1")
    Long[] findLoanHistoryOfUser(long userid);
}
