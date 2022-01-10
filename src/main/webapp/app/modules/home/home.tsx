import './home.scss';
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import Axios from 'axios';
import { useAppSelector } from 'app/config/store';

import './VideoTile.css';
import './App.css';

function videoSearch() {
  const [query, setquery] = useState(''); // use state is updating the value in the frontend
  const [videos, setvideos] = useState([]);

  // const YOUR_APP_KEY = '616093e66ab252685ad921e5c4680152';

  const url =
    // `https://api.themoviedb.org/3/search/movie?api_key${YOUR_APP_KEY}=&q=${query}`;
    `https://api.themoviedb.org/3/search/movie?api_key=616093e66ab252685ad921e5c4680152&query=marvel`;
  async function getVideos() {
    const result = await Axios.get(url);
    setvideos(result.data.results);
    // console.log(result.data);
  }

  const onSubmit = e => {
    e.preventDefault(); // prevent page from reloading
    getVideos();
  };

  const account = useAppSelector(state => state.authentication.account);

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
        {account?.login ? (
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
                  return (
                    <>
                      <div
                        className="VideoTile"
                        onClick={() => {
                          window.open(video['video']['url']);
                        }}
                      >
                        <img src={`https://image.tmdb.org/t/p/w185${url}`} alt="card image" style={{ width: '100%', height: 360 }} />
                        <p className="videoTile__name">{video['video']['label']}</p>
                      </div>
                    </>
                  );
                })}
              </div>
            </Col>
            <Alert color="light"></Alert>
          </div>
        ) : (
          <div>
            <Alert color="light">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>

            <Alert color="light">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
            <div>
              <Translate contentKey="home.title">Welcome to Campr</Translate>
            </div>
          </div>
        )}
      </Col>
    </Row>
  );
}

export default videoSearch;
