package ru.job4j.cinema.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOG = Logger.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> createTicket(Ticket ticket) {
        Optional<Ticket> optionalTicket = Optional.empty();
        try (var connection = sql2o.open()) {
            var sql = """
                INSERT INTO tickets(session_id, row_number, place_number, user_id)
                VALUES (:sessionId, :rowNumber, :placeNumber, :userId)""";
            var query = connection.createQuery(sql, true)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());

            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            optionalTicket = Optional.of(ticket);
        } catch (Exception e) {
            LOG.error("Exception in buying ticket", e);
        }
        return optionalTicket;
    }
}
