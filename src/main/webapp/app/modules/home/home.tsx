// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck
/* eslint-disable */

import { Col, Alert, Card } from 'reactstrap';
import axios from 'axios';
import React, { ChangeEvent, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, createEntity, deleteEntity, reset } from 'app/entities/user-favorites/user-favorites.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayCurrentDateTime } from 'app/shared/util/date-utils';
import VideoCard from './VideoCard';
import Modal from 'react-bootstrap/Modal';
import { Button } from 'react-bootstrap';
import './VideoTile.css';
import './home.scss';
import './App.css';

// @ts-ignore
function Home() {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';
  const API_URL = 'https://api.themoviedb.org/3';
  const [video, setVideo] = useState([]);
  const [selectedVideo, setSelectedVideo] = useState([]);
  const [searchKey, setSearchKey] = useState('');
  const [playTrailer, setPlayTrailer] = useState(true);
  const [show, setShow] = useState(false);
  const dispatch = useAppDispatch();
  useEffect(() => {
    getFirstVideoFetch();
  }, []);
  // json friendly video list
  const favors = [];
  const userFavorites = useAppSelector(state => state.userFavorites.entity);
  const [query, setquery] = useState(''); // use state is updating the value in the frontend
  const [videos, setvideos] = useState([]);
  const [showButton, setShowButton] = useState(false);
  const [videoTileEnabled, setVideoTileEnabled] = useState(true);
  const account = useAppSelector(state => state.authentication.account);
  // json friendly user formatter
  const user = (id, login) => {
    return { id: id, login: login };
  };
  const getFirstVideoFetch = async searchKey => {
    const type = searchKey ? 'search' : 'discover';
    const {
      data: { results },
    } = await axios.get(`${API_URL}/${type}/movie`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
        query: searchKey,
      },
    });

    setSelectedVideo(results[0]);
    setVideo(results);
  };

  const getSecondVideoFetch = async id => {
    const { data } = await axios.get(`${API_URL}/movie/${id}`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
        append_to_response: 'videos',
      },
    });
    return data;
  };
  const selectVideo = async video => {
    const data = await getSecondVideoFetch(video.id);
    setSelectedVideo(data);
  };
  const renderPosters = () => video.map(video => <VideoCard key={video.id} video={video} selectVideo={selectVideo} />);
  const searchVideos = e => {
    e.preventDefault();
    getFirstVideoFetch(searchKey);
  };
  const addToFavorites = (event: React.MouseEvent<HTMLButtonElement>, videoId: any) => {
    // if user is logged in
    // must map video to a new object with a singular attrib: its id
    if (account == null) {
      console.log('You need to log in first!');
    } else if (account.login) {
      const favor = video(videoId);
      const you = user(account.id, account.login);
      favors.push(favor);
      const entity = {
        ...userFavorites,
        dateAdded: displayCurrentDateTime(),
        user: you,
        videos: favors,
      };
      dispatch(createEntity(entity));
      // resets favors after it is added in to favorites
      // resets users after fave added in
      //setUsers([]);
      // maybe a seperate attribute indicating whether video is in faves or not?
      // no need just use (isNew) (maybe not?)
      // const favorites = useAppSelector(state => account-favorites.entities);
      // how to access favorites for just one user
      // the reducer for the user-favorites
      // how to get the update?
      // how to make sure the user matches the favorites
      // set addedTofavorites to true
      // 1/16/22: 6:57
      //may be getting a 500 error because video is not in database
    } else {
      console.log('You need to log in first!');
    }
    // submit form to add video to favorites
    // confirmation will be that the button changes to "remove from favorites"
    console.log('Added to favorites!');
  };

  const renderTrailer = () => {
    const trailer = selectedVideo.videos.results[0];
    console.log(trailer);
    const trailerKey = trailer.key;
    return (
      <iframe
        width="760"
        height="500"
        src={`https://www.youtube.com/embed/${trailer.key}?enablejsapi=1&origin=https://camp-r.herokuapp.com*`}
        title="YouTube video player"
        frameBorder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
      />
    );
  };
  const disableVideoTile = () => setVideoTileEnabled(false);
  const enableVideoTile = () => setVideoTileEnabled(true);
  const videoTileEnter = () => setShowButton(true);
  const videoTileLeave = () => setShowButton(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  return (
    /**
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
                        if (videoTileEnabled) {
                          video.poster_path == null
                            ? window.open(`https://www.youtube.com/results?search_query=${query}`)
                            : window.open(`https://www.youtube.com/watch?v=${videoDisplay.results[0].key}`);
                        }
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
                        <button
                          type="submit"
                          onMouseEnter={disableVideoTile}
                          onMouseLeave={enableVideoTile}
                          className="FaveButton"
                          style={{ position: 'absolute', bottom: 10 }}
                          onClick={e => addToFavorites(e, video.id)}
                        >
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
*/
    <>
      <div className="videoapp">
        <header className={'header'}>
          <div className={'header-content max-center'}>
            <form onSubmit={searchVideos}>
              <input
                placeholder="Search for movies"
                type="text"
                onChange={(e: ChangeEvent<HTMLInputElement>) => setSearchKey(e.target.value)}
              />
              <button type={'submit'}>Search</button>
            </form>
          </div>
        </header>
        <div className="imageHeader" style={{ backgroundImage: `url('${IMAGE_PATH}${selectedVideo.backdrop_path}')` }}>
          <div className="imageHeader-content max-center">
            <h1 className={'imageHeader-title'}>{selectedVideo.title}</h1>
            {selectedVideo.overview ? <p className={'imageHeader-overview'}>{selectedVideo.overview}</p> : null}
            <button className={'btnPlay-Close'} onClick={handleShow}>
              Play Trailer
            </button>

            <Modal size="lg" show={show} onHide={handleClose}>
              <Modal.Header closeButton>
                <Modal.Title>{selectedVideo.title}</Modal.Title>
              </Modal.Header>
              <Modal.Body>{selectedVideo.videos && playTrailer ? renderTrailer() : null}</Modal.Body>
            </Modal>
          </div>
        </div>
        <div className="container">{renderPosters()}</div>
      </div>

      {/*<Col md="1">{account?.login ? <div /> : <Alert color="light" />}</Col>*/}
    </>
  );
}

export default Home;
