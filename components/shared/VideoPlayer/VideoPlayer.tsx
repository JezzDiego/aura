import { useState } from "react";
import { ActivityIndicator, useWindowDimensions, View } from "react-native";
import YoutubeIframe from "react-native-youtube-iframe";

const VideoPlayer = ({ videoId }: { videoId: string }) => {
  const [ready, setReady] = useState(false);
  const videoWidth = useWindowDimensions().width - 96;
  const videoHeight = (9 / 16) * videoWidth;

  return (
    <View className="flex-1 items-center justify-center">
      <YoutubeIframe
        height={ready ? videoHeight : 0}
        width={videoWidth}
        videoId={videoId}
        onReady={() => setReady(true)}
        webViewProps={{
          allowsInlineMediaPlayback: true,
          mediaPlaybackRequiresUserAction: false,
          allowsFullscreenVideo: true,
          startInLoadingState: true,
        }}
      />
      {!ready && <ActivityIndicator size="large" color="#5555CB" />}
    </View>
  );
};

export default VideoPlayer;
