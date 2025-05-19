import { Button, ButtonText } from "./ui/button";
import { Heading } from "./ui/heading";
import {
  Modal,
  ModalBackdrop,
  ModalContent,
  ModalCloseButton,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "./ui/modal";
import { Icon, CloseIcon } from "./ui/icon";
import React from "react";
import { Pressable, View } from "react-native";
import { WeekCardProps } from "./WeekCard";
import { ThemedText } from "./ThemedText";

type WeekDetailsProps = React.PropsWithChildren<{
  CardProps: WeekCardProps;
}>;

function WeekDetails({ children, CardProps }: WeekDetailsProps) {
  const [showModal, setShowModal] = React.useState(false);
  const { title, subTitle } = CardProps;

  return (
    <View>
      <Pressable onPress={() => setShowModal(true)}>{children}</Pressable>

      <Modal isOpen={showModal} onClose={() => setShowModal(false)} size="lg">
        <ModalBackdrop />
        <ModalContent>
          <ModalHeader>
            <Heading size="md" className="text-typography-950">
              Detalhes da {title.toLowerCase()}
            </Heading>
            <ModalCloseButton>
              <Icon
                as={CloseIcon}
                size="xl"
                className="stroke-background-400 group-[:hover]/modal-close-button:stroke-background-700 group-[:active]/modal-close-button:stroke-background-900 group-[:focus-visible]/modal-close-button:stroke-background-900"
              />
            </ModalCloseButton>
          </ModalHeader>

          <ModalBody>
            <ThemedText className="text-typography-500">{subTitle}</ThemedText>
          </ModalBody>

          <ModalFooter>
            <Button
              variant="outline"
              action="secondary"
              onPress={() => setShowModal(false)}
            >
              <ButtonText>Fechar</ButtonText>
            </Button>
            <Button onPress={() => setShowModal(false)}>
              <ButtonText>Começar</ButtonText>
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </View>
  );
}

export default WeekDetails;
