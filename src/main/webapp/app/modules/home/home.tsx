import './home.scss';
// where we at now: need to match users json to only display id and login
// my best guess is leveraging the user-favorites-update way of getting the user and trying to understand that

// future stuff: might need to switch the url to the favorite making one in order for the json to go thru
import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { IUser } from 'app/shared/model/user.model';

import Axios from 'axios';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, createEntity, deleteEntity, reset } from 'app/entities/user-favorites/user-favorites.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayCurrentDateTime } from 'app/shared/util/date-utils';
import './VideoTile.css';
import './App.css';
/* eslint-disable */
function posterSearch() {
  const dispatch = useAppDispatch();
  //const users = useAppSelector(state => state.userManagement.users);

  // json friendly video list
  const favors = [];
  const userFavorites = useAppSelector(state => state.userFavorites.entity);
  //const [users, setUsers] = useState([]);
  //const [isNew] = useState(!props.match.params || !props.match.params.id);
  const [query, setquery] = useState(''); // use state is updating the value in the frontend
  const [videos, setvideos] = useState([]);
  const [showButton, setShowButton] = useState(false);
  const account = useAppSelector(state => state.authentication.account);
  // json friendly user formatter
  const user = (id, login) => {
    return { id: id, login: login };
  };
  // json friendly video formatter
  const video = id => {
    return { id: id };
  };
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
        dateAdded: displayCurrentDateTime(),
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
                        <button
                          type="submit"
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
        </div>
      </Col>
    </Row>
  );
}

export default posterSearch;
