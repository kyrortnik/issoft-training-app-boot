package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //    @Query(value =
//            "SELECT u " +
//                    "FROM Ticket t " +
//                    "INNER JOIN t.buyer u " +
//                    "INNER JOIN t.hall " +
//                    "WHERE t.hall.name = :hallName " +
//                    "AND t.purchaseDateTime BETWEEN :lastMonth AND :now " +
//                    "GROUP BY u " +
//                    "ORDER BY COUNT(t.id) DESC")
    @Query(value =
            "SELECT u FROM Ticket t INNER JOIN t.buyer u INNER JOIN t.hall " +
            "WHERE t.hall.name = :hallName AND t.purchaseDateTime BETWEEN :lastMonth AND :now " +
            "GROUP BY u.id ORDER BY COUNT(t.id) DESC")
    List<User> findLastMonthMostActiveForHall(String hallName, LocalDateTime lastMonth, LocalDateTime now);

    Optional<User> findByLogin(String login);
}