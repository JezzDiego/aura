import { ThemedText } from "../../ThemedText";
import { Image } from "../../ui/image";
import { ThemedView } from "../../ThemedView";
import { useThemeColor } from "@/hooks/useThemeColor";
import { Badge, BadgeIcon, BadgeText } from "../../ui/badge";
import { View } from "react-native";
import { BicepsFlexedIcon, BoneIcon, MapPinIcon } from "lucide-react-native";
import { Icon } from "../../ui/icon";
import { Link } from "expo-router";

export interface WeekCardProps {
  id: string;
  image: string;
  title: string;
  type: "upper" | "lower";
  subTitle: string;
}

const WeekCard = ({ id, image, title, type, subTitle }: WeekCardProps) => {
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
    upper: {
      card: workoutColor,
      text: "#5555CB",
      icon: BicepsFlexedIcon,
    },
    lower: {
      card: warmupColor,
      text: "#A05E03",
      icon: BoneIcon,
    },
  };

  const excerciseType = defaultColors[type];

  return (
    <Link href={`/exercise/${id}`}>
      <ThemedView
        style={{
          backgroundColor: excerciseType.card,
        }}
        className="rounded-xl py-6 px-8"
      >
        <View className="flex flex-row items-start justify-between gap-8">
          <View>
            <ThemedText className="text-xl font-bold">{title}</ThemedText>
            <ThemedText className="text-gray-500 mt-4 max-w-64">{subTitle}</ThemedText>
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
