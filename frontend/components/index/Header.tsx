import React from 'react';
import styled from 'styled-components';

const Container = styled.header`
  display: flex;
  height: 5rem;
  width: 100%;
`;

const Header = () => {
  return (
    <Container>
      <img src="/logo.svg" />
    </Container>
  );
};

export default Header;
