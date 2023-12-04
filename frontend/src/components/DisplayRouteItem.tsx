import styled from 'styled-components';
import { FC } from 'react';
import { Checkbox } from 'antd';

const Wrapper = styled('div')`
  padding: 6px;
  display: flex;
  gap: 10px;
`;

const Title = styled('h4')`

`

interface Props {
  title: string;
  route: number;
  setRoute(route: number): void;
}

const DisplayRouteItem: FC<Props> = ({ setRoute, route, title }) => {
  return (
    <Wrapper>
      <Title>{title}</Title>
      <label>
        <Checkbox checked={route === 0} onChange={e => {
          if (e.target.checked) {
            setRoute(0);
          }}}
        />
        Прямой маршрут
      </label>

      <label>
        <Checkbox checked={route === 1} onChange={e => {
          if (e.target.checked) {
            console.log('set');
            setRoute(1);
          }}}
        />
        Обратный маршрут
      </label>
    </Wrapper>
  );
};

export default DisplayRouteItem;