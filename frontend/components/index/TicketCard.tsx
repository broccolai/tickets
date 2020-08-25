import React, { useState } from 'react';
import Card from '../general/Card';
import styled from 'styled-components';
import Ticket from '../../constructs/Ticket';
import { SubText, TITLE_FONTS } from '../general/Typography';

const ColoredCard = styled(Card)`
  border-left-color: ${(props) => props.color};
  border-left-style: solid;
  border-left-width: 5px;
  cursor: pointer;
`;

const Id = styled(SubText)`
  float: left;
`;

const Player = styled(SubText)`
  float: right;
`;

const Title = styled.span`
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  float: left;
  font-family: ${TITLE_FONTS};
  letter-spacing: 1px;
  margin: 0.5rem 0 0.3rem;
  overflow: hidden;
  text-overflow: clip;
`;

type InformationTypings = {
  expand: boolean;
};

const Information = styled.div<InformationTypings>`
  max-height: ${(props) => (props.expand ? '6rem' : 0)};
  overflow: hidden;
  transition: all 0.8s ease-in-out;
`;

const Item = styled.span``;

type Props = {
  ticket: Ticket;
};

const TicketCard = ({ ticket: { status, message } }: Props) => {
  const [expand, setExpand] = useState(false);

  return (
    <ColoredCard color={status} onClick={() => setExpand(!expand)}>
      <div>
        <Id>
          <small>#</small>5
        </Id>
        <Player>broccolai</Player>
        <Title>{message}</Title>
      </div>
      <Information expand={expand}>
        <Item>uuid</Item>
        <Item>uuid</Item>
        <Item>uuid</Item>
      </Information>
    </ColoredCard>
  );
};

export default TicketCard;
