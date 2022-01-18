import React from 'react';

const MovieCard = ({ movie, selectmovie }) => {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';

  return (
    <div className={'movie-card'} onClick={() => selectmovie(movie)}>
      {movie.poster_path ? (
        <img className={'movie-cover'} src={`${IMAGE_PATH}${movie.poster_path}`} alt="" />
      ) : (
        <div className={'movie-placeholder'}> No Image Found </div>
      )}
      <h5 className={'movie.title'}>{movie.title}</h5>
    </div>
  );
};

export default MovieCard;
