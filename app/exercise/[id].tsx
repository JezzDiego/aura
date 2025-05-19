import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { useLocalSearchParams, useNavigation } from "expo-router";
import { PropsWithChildren, useEffect } from "react";
import { ScrollView, View } from "react-native";

import {
  Accordion,
  AccordionContent,
  AccordionContentText,
  AccordionHeader,
  AccordionIcon,
  AccordionItem,
  AccordionTitleText,
  AccordionTrigger,
} from "@/components/ui/accordion";
import {
  ChevronDownCircleIcon,
  ChevronUpCircleIcon,
} from "lucide-react-native";

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
            className="text-center font-bold text-2xl mt-8 mb-4"
          >
            Day #{id}
          </ThemedText>
          <ThemedText type="subtitle" className="font-normal text-base">
            Warmup
          </ThemedText>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
        </View>

        <View>
          <ThemedText type="subtitle" className="font-normal text-base">
            Workout
          </ThemedText>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
          <ExerciseAccordion title="Exercise Details">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua.
          </ExerciseAccordion>
        </View>
      </ThemedView>
    </ScrollView>
  );
};

export default ExerciseScreen;

const ExerciseAccordion = ({
  title,
  children,
}: PropsWithChildren & { title: string }) => {
  return (
    <Accordion className="mt-4" variant="unfilled">
      <View className="border-[0.5px] border-gray-200 rounded-xl">
        <AccordionItem value="item-1">
          <AccordionHeader>
            <AccordionTrigger>
              {({ isExpanded }: { isExpanded: boolean }) => {
                return (
                  <>
                    <AccordionTitleText>{title}</AccordionTitleText>
                    {isExpanded ? (
                      <AccordionIcon as={ChevronUpCircleIcon} />
                    ) : (
                      <AccordionIcon as={ChevronDownCircleIcon} />
                    )}
                  </>
                );
              }}
            </AccordionTrigger>
          </AccordionHeader>
          <AccordionContent>
            <AccordionContentText>{children}</AccordionContentText>
          </AccordionContent>
        </AccordionItem>
      </View>
    </Accordion>
  );
};
