import {
  Image,
  Platform,
  StyleSheet,
  TouchableOpacity,
  useColorScheme,
  View,
} from "react-native";

import { useState } from "react";
import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { SafeAreaView } from "react-native-safe-area-context";
import { Week } from "@/components/shared/Week";
import { StatusBar } from "expo-status-bar";
import { Divider } from "@/components/ui/divider";
import { Icon } from "@/components/ui/icon";
import { HouseIcon, WeightIcon } from "lucide-react-native";
import { useThemeColor } from "@/hooks/useThemeColor";
import Animated, {
  FadeIn,
  FadeOut,
  LinearTransition,
} from "react-native-reanimated";
import api from "@/services/api";

type TabProps = "gym" | "house";

export default function HomeScreen() {
  const [tab, setTab] = useState<TabProps>("gym");

  const gymBorderColor = useThemeColor(
    { light: "#5555CB", dark: "#5555CB" },
    "background"
  );
  const houseBorderColor = useThemeColor(
    { light: "#f9b43c", dark: "#f9b43c" },
    "background"
  );

  const handleTabChange = (newTab: TabProps) => {
    setTab(newTab);
  };

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
      {/* <StatusBar style="auto" /> */}
      <ThemedView style={styles.titleContainer}>
        <View className="flex flex-row items-center gap-2">
          <ThemedText type="title">Welcome to Aura!</ThemedText>
          <HelloWave />
        </View>

        <ThemedText type="subtitle" style={{ fontWeight: "400", fontSize: 16 }}>
          Your Aesthetic Unlimited Revolution Application program 🚀
        </ThemedText>
      </ThemedView>
      <ThemedView className="flex-row items-center justify-between px-10 mb-4 mt-6">
        <TouchableOpacity
          className="border border-solid border-gray-500 rounded-lg px-8 py-2 flex flex-row items-center gap-2"
          onPress={() => handleTabChange("gym")}
          style={{
            borderColor: tab === "gym" ? gymBorderColor : "#687076",
          }}
        >
          <Icon as={WeightIcon} size="sm" />
          <ThemedText type="default" className="font-bold ">
            Gym
          </ThemedText>
        </TouchableOpacity>

        <Divider orientation="vertical" />

        <TouchableOpacity
          className="border border-solid border-gray-500 rounded-lg px-8 py-2 flex flex-row items-center gap-2"
          onPress={() => handleTabChange("house")}
          style={{
            borderColor: tab === "house" ? houseBorderColor : "#687076",
          }}
        >
          <Icon as={HouseIcon} size="sm" />
          <ThemedText type="default" className="font-bold">
            House
          </ThemedText>
        </TouchableOpacity>
      </ThemedView>

      {tab === "gym" && (
        <View className="flex-1 gap-12 mt-4">
          {api.gym.map((item, index) => (
            <Animated.View
              key={index}
              className="flex-1 gap-10"
              entering={FadeIn.duration(
                300 + (index * 300 <= 3000 ? index * 300 : 3000)
              )}
              exiting={FadeOut.duration(
                300 + (index * 300 <= 3000 ? index * 300 : 3000)
              )}
              layout={LinearTransition.springify()}
            >
              <Week.List key={index} title={item.title} cards={item.cards} />
              <Divider />
            </Animated.View>
          ))}
        </View>
      )}

      {tab === "house" && (
        <View className="flex-1 gap-12 mt-4">
          {api.house.map((item, index) => (
            <Animated.View
              key={index}
              className="flex-1 gap-10"
              entering={FadeIn.duration(
                300 + (index * 300 <= 3000 ? index * 300 : 3000)
              )}
              exiting={FadeOut.duration(
                300 + (index * 300 <= 3000 ? index * 300 : 3000)
              )}
              layout={LinearTransition.springify()}
            >
              <Week.List key={index} title={item.title} cards={item.cards} />
              <Divider />
            </Animated.View>
          ))}
        </View>
      )}
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    alignItems: "flex-start",
    paddingHorizontal: 32,
    gap: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
});
