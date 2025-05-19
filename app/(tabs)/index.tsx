import { Image, Platform, StyleSheet, View } from "react-native";

import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { SafeAreaView } from "react-native-safe-area-context";
import WeekList from "@/components/WeekList";
import { StatusBar } from "expo-status-bar";

export default function HomeScreen() {
  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: "#fff", dark: "#fff" }}
      headerImage={
        <View className="flex items-center justify-center">
          {Platform.OS === "android" && (
            <SafeAreaView
              className="absolute top-0 left-0 right-0"
              style={{
                backgroundColor: "rgba(0, 0, 0, 0.5)",
              }}
            />
          )}

          <Image
            source={require("@/assets/images/undraw_athletes-training_koqa.png")}
            className="w-full h-full"
            resizeMode="contain"
            alt="indoor bike"
          />
        </View>
      }
    >
      <StatusBar style="dark" />
      <ThemedView style={styles.titleContainer}>
        <View className="flex flex-row items-center gap-2">
          <ThemedText type="title">Welcome to Aura!</ThemedText>
          <HelloWave />
        </View>

        <ThemedText type="subtitle" style={{ fontWeight: "400", fontSize: 16 }}>
          Your Aesthetic Unlimited Revolution Application program 🚀
        </ThemedText>
      </ThemedView>
      <ThemedView style={styles.stepContainer} className="gap-0 mb-0 mt-2">
        <ThemedText type="subtitle">Upcoming workouts</ThemedText>
      </ThemedView>
      <WeekList />
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    alignItems: "flex-start",
    paddingHorizontal: 32,
    gap: 8,
  },
  stepContainer: {
    paddingHorizontal: 32,
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
});
