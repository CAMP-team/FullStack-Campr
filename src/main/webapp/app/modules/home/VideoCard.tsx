import React from 'react';

const VideoCard = ({ video, selectvideo }) => {
  const IMAGE_PATH = 'https://image.tmdb.org/t/p/original';

  return (
    <div className={'video-card'} onClick={() => selectvideo(video)}>
      {video.poster_path ? (
        <img className={'video-cover'} src={`${IMAGE_PATH}${video.poster_path}`} alt="" />
      ) : (
        <div className={'video-placeholder'}> No Image Found </div>
      )}
      <h5 className={'video.title'}>{video.title}</h5>
    </div>
  );
};

export default VideoCard;
