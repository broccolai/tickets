import { getGuilds } from './database';

export const servers = await getGuilds();
