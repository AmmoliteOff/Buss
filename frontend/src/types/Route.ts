import { Waypoint } from '@/types/Waypoint';

export interface Route {
  id: number;
  title: string;
  oppositeRouteId: number;
  waypoints: Waypoint[];
}