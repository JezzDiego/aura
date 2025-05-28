import { PropsWithChildren } from "react";
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

import { Video } from "@/components/shared/VideoPlayer";
import { View } from "react-native";

const ExerciseAccordion = ({
  title,
  videoId,
  reverseOrder = false,
  children,
}: PropsWithChildren & {
  title: string;
  videoId?: string;
  reverseOrder?: boolean;
}) => {
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
          <AccordionContent
            className={`flex ${
              reverseOrder ? "flex-col-reverse" : "flex-col"
            } gap-4 items-center justify-center mt-4`}
          >
            {videoId && <Video.Player videoId={videoId} />}
            <AccordionContentText>{children}</AccordionContentText>
          </AccordionContent>
        </AccordionItem>
      </View>
    </Accordion>
  );
};

export default ExerciseAccordion;
