export class ApiService {
  private static instance: ApiService;

  private constructor() { }

  public static getInstance(): ApiService {
    if (!ApiService.instance) {
      ApiService.instance = new ApiService();
    }
    return ApiService.instance;
  }

  public getWeekData(): Promise<typeof weekData> {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(weekData);
      }, 1000);
    });
  }

  public getExerciseData(exerciseId: string): Promise<typeof exerciseData> {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(exerciseData.filter((exercise) => exercise.id === exerciseId));
      }, 1000);
    });
  }
}

const weekData = {
  gym: [
    {
      id: "1",
      title: "Week 1 - gym",
      cards: [
        {
          id: "1",
          name: "Day 1",
          description: "Focusing on chest",
          type: "upper",
        },
        {
          id: "2",
          name: "Day 2",
          description: "description",
          type: "lower",
        },
        {
          id: "3",
          name: "Day 3",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      id: "2",
      title: "Week 2 - gym",
      cards: [
        {
          id: "4",
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          id: "5",
          name: "Day 2",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      id: "3",
      title: "Week 3 - gym",
      cards: [
        {
          id: "6",
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          id: "7",
          name: "Day 2",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      id: "4",
      title: "Week 4 - gym",
      cards: [
        {
          id: "8",
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          id: "9",
          name: "Day 2",
          description: "description",
          type: "upper",
        },
        {
          id: "10",
          name: "Day 3",
          description: "description",
          type: "lower",
        },
      ],
    },
  ],
  house: [
    {
      id: "5",
      title: "Week 1 - house",
      cards: [
        {
          id: "12",
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          id: "13",
          name: "Day 2",
          description: "description",
          type: "upper",
        },
        {
          id: "14",
          name: "Day 3",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      id: "6",
      title: "Week 2 - house",
      cards: [
        {
          id: "15",
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          id: "16",
          name: "Day 2",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      id: "7",
      title: "Week 3 - house",
      cards: [
        {
          id: "17",
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          id: "18",
          name: "Day 2",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      id: "8",
      title: "Week 4 - house",
      cards: [
        {
          id: "19",
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          id: "20",
          name: "Day 2",
          description: "description",
          type: "lower",
        },
        {
          id: "21",
          name: "Day 3",
          description: "description",
          type: "upper",
        },
      ],
    },
  ],
};

export type WeekDataProps = typeof weekData;

const exerciseData = [
  {
    id: "1",
    title: "Upper Body Workout",
    warmup: [
      {
        title: "Baby pogos",
        description: "A quick warmup to get your muscles ready.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Arm circles",
        description: "Loosen up those shoulders with some arm circles.",
        videoId: "2g811Eo7K8U",
      },
    ],

    workout: [
      {
        title: "Push-ups",
        description: "A classic exercise for building upper body strength.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Pull-ups",
        description: "Great for your back and biceps.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Dumbbell Shoulder Press",
        description: "Strengthen your shoulders with this exercise.",
        videoId: "2g811Eo7K8U",
      },
    ],
  },
  {
    id: "2",
    title: "Lower Body Workout",
    warmup: [
      {
        title: "Baby pogos",
        description: "A quick warmup to get your muscles ready.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Arm circles",
        description: "Loosen up those shoulders with some arm circles.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Push-ups",
        description: "A classic exercise for building upper body strength.",
        videoId: "2g811Eo7K8U",
      }
    ],
    workout: [
      {
        title: "Squats",
        description: "A classic exercise for building lower body strength.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Lunges",
        description: "Great for your quads and hamstrings.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Dumbbell Calf Raises",
        description: "Strengthen your calves with this exercise.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Dumbbell Leg Raises",
        description: "Strengthen your legs with this exercise.",
        videoId: "2g811Eo7K8U",
      },
      {
        title: "Dumbbell Lunges",
        description: "Strengthen your legs with this exercise.",
        videoId: "2g811Eo7K8U",
      }
    ]
  }
];

export type ExerciseDataProps = typeof exerciseData;
