import { Platform, ScrollView, View } from "react-native";
import WeekCard from "./WeekCard";
import { ThemedText } from "./ThemedText";

export default function WeekList() {
  const isWeb = Platform.OS === "web";

  return (
    <View>
      <ThemedText type="default" className="px-10 mb-2">
        Week 1 (20/04 - 26/04)
      </ThemedText>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={isWeb}
        contentContainerStyle={{
          paddingHorizontal: 32,
          gap: 16,
          marginBottom: isWeb ? 16 : 0,
        }}
      >
        <WeekCard
          title="Dia 1"
          image={require("@/assets/images/undraw_personal-trainer.png")}
          type="Workout"
          subTitle="Today at 2:15 PM"
        />
        <WeekCard
          title="Dia 2"
          image={require("@/assets/images/undraw_morning-workout.png")}
          type="Warmup"
          subTitle="Today at 2:99 PM"
        />
        <WeekCard
          title="Dia 3"
          image={require("@/assets/images/undraw_personal-trainer.png")}
          type="Workout"
          subTitle="Tomorrow at 7 AM"
        />
        <WeekCard
          title="Dia 4"
          image={require("@/assets/images/undraw_morning-workout.png")}
          type="Warmup"
          subTitle="Tomorrow at 7 AM"
        />
      </ScrollView>
    </View>
  );
}
