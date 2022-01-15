import './home.scss';

import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import Axios from 'axios';
import { useAppSelector } from 'app/config/store';

import './VideoTile.css';
import './App.css';
/* eslint-disable */
function posterSearch() {
  const [query, setquery] = useState(''); // use state is updating the value in the frontend
  const [videos, setvideos] = useState([]);
  const [showButton, setShowButton] = useState(false);
  const addToFavorites = () => {
    //if user is logged in
    //submit form to add video to favorites
    //confirmation will be that the button changes to "remove from favorites"
    console.log('Added to favorites!');
  };
  const posterUrl = `https://api.themoviedb.org/3/search/movie?&api_key=616093e66ab252685ad921e5c4680152&query=${query}`;

  async function getPoster() {
    const result = await Axios.get(posterUrl);
    setvideos(result.data.results);
    // console.log(result.data);
  }

  const onSubmit = e => {
    e.preventDefault(); // prevent page from reloading
    getPoster();
  };

  const videoTileEnter = () => setShowButton(true);
  const videoTileLeave = () => setShowButton(false);
  return (
    <Row>
      <Col md="3" className="pad"></Col>
      <Col md="9">
        <h2>
          <Translate contentKey="home.title">Welcome to Campr</Translate>
        </h2>
        <p className="lead">
          <Translate contentKey="home.subtitle"></Translate>
        </p>
        <div>
          <Col md="5">
            <div className="app">
              <form className="app__searchForm" onSubmit={onSubmit}>
                <input
                  type="text"
                  className="app__input"
                  placeholder="Find Videos"
                  value={query}
                  onChange={e => setquery(e.target.value)}
                />
                <input className="app__submit" type="submit" value="Search" />
              </form>
            </div>
            <div className="app__videos">
              {videos.map(video => {
                const value = video.id;
                const url = `https://api.themoviedb.org/3/movie/${value}/videos?api_key=616093e66ab252685ad921e5c4680152`;
                var videoDisplay;
                fetch(url)
                  .then(res => res.json())
                  .then(data => (videoDisplay = data))
                  .then(() => console.log(videoDisplay));
                return (
                  <div
                    key={video.id}
                    className="VideoTile"
                    //if videoid not in favorites list already render remove from favorites button
                    onMouseEnter={videoTileEnter}
                    onMouseLeave={videoTileLeave}
                    onClick={() => {
                      {
                        video.poster_path == null
                          ? window.open(`https://www.youtube.com/results?search_query=${query}`)
                          : window.open(`https://www.youtube.com/watch?v=${videoDisplay.results[0].key}`);
                      }
                    }}
                  >
                    <div className="row" style={{ position: 'relative' }}>
                      {video.poster_path == null ? (
                        <img
                          className="videoTile__img"
                          src={`https://c.tenor.com/0bN9L54PMmsAAAAC/coming-soon-see-it-soon.gif`}
                          alt="card image"
                          style={{ width: '100%', height: 360 }}
                        />
                      ) : (
                        <img
                          className="videoTile__img"
                          src={`https://image.tmdb.org/t/p/w185${video.poster_path}`}
                          alt="card image"
                          style={{ width: '100%', height: 360 }}
                        />
                      )}
                      {showButton && video.poster_path != null && (
                        <button type="button" className="FaveButton" style={{ position: 'absolute', bottom: 10 }} onClick={addToFavorites}>
                          Favorite
                        </button>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </Col>
          <Alert color="light"></Alert>
        </div>
      </Col>
    </Row>
  );
}

export default posterSearch;
