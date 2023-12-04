import { Map, Polyline, YMaps } from '@pbe/react-yandex-maps';
import styled from 'styled-components';
import { Route } from '@/types/Route';
import { FC } from 'react';

const StyledMap = styled(Map)`
  //grid-column-start: common;
  //grid-column-end: map;
`;

interface Props {
  routes: Route[];
}

const MyMap: FC<Props> = ({ routes }) => {

  return (
    <YMaps>
      <StyledMap
        defaultState={{ center: [51.6683, 39.1919], zoom: 11 }}
      >
        {routes.map(r =>
          <Polyline key={r.id}
            geometry={r.waypoints.map(wp => [wp.latitude, wp.longitude])}
          />
        )}
        {/*<Polyline*/}
        {/*  geometry={waypoints.map(wp => [wp.latitude, wp.longitude])}*/}
        {/*  options={{*/}
        {/*    strokeColor: '#000',*/}
        {/*    strokeWidth: 4,*/}
        {/*    strokeOpacity: 0.5,*/}
        {/*  }}*/}
        {/*/>*/}
      </StyledMap>
    </YMaps>
  );
};

export default MyMap;
