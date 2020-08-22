import Location from './Location';
import Player from './Player';

type Ticket = {
  id: number;
  player: Player;
  location: Location;
  status: string;
  message: string;
};

export default Ticket;
