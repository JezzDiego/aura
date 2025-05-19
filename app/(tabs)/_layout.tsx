import { Tabs } from "expo-router";
import React from "react";
import { Platform } from "react-native";

import { HapticTab } from "@/components/HapticTab";
import TabBarBackground from "@/components/ui/TabBarBackground";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { Icon } from "@/components/ui/icon";
import { House, Telescope } from "lucide-react-native";

export default function TabLayout() {
  const colorScheme = useColorScheme();

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors[colorScheme ?? "light"].tint,
        headerShown: false,
        tabBarButton: HapticTab,
        tabBarBackground: TabBarBackground,
        tabBarLabelStyle: {
          fontFamily: "Nunito",
          fontWeight: "700",
        },
        tabBarStyle: Platform.select({
          ios: {
            overflow: "hidden",
            borderRadius: 20,
            position: "absolute",
            bottom: 30,
            left: 20,
            right: 20,
            height: 55,
            marginLeft: 25,
            marginRight: 25,
            borderTopWidth: 0,
            paddingTop: 10,
            backgroundColor: Colors[colorScheme ?? "light"].navbar,
          },
          android: {
            borderRadius: 10,
            position: "absolute",
            bottom: 25,
            left: 20,
            right: 20,
            elevation: 2,
            shadowColor: "#ffffff",
            shadowOffset: { width: 0, height: 4 },
            shadowOpacity: 0.2,
            shadowRadius: 8,
            height: 55,
            marginLeft: 20,
            marginRight: 20,
            paddingTop: 10,
            backgroundColor: Colors[colorScheme ?? "light"].navbar,
          },
          web: {
            borderRadius: 20,
            position: "absolute",
            bottom: 25,
            left: 20,
            right: 20,
            elevation: 2,
            shadowColor: "#000",
            shadowOffset: { width: 0, height: 4 },
            shadowOpacity: 0.2,
            shadowRadius: 8,
            height: 55,
            marginLeft: 20,
            marginRight: 20,
            paddingTop: 1,
            backgroundColor: "#F2F4F5",
          },
        }),
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: "",
          tabBarIcon: ({ color }) => (
            <Icon size="xl" as={House} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="explore"
        options={{
          title: "",
          tabBarIcon: ({ color }) => (
            <Icon size="xl" as={Telescope} color={color} />
          ),
        }}
      />
    </Tabs>
  );
}
