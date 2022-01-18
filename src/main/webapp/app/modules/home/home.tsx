// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck
/* eslint-disable */
import { Col, Alert } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import './home.scss';
import './App.css';
import MovieCard from './MovieCard';
import axios from 'axios';
import React, { ChangeEvent, useEffect, useState } from 'react';
import Youtube from 'react-youtube';
import YouTube from 'react-youtube';
import ReactPlayer from 'react-player';

function Home() {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';
  const API_URL = 'https://api.themoviedb.org/3';
  const [movies, setMovies] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState([]);
  const [searchKey, setSearchKey] = useState('');
  // const [playTrailer,setPlayTrailer] = useState(false)

  const fetchMovies = async searchKey => {
    const type = searchKey ? 'search' : 'discover';
    const {
      data: { results },
    } = await axios.get(`${API_URL}/${type}/movie`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
        query: searchKey,
      },
    });

    setSelectedMovie(results[0]);
    setMovies(results);
  };

  const fetchMovie = async id => {
    const { data } = await axios.get(`${API_URL}/movie/${id}`, {
      params: {
        api_key: '616093e66ab252685ad921e5c4680152',
        append_to_response: 'videos',
      },
    });
    return data;
  };
  const selectmovie = async movie => {
    const data = await fetchMovie(movie.id);
    console.log(data);
    setSelectedMovie(movie);
  };

  useEffect(() => {
    fetchMovies(searchKey);
  }, []);

  const renderPosters = () => movies.map(movie => <MovieCard key={movie.id} movie={movie} selectmovie={selectmovie} />);
  const account = useAppSelector(state => state.authentication.account);
  const searchMovies = e => {
    e.preventDefault();
    fetchMovies(searchKey);
  };

  const renderTrailer = () => {
    const trailer = selectedMovie.videos.results.find(video => video.name === 'Official Trailer');
    console.log(trailer);

    return (
      <YouTube
        videoId={trailer.key}
        // containerClassName={"youtube-container"}
        // opts={{
        //   width: "100",
        //   height: "100%"
      />
    );
  };

  return (
    <>
      <div className="MovieApp">
        <header className={'header'}>
          <div className={'header-content max-center'}>
            <form onSubmit={searchMovies}>
              <input type="text" onChange={(e: ChangeEvent<HTMLInputElement>) => setSearchKey(e.target.value)} />
              <button type={'submit'}>Search</button>
            </form>
          </div>
        </header>
        <div className="imageHeader" style={{ backgroundImage: `url('${IMAGE_PATH}${selectedMovie.backdrop_path}')` }}>
          <div className="imageHeader-content max-center">
            {selectedMovie.videos ? renderTrailer() : null}
            {/* <button className={"button"} onClick={() =>setPlayTrailer(true)} >Play Trailer</button> */}
            <h1 className={'imageHeader-title'}>{selectedMovie.title}</h1>
            {selectedMovie.overview ? <p className={'imageHeader-overview'}>{selectedMovie.overview}</p> : null}
          </div>
        </div>

        <div className="container max-center">{renderPosters()}</div>
      </div>

      <Col md="9">{account?.login ? <div></div> : <Alert color="light"></Alert>}</Col>
    </>
  );
}

export default Home;
