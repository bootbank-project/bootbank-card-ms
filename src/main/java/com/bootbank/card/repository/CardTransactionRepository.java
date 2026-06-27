import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("SELECT t FROM CardTransaction t " +
            "JOIN t.userCard c " +
            "WHERE c.clientCif = :clientCif " +
            "ORDER BY t.createdAt DESC")
    List<CardTransaction> findByClientCifOrderByCreatedAtDesc(@Param("clientCif") String clientCif);
}
