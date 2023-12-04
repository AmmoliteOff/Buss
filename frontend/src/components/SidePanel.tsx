import styled from 'styled-components';
import Search from '@/components/Search';
import { FC, ReactNode } from 'react';

const StyledSidePanel = styled('div')`
  background-color: black;
  width: 500px;
  border-top-right-radius: 40px;
  border-bottom-right-radius: 40px;
  margin-right: -40px;
  position: relative;
  z-index: 10;
  padding: 30px;

  & > * + * {
    margin-top: 20px;
  }
`;

interface Props {
  children: ReactNode;
}

const SidePanel: FC<Props> = ({ children }) => {
  return (
    <StyledSidePanel>
      <Search />
      {children}
      {/*<NearRoutes />*/}
    </StyledSidePanel>
  );
};

export default SidePanel;