import Location from './Location';
import Player from './Player';
import TicketStatus from './TicketStatus';

export type RawTicket = {
  id: number;
  player: Player;
  location: Location;
  status: keyof typeof TicketStatus;
  message: string;
};

class Ticket {
  private constructor(
    public id: number,
    public player: Player,
    public location: Location,
    public status: TicketStatus,
    public message: string,
  ) {}

  static fromJson({ id, player, location, status, message }: RawTicket): Ticket {
    return new Ticket(id, player, location, TicketStatus[status], message);
  }
}

export default Ticket;
