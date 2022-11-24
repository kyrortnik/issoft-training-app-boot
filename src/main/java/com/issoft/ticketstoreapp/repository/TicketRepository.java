package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying
    @Query("DELETE FROM Ticket t WHERE t IN \n" +
            "(   SELECT tk FROM Ticket tk INNER JOIN tk.buyer u \n" +
            "    WHERE tk.id = :ticketId AND tk.buyer.login = :username)\n" +
            "OR t IN\n" +
            "(   SELECT tk FROM Ticket tk INNER JOIN tk.hall h INNER JOIN h.cinema c \n" +
            "    WHERE tk.id = :ticketId AND CONCAT('ROLE_CINEMA_ADMIN','_',UPPER(c.name)) = \n" +
            "    (   SELECT r.name FROM Role r INNER JOIN r.users u \n" +
            "        WHERE u.login = :username AND r.name LIKE 'ROLE_CINEMA_ADMIN_%')\n" +
            ")")
    int deleteByIdWithUsername(Long ticketId, String username);
}