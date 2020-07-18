type MessageData = {
  id: string;
  author: string;
  color: string;
  action: string;
  fields?: Array<[string, string]>;
};

export default MessageData;
