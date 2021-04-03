import { IClientDetails } from 'app/entities/client-details/client-details.model';

export interface IClient {
  id?: number;
  wlascicielDomu?: string | null;
  numer?: number | null;
  adres?: string | null;
  clientDetails?: IClientDetails[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public wlascicielDomu?: string | null,
    public numer?: number | null,
    public adres?: string | null,
    public clientDetails?: IClientDetails[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
