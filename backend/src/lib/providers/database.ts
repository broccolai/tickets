import sqlite3 from 'sqlite3';

import PureGuild from '../constructs/PureGuild';

const db = new sqlite3.Database('./storage.db', sqlite3.OPEN_READWRITE | sqlite3.OPEN_CREATE);

db.run('CREATE TABLE IF NOT EXISTS server(guild TEXT, token TEXT, outputChannel TEXT)');
db.run(
  'CREATE TABLE IF NOT EXISTS ticket(guild TEXT, id INTEGER, player UUID, location TEXT, status TEXT, message TEXT, PRIMARY KEY(guild, id))',
);
db.run('CREATE TABLE IF NOT EXISTS player(uuid TEXT PRIMARY KEY, name TEXT)');

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
