import React, { ChangeEvent } from 'react';
import { useState } from 'react';
import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import Axios from 'axios';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import './App.css';
import Header from 'app/shared/layout/header/header';
import MovieCard from './MovieCard';

/* eslint-disable */

function App() {
  const API_URL = 'https://api.themoviedb.org/3';
  const [movies, setMovies] = useState([]);
  const [searchKey, setSearchKey] = useState('');

  const fetchMovies = async searchKey => {
    const type = searchKey ? 'search' : 'discover';
    const {
      data: { results },
    } = await Axios.get(`${API_URL}/${type}/movie`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
        query: searchKey,
      },
    });

    setMovies(results);
  };

  useEffect(() => {
    fetchMovies(searchKey);
  }, []);

  const renderVideos = () => movies.map(movie => <MovieCard key={movie.id} video={movie} />);

  const searchMovies = e => {
    e.preventDefault();
    fetchMovies(searchKey);
  };

  const account = useAppSelector(state => state.authentication.account);
  return (
    <>
      <div className="MovieApp">
        <header>
          <h1>HelloYoutubue</h1>

          <form onSubmit={searchMovies}>
            <input type="text" onChange={(e: ChangeEvent<HTMLInputElement>) => setSearchKey(e.target.value)} />
            <button type={'submit'}>Search</button>
          </form>
        </header>
        {/* {searchKey}    */} {/* //Test to check search function */}
        <div className="container">{renderVideos()}</div>
      </div>

      <Col md="9">{account?.login ? <div></div> : <Alert color="light"></Alert>}</Col>
    </>
  );
}

export default App;
