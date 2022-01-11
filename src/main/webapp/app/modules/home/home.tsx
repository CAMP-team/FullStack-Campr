import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import Axios from 'axios';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import './VideoTile.css';
import './App.css';

/* eslint-disable */

function posterSearch() {
  const [query, setquery] = useState(''); // use state is updating the value in the frontend
  const [videos, setvideos] = useState([]);

  const posterUrl = `https://api.themoviedb.org/3/search/movie?&api_key=616093e66ab252685ad921e5c4680152&query=${query}`;

  const testUrl = `https://www.youtube.com/`;
  async function getPoster() {
    var result = await Axios.get(posterUrl);
    // return result.data;
    console.log(result.data);
    setvideos(result.data.results);
    // console.log(result.data);
  }

  const onSubmit = e => {
    e.preventDefault(); // prevent page from reloading
    getPoster();
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
                    <div
                      key={video.id}
                      className="VideoTile"
                      onClick={() => {
                        window.open(testUrl);
                      }}
                    >
                      <img
                        className="videoTile__img"
                        src={`https://image.tmdb.org/t/p/w185${video.poster_path}`}
                        alt="card image"
                        style={{ width: '100%', height: 360 }}
                      />
                    </div>
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
            <div></div>
          </div>
        )}
      </Col>
    </Row>
  );
}

export default posterSearch;
