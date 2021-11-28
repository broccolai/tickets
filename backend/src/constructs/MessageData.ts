import Player from './Player';
import Ticket from './Ticket';

type MessageData = {
  server: string | null;
  ticket: Ticket;
  author: Player;
  action: string;
  note?: string;
};

export default MessageData;
