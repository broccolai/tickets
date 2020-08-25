import sqlite3 from 'sqlite3';

import PureGuild from '../constructs/PureGuild';

const db = new sqlite3.Database('./storage.db', sqlite3.OPEN_READWRITE);

db.run('CREATE TABLE IF NOT EXISTS server(guild TEXT, token TEXT, outputChannel TEXT)');
db.run('CREATE TABLE IF NOT EXISTS ticket(id NUMERIC, player UUID, location TEXT, status TEXT, message TEXT');
db.run('CREATE TABLE IF NOT EXIST player(uuid TEXT, name TEXT)');

export const getData = (): Map<string, PureGuild> => {
  const servers = new Map<string, PureGuild>();

  db.all('SELECT guild, token, outputChannel FROM server', (err, rows) => {
    if (err) return;

    rows.forEach((row) => {
      const guild = new PureGuild(row['guild'], row['token'], row['outputChannel']);

      servers.set(guild.id, guild);
    });
  });

  return servers;
};

export default db;
