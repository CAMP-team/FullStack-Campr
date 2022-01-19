// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck
/* eslint-disable */
import { Col, Alert, Card } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import './App.css';
import VideoCard from './VideoCard';
import axios from 'axios';
import React, { ChangeEvent, useEffect, useState } from 'react';
import Youtube from 'react-youtube';
import ReactPlayer from 'react-player';
import YouTubePlayer from 'react-player/youtube';
import YouTube from 'react-youtube';

function Home() {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';
  const API_URL = 'https://api.themoviedb.org/3';
  const [video, setVideo] = useState([]);
  const [selectedVideo, setSelectedVideo] = useState([]);
  const [searchKey, setSearchKey] = useState('');
  // const [playTrailer,setPlayTrailer] = useState(false)

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
    // console.log(data);
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
    return <Youtube videoId={trailer.key} />;
  };

  return (
    <>
      <div className="videoapp">
        <header className={'header'}>
          <div className={'header-content max-center'}>
            ]
            <form onSubmit={searchVideos}>
              <input type="text" onChange={(e: ChangeEvent<HTMLInputElement>) => setSearchKey(e.target.value)} />
              <button type={'submit'}>Search</button>
            </form>
          </div>
          <div></div>
        </header>
        <div className="imageHeader" style={{ backgroundImage: `url('${IMAGE_PATH}${selectedVideo.backdrop_path}')` }}>
          <div className="imageHeader-content max-center">
            {selectedVideo.videos ? renderTrailer() : null}
            {/* <button className={"button"} onClick={() =>setPlayTrailer(true)} >Play Trailer</button> */}
            <h1 className={'imageHeader-title'}>{selectedVideo.title}</h1>
            {selectedVideo.overview ? <p className={'imageHeader-overview'}>{selectedVideo.overview}</p> : null}
          </div>
        </div>

        <div className="container">{renderPosters()}</div>
      </div>

      <Col md="1">{account?.login ? <div></div> : <Alert color="light"></Alert>}</Col>
    </>
  );
}

export default Home;
