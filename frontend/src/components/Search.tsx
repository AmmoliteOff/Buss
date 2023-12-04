import { Input } from 'antd';
import styled from 'styled-components';

const StyledInput = styled(Input)`
  border-radius: 3px;
`;

const Search = () => {
  return (
    <StyledInput
      placeholder="Поиск в системе"
    />
  );
};

export default Search;