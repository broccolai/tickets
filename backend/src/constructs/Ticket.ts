import Location, { deserialiseLocation } from './Location';
import Player, { deserialisePlayer } from './Player';

type Ticket = {
  id: number;
  player: Player;
  location: Location;
  status: string;
  message: string;
};

export const ticketFromRow = async (row: never): Promise<Ticket> => {
  return {
    id: Number(row['id']),
    player: await deserialisePlayer(row['player']),
    location: deserialiseLocation(row['location']),
    status: row['status'],
    message: row['message'],
  };
};

export default Ticket;
