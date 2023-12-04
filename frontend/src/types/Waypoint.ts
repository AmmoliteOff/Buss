import { Stop } from '@/types/Stop';

export interface Waypoint {
  id: number;
  stop?: Stop;
  currentLoadScore: number;
  longitude: number;
  latitude: number;
}