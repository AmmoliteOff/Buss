import { Waypoint } from '@/types/Waypoint';

export interface Stop {
  id: number;
  title: string;
  route: Waypoint[];
}