import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { useLocalSearchParams, useNavigation } from "expo-router";
import { useEffect, useState } from "react";
import { ActivityIndicator, Alert, ScrollView, View } from "react-native";
import { Exercise } from "@/components/shared/Exercise";
import { BicepsFlexedIcon, FlameIcon } from "lucide-react-native";
import { Badge, BadgeIcon, BadgeText } from "@/components/ui/badge";
import { ApiService, ExerciseDataProps } from "@/services/api";

const ExerciseScreen = () => {
  const navigation = useNavigation();
  const theme = useColorScheme();
  const colors = Colors[theme!];
  const [data, setData] = useState<ExerciseDataProps[0]>();

  const { id } = useLocalSearchParams();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const exerciseData = await ApiService.getInstance().getExerciseData(
          id as string
        );
        setData(exerciseData[0]);
      } catch (error) {
        Alert.alert(
          "Error fetching week data:",
          error instanceof Error ? error.message : "Unknown error"
        );
      }
    };
    fetchData();
  }, [data, id]);

  useEffect(() => {
    navigation.setOptions({
      headerShown: true,
      headerTitle: "",
      headerBackTitle: "Back",
      headerShadowVisible: false,
      headerTitleAlign: "center",
      headerStyle: {
        backgroundColor: colors.background,
      },
      headerBackTitleVisible: false,
      headerBackButtonMenuEnabled: false,
      headerBackButtonMenuVisible: false,
    });
  }, [colors, navigation]);

  if (!data) {
    return (
      <ThemedView className="flex-1 items-center justify-center">
        <ActivityIndicator size="large" color="#5555CB" />
      </ThemedView>
    );
  }

  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
      <ThemedView className="flex-1 px-8 pb-20 gap-20">
        <View>
          <ThemedText
            type="title"
            className="text-center font-bold text-2xl mt-4 mb-12"
          >
            {data.title}
          </ThemedText>

          <ThemedText>
            <Badge
              size="lg"
              variant="solid"
              action="muted"
              className="rounded-full bg-white p-2"
            >
              <BadgeText className="font-bold ml-1 text-[#A05E03] text-md">
                Warmup
              </BadgeText>
              <BadgeIcon as={FlameIcon} color="#A05E03" className="ml-1" />
            </Badge>
          </ThemedText>
          {data.warmup.map((warmup, index) => (
            <Exercise.Accordion
              key={index}
              title={warmup.title}
              videoId={warmup.videoId}
              reverseOrder={index % 2 === 0}
            >
              {warmup.description}
            </Exercise.Accordion>
          ))}
        </View>

        <View>
          <ThemedText>
            <Badge
              size="lg"
              variant="solid"
              action="muted"
              className="rounded-full bg-white p-2"
            >
              <BadgeText className="font-bold ml-1 text-[#5555CB] text-md">
                Workout
              </BadgeText>
              <BadgeIcon
                as={BicepsFlexedIcon}
                color="#5555CB"
                className="ml-1"
              />
            </Badge>
          </ThemedText>

          {data.workout.map((workout, index) => (
            <Exercise.Accordion
              key={index}
              title={workout.title}
              videoId={workout.videoId}
              reverseOrder={index % 2 === 0}
            >
              {workout.description}
            </Exercise.Accordion>
          ))}
        </View>
      </ThemedView>
    </ScrollView>
  );
};

export default ExerciseScreen;
