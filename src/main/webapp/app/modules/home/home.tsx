// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck
/* eslint-disable */

import { Col, Alert, Card } from 'reactstrap';
import axios from 'axios';
import React, { ChangeEvent, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
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

  useEffect(() => {
    getFirstVideoFetch();
  }, []);

  const renderPosters = () => video.map(video => <VideoCard key={video.id} video={video} selectVideo={selectVideo} />);

  const account = useAppSelector(state => state.authentication.account);
  const searchVideos = e => {
    e.preventDefault();
    getFirstVideoFetch(searchKey);
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

  const videoTileEnter = () => setShowButton(true);
  const videoTileLeave = () => setShowButton(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  return (
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
