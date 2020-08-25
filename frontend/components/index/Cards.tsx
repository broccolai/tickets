import React, { useState, useEffect } from 'react';
import Ticket, { RawTicket } from '../../constructs/Ticket';
import TicketCard from './TicketCard';
import styled from 'styled-components';

const Container = styled.section`
  padding: 2rem;
  width: 26rem;
`;

const Cards = () => {
  const [tickets, setTickets] = useState<Ticket[]>([]);

  useEffect(() => {
    const execute = async () => {
      const res = await fetch('http://localhost:400/api/web/tickets', {
        method: 'GET',
      });

      const data = await res.json();
      const raw: RawTicket[] = data['tickets'];
      const assembled = raw.map((rawTicket) => Ticket.fromJson(rawTicket));

      setTickets(assembled);
    };

    execute();
  }, [tickets]);

  return (
    <Container>
      {tickets.map((ticket) => {
        return <TicketCard key={ticket.id} ticket={ticket} />;
      })}
    </Container>
  );
};

export default Cards;
