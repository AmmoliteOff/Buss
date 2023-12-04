import SidePanel from '@/components/SidePanel';
import MyMap from '@/components/MyMap';
import styled from 'styled-components';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { ApiUrl } from '@/api';
import { useActions } from '@/store/actions';
import { Route } from '@/types/Route';
import DisplayRouteItem from '@/components/DisplayRouteItem';

const StyledMainPage = styled('div')`
  width: 100%;
  display: grid;
  grid-template-columns: 500px  1fr;
  //grid-template-areas: 'side common map';
  //align-content: stretch;
`;

const MainPage = () => {
  const { setRoutes } = useActions();
  const [groupedRoutes, setGroupedRoutes] = useState<Route[][]>([]);
  const [selectedRoutes, setSelectedRoutes] = useState<number[]>([]);

  useEffect(() => {
    axios.get<Route[]>(ApiUrl.getAllRoutes())
      .then(res => {
        const routes = res.data;
        setRoutes(routes);

        const result: Route[][] = [];
        for (const route of routes) {
          let found = false;
          for (const pair of result) {
            if (pair.length === 1 && pair[0].oppositeRouteId === route.id) {
              pair.push(route);
              found = true;
              break;
            }
          }
          if (!found) {
            result.push([route]);
          }
        }
        for (const pair of result) {
          pair.sort((a, b) => a.id - b.id);
        }
        setGroupedRoutes(result);
        setSelectedRoutes(Array(result.length).fill(0));
      });
  }, []);

  const finalRoutes = groupedRoutes.map((pair, i) => pair[selectedRoutes[i]]);

  return (
    <StyledMainPage>
      <SidePanel>
        <div style={{ background: 'white', borderRadius: 3 }}>
          {selectedRoutes.map((_, i) =>
            <DisplayRouteItem
              title={groupedRoutes[i][0].title}
              route={selectedRoutes[i]}
              setRoute={route => setSelectedRoutes(selectedRoutes.map((r, j) => {
                if (i === j) {
                  console.log(route);
                  return route;
                }
                return r;
              }))}
            />
          )}
        </div>
      </SidePanel>
      <MyMap routes={finalRoutes} />
    </StyledMainPage>
  );
};

export default MainPage;