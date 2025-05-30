import { Platform, ScrollView, View } from "react-native";
import WeekCard from "./WeekCard";
import { ThemedText } from "../../ThemedText";

const images = {
  upper: require("@/assets/images/undraw_personal-trainer.png"),
  lower: require("@/assets/images/undraw_morning-workout.png"),
};

type WeekCardType = {
  id: string;
  title: string;
  cards: {
    id: string;
    name: string;
    description: string;
    type: string;
  }[];
};

export default function WeekList({ id, title, cards }: WeekCardType) {
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
        {cards.map((card) => (
          <WeekCard
            key={card.id}
            id={card.id}
            title={card.name}
            image={images[card.type as keyof typeof images]}
            type={card.type === "lower" ? "lower" : "upper"}
            subTitle={card.description}
          />
        ))}
      </ScrollView>
    </View>
  );
}
