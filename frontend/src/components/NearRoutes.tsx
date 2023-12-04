import styled from 'styled-components';
import RouteElem from '@/components/RouteElem';

const Routes = styled('div')`
  padding: 10px;
  border-radius: 3px;
  background-color: white;

`;

const Header = styled('header')`
  border-bottom: 1px solid gray;
  font-weight: 500;
  margin-bottom: 10px;
`;

const List = styled('div')`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
`;

const NearRoutes = () => {
  return (
    <Routes>
      <Header>Ближайшие маршруты</Header>
      <List>{Array(10).fill(null).map(_ => (
          <RouteElem />
      ))}</List>
    </Routes>
  );
};

export default NearRoutes;