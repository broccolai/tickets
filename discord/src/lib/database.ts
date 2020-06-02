import sqlite3 from 'sqlite3';

const db = new sqlite3.Database('./storage.db', sqlite3.OPEN_READWRITE);

db.run('CREATE TABLE IF NOT EXISTS server(guild TEXT, token TEXT, outputChannel TEXT)');

export const getData = () => {
  const servers = new Map();

  db.all('SELECT guild, token, outputChannel FROM server', (err, rows) => {
    if (err) return;

    rows.forEach((row) => {
      const guild = row['guild'];
      const token = row['token'];
      const output = row['outputChannel'];

      servers.set(guild, { token: token, output: output });
    });
  });

  return servers;
};

export default db;
