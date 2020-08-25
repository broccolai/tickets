type Location = {
  world: string;
  x: number;
  y: number;
  z: number;
};

export const serialiseLocation = ({ world, x, y, z }: Location): string => {
  return world + '|' + x + '|' + y + '|' + z;
};

export const deserialiseLocation = (data: string): Location => {
  const split = data.split('|');

  return {
    world: split[0],
    x: Number(split[1]),
    y: Number(split[2]),
    z: Number(split[3]),
  };
};

export default Location;
