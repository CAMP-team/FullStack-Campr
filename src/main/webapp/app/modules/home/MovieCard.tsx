import React from 'react';

const VideoCard = ({ video }) => {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/w500';

  return (
    <div className={'video-card'}>
      {video.poster_path ? <img className={'video-cover'} src={`${IMAGE_PATH}${video.poster_path}`} alt="" /> : null}

      <h5 className={'video.title'}>{video.title}</h5>
    </div>
  );
};

export default VideoCard;
