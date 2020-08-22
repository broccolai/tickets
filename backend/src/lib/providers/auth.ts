import basicAuth from 'express-basic-auth';

import { servers } from './storage';

const authorizer = (username: string, password: string) => {
  if (!servers.has(username)) {
    return false;
  }

  const guild = servers.get(username);

  return basicAuth.safeCompare(guild.token, password);
};

export default basicAuth({ authorizer: authorizer });
