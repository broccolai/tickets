type MessageData = {
  id: string;
  uuid: string;
  author: string;
  color: string;
  action: string;
  fields?: {
    [key: string]: string;
  };
};

export default MessageData;
