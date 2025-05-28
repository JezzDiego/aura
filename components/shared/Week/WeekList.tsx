import { Platform, ScrollView, View } from "react-native";
import WeekCard from "./WeekCard";
import { ThemedText } from "../../ThemedText";

const images = {
  upper: require("@/assets/images/undraw_personal-trainer.png"),
  lower: require("@/assets/images/undraw_morning-workout.png"),
};

type WeekCardType = {
  title: string;
  cards: {
    name: string;
    description: string;
    type: string;
  }[];
};

export default function WeekList({ title, cards }: WeekCardType) {
  const isWeb = Platform.OS === "web";

  return (
    <View>
      <ThemedText type="subtitle" className="px-10 mb-6">
        {title}
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
        {cards.map((card, index) => (
          <WeekCard
            key={index}
            title={card.name}
            image={images[card.type as keyof typeof images]}
            type={card.type === "lower" ? "lower" : "upper"}
            subTitle={card.description}
          />
        ))}
        {/* <WeekCard
          title="Dia 1"
          image={require("@/assets/images/undraw_personal-trainer.png")}
          type="upper"
          subTitle="Today at 2:15 PM"
        />
        <WeekCard
          title="Dia 2"
          image={require("@/assets/images/undraw_morning-workout.png")}
          type="lower"
          subTitle="Today at 2:99 PM"
        />
        <WeekCard
          title="Dia 3"
          image={require("@/assets/images/undraw_personal-trainer.png")}
          type="upper"
          subTitle="Tomorrow at 7 AM"
        />
        <WeekCard
          title="Dia 4"
          image={require("@/assets/images/undraw_morning-workout.png")}
          type="lower"
          subTitle="Tomorrow at 7 AM"
        /> */}
      </ScrollView>
    </View>
  );
}
