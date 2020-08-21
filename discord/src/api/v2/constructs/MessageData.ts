import Player from './Player';
import Ticket from './Ticket';

type MessageData = {
  ticket: Ticket;
  author: Player;
  action: string;
  fields?: {
    [key: string]: string;
  };
};

export default MessageData;
