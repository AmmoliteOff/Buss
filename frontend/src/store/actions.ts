import { routesActions } from '@/store/routesSlice';
import { bindActionCreators } from '@reduxjs/toolkit';
import { useAppDispatch } from '@/store/store';

const actions = {
  ...routesActions
};

export function useActions() {
  return bindActionCreators(actions, useAppDispatch());
}