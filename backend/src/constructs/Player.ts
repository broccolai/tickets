import db from '@lib/providers/database';

type Player = {
  name: string;
  uuid: string;
};

export const deserialisePlayer = async (uuid: string): Promise<Player> => {
  let onResolve;
  const promise = new Promise<Player>((resolve) => {
    onResolve = resolve;
  });

  db.get('SELECT name FROM player WHERE uuid = ?', [uuid], (err, row) => {
    if (err) {
      throw new Error();
    }

    onResolve({
      uuid: uuid,
      name: row['name'],
    });
  });

  return promise;
};

export default Player;
