import { Route } from '@/types/Route';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type RoutesState = Route[];

const initialState: RoutesState = [];

const routesSlice = createSlice({
  name: 'routes',
  initialState,
  reducers: {
    setRoutes(_, action: PayloadAction<RoutesState>) {
      return action.payload;
    },
  }
});

export const routesActions = routesSlice.actions;
export const routesReducer = routesSlice.reducer;
