import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { useLocalSearchParams, useNavigation } from "expo-router";
import { useEffect } from "react";
import { ScrollView, View } from "react-native";
import { Exercise } from "@/components/shared/Exercise";
import { BicepsFlexedIcon, FlameIcon } from "lucide-react-native";
import { Badge, BadgeIcon, BadgeText } from "@/components/ui/badge";

const ExerciseScreen = () => {
  const navigation = useNavigation();
  const theme = useColorScheme();
  const colors = Colors[theme!];

  const { id } = useLocalSearchParams();

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

  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
      <ThemedView className="flex-1 px-8 gap-20">
        <View>
          <ThemedText
            type="title"
            className="text-center font-bold text-2xl mt-4 mb-12"
          >
            Day #{id}
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

          <Exercise.Accordion title="Exercise Details" videoId="2g811Eo7K8U">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem
            ipsum dolor sit amet consectetur adipisicing elit. Doloribus
            necessitatibus dolorum sunt tempora quod, rerum nemo repudiandae
            deserunt optio voluptatem. Mollitia rerum dolores, voluptates in
            facilis dolor cum blanditiis dolore.
          </Exercise.Accordion>
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

          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
          <Exercise.Accordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </Exercise.Accordion>
        </View>
      </ThemedView>
    </ScrollView>
  );
};

export default ExerciseScreen;
