import React from 'react';
import { useState } from 'react';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import Axios from 'axios';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import './App.css';
import VideoCard from './VideoCard';
import Header from 'app/shared/layout/header/header';

/* eslint-disable */

function videoApp() {
  const API_URL = 'https://api.themoviedb.org/3';
  const [videos, setVideos] = useState([]);

  const fetchMovies = async () => {
    const {
      data: { results },
    } = await Axios.get(`${API_URL}/discover/movie`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
      },
    });
    setVideos(results);
    console.log('data', results);
  };

  useEffect(() => {
    fetchMovies();
  }, []);

  const renderVideos = () => videos.map(video => <VideoCard key={video.id} video={video} />);

  const account = useAppSelector(state => state.authentication.account);
  return (
    <Row>
      <Col md="8" className="pad"></Col>
      <Col md="9">
        <div className="VideoApp">
          <div className="container">{renderVideos()}</div>
        </div>
        {account?.login ? (
          <div>
            <Col md="5"></Col>
          </div>
        ) : (
          <Alert color="light"></Alert>
        )}
        <div>
          <div></div>
        </div>
      </Col>
    </Row>
  );
}

export default videoApp;
