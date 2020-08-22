import { AppProps } from 'next/app';
import React from 'react';
import { createGlobalStyle } from 'styled-components';

const GlobalReset = createGlobalStyle`
    body {
      margin: 0;
    }
`;

const App = ({ Component, pageProps }: AppProps) => (
  <>
    <Component {...pageProps} />
    <GlobalReset />
  </>
);

export default App;
