export default class PureGuild {
  id: string;
  token: string;
  output: string;

  constructor(id: string, token: string, output: string) {
    this.id = id;
    this.token = token;
    this.output = output;
  }
}
