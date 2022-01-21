// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck
/* eslint-disable */

import { Col, Alert, Card } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, createEntity, deleteEntity, reset } from 'app/entities/user-favorites/user-favorites.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import axios from 'axios';
import React, { ChangeEvent, useEffect, useState } from 'react';
import Youtube from 'react-youtube';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, createEntity, deleteEntity, reset } from 'app/entities/user-favorites/user-favorites.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import VideoCard from './VideoCard';
import './VideoTile.css';
import './home.scss';
import './App.css';
import './App.css';
// @ts-ignore
// @ts-ignore
function Home() {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';
  const API_URL = 'https://api.themoviedb.org/3';
  const [video, setVideo] = useState([]);
  const [selectedVideo, setSelectedVideo] = useState([]);
  const [searchKey, setSearchKey] = useState('');
  const [playTrailer, setPlayTrailer] = useState(true);
  const dispatch = useAppDispatch();

  // json friendly video list
  // const [users, setUsers] = useState([]);
  // const [isNew] = useState(!props.match.params || !props.match.params.id);
  // const [playTrailer,setPlayTrailer] = useState(false)
  // const users = useAppSelector(state => state.userManagement.users);
  // const userFavorites = useAppSelector(state => state.userFavorites.entity);
  // const [users, setUsers] = useState([]);
  //const [isNew] = useState(!props.match.params || !props.match.params.id);

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

  // json friendly user formatter
  // const user = (id, login) => {
  //   return { id: id, login: login };
  // };

  /**
    //perhaps the way to gain authentication to access users?
    useEffect(() => {
        if (isNew) {
          dispatch(reset());
        } else {
          dispatch(getEntity(props.match.params.id));
        }

        dispatch(getUsers({}));
    }, []);
    */
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
        //putting a fixed timestamp will allow post to go thru
        dateAdded: '2021-12-29T05:00:00Z',
        user: you,
        //user: users.find(it => it.id.toString() === account.id.toString()),
        //user: account.id,
        videos: favors,
        //videos: videoId,
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

      // user-favorites.createEntity(videoId);

      // how to get current  logged in user( is is just account.login?)

      // can i get favorites from account if so how?????
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
    // let fullUrl = "https://www.youtube.com/embed/" + trailerKey.toString();
    // return <Youtube videoId={trailer.key} />;
    return (
      <iframe
        width="853"
        height="480"
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
  return (
    <>
      <div className="videoapp">
        <header className={'header'}>
          <div className={'header-content max-center'}>
            <form onSubmit={searchVideos}>
              <input type="text" onChange={(e: ChangeEvent<HTMLInputElement>) => setSearchKey(e.target.value)} />
              <button type={'submit'}>Search</button>
            </form>
          </div>
          <div></div>
        </header>
        <div className="imageHeader" style={{ backgroundImage: `url('${IMAGE_PATH}${selectedVideo.backdrop_path}')` }}>
          <div className="imageHeader-content max-center">
            {selectedVideo.videos && playTrailer ? renderTrailer() : null}
            <h1 className={'imageHeader-title'}>{selectedVideo.title}</h1>
            {selectedVideo.overview ? <p className={'imageHeader-overview'}>{selectedVideo.overview}</p> : null}
            <button className={'btnPlay-Close'} onClick={() => setPlayTrailer(true)}>
              Play
            </button>
            <button className={'btnPlay-Close'} onClick={() => setPlayTrailer(false)}>
              Close
            </button>
          </div>
          <div></div>
        </div>

        <div className="container">{renderPosters()}</div>
      </div>

      <Col md="1">{account?.login ? <div /> : <Alert color="light" />}</Col>
    </>
  );
}

export default Home;
