import React from 'react';
import styled from 'styled-components';
import GoogleFonts from 'next-google-fonts';
import Header from '../components/index/Header';
import Cards from '../components/index/Cards';

const Background = styled.main`
  background: rgb(28, 30, 32);
  min-height: 100vh;
`;

const Index = () => (
  <>
    <GoogleFonts href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap" />
    <Background>
      <Header />
      <Cards />
    </Background>
  </>
);

export default Index;
