import { ThemedText } from "./ThemedText";
import { Image } from "./ui/image";
import { ThemedView } from "./ThemedView";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Badge, BadgeIcon, BadgeText } from "./ui/badge";
import { View } from "react-native";
import { BicepsFlexedIcon, FlameIcon, MapPinIcon } from "lucide-react-native";
import { Icon } from "./ui/icon";
import { Link } from "expo-router";

export interface WeekCardProps {
  image: string;
  title: string;
  type: "Workout" | "Warmup";
  subTitle: string;
}

const WeekCard = ({ image, title, type, subTitle }: WeekCardProps) => {
  const workoutColor = useThemeColor(
    { light: "#D5D5FF", dark: "#5555CB" },
    "background"
  );

  const warmupColor = useThemeColor(
    { light: "#FFF9F0", dark: "#f9b43c" },
    "background"
  );

  const defaultColors: {
    [key in WeekCardProps["type"]]: {
      card: string;
      text: string;
      icon: React.ElementType;
    };
  } = {
    Workout: {
      card: workoutColor,
      text: "#5555CB",
      icon: BicepsFlexedIcon,
    },
    Warmup: {
      card: warmupColor,
      text: "#A05E03",
      icon: FlameIcon,
    },
  };

  const excerciseType = defaultColors[type];

  return (
    <Link href="/exercise/1">
      <ThemedView
        style={{
          backgroundColor: excerciseType.card,
        }}
        className="rounded-xl py-4 px-8"
      >
        <View className="flex flex-row items-start justify-between gap-8">
          <View>
            <ThemedText className="text-xl font-bold">{title}</ThemedText>
            <ThemedText className="text-gray-500">{subTitle}</ThemedText>
          </View>

          <View className="flex flex-col items-end justify-center">
            <Badge
              size="sm"
              variant="solid"
              action="muted"
              className="rounded-full bg-white p-2"
            >
              <BadgeText
                style={{
                  color: excerciseType.text,
                }}
                className="font-bold ml-1"
              >
                {type}
              </BadgeText>
              <BadgeIcon
                as={excerciseType.icon}
                color={excerciseType.text}
                className="ml-1"
              />
            </Badge>
          </View>
        </View>

        <View className="flex flex-row items-end justify-between gap-12">
          <View className="flex flex-row items-center gap-1">
            <Icon size="md" as={MapPinIcon} />
            <ThemedText type="defaultSemiBold" className="text-gray-500">
              Gym
            </ThemedText>
          </View>

          <Image
            source={image}
            size="xl"
            resizeMode="contain"
            alt="Week Image"
          />
        </View>
      </ThemedView>
    </Link>
  );
};

export default WeekCard;
