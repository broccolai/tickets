const Action: {
  [key: string]: { title: string; author: string };
} = {
  NEW_TICKET: { title: 'Ticket Created', author: 'Created by ' },
  UPDATE_TICKET: { title: 'Ticket Updated', author: 'Updated by ' },
  CLOSE_TICKET: { title: 'Ticket Closed', author: 'Closed by ' },
  PICK_TICKET: { title: 'Ticket Picked', author: 'Picked by ' },
  CLAIM_TICKET: { title: 'Ticket Claimed', author: 'Claimed by ' },
  YIELD_TICKET: { title: 'Ticket Yielded', author: 'Yielded by ' },
  UNCLAIM_TICKET: { title: 'Ticket Unclaimed', author: 'Unclaimed by ' },
  ASSIGN_TICKET: { title: 'Ticket Assigned', author: 'Assigned by ' },
  DONE_TICKET: { title: 'Ticket Done-marked', author: 'Done-marked by ' },
  REOPEN_TICKET: { title: 'Ticket Reopened', author: 'Reopened by ' },
  NOTE_TICKET: { title: 'Note added to Ticket', author: 'Added by ' },
};

export default Action;
