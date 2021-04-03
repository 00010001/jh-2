import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';

export interface IClientDetails {
  id?: number;
  kominyDymowe?: string | null;
  kominySpalinowe?: string | null;
  przewodyWentylacyjne?: string | null;
  ryczaltJednorazowy?: string | null;
  dataCzyszczenia?: dayjs.Dayjs | null;
  dzienCzyszczenia?: number | null;
  zaplacono?: string | null;
  podpis?: string | null;
  client?: IClient | null;
}

export class ClientDetails implements IClientDetails {
  constructor(
    public id?: number,
    public kominyDymowe?: string | null,
    public kominySpalinowe?: string | null,
    public przewodyWentylacyjne?: string | null,
    public ryczaltJednorazowy?: string | null,
    public dataCzyszczenia?: dayjs.Dayjs | null,
    public dzienCzyszczenia?: number | null,
    public zaplacono?: string | null,
    public podpis?: string | null,
    public client?: IClient | null
  ) {}
}

export function getClientDetailsIdentifier(clientDetails: IClientDetails): number | undefined {
  return clientDetails.id;
}
