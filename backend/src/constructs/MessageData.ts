import Player from './Player';
import Ticket from './Ticket';

type MessageData = {
  ticket: Ticket;
  author: Player;
  action: string;
};

export default MessageData;
