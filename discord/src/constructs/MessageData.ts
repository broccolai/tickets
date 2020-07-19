type MessageData = {
  id: string;
  uuid: string;
  author: string;
  color: string;
  action: string;
  fields?: Array<[string, string]>;
};

export default MessageData;
