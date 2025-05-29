export class ApiService {
  private static instance: ApiService;

  private constructor() {}

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
}

const weekData = {
  gym: [
    {
      title: "Week 1 - gym",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 2",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 3",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      title: "Week 2 - gym",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 2",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      title: "Week 3 - gym",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 2",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      title: "Week 4 - gym",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 2",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 3",
          description: "description",
          type: "lower",
        },
      ],
    },
  ],
  house: [
    {
      title: "Week 1 - house",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 2",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 3",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      title: "Week 2 - house",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 2",
          description: "description",
          type: "lower",
        },
      ],
    },
    {
      title: "Week 3 - house",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 2",
          description: "description",
          type: "upper",
        },
      ],
    },
    {
      title: "Week 4 - house",
      cards: [
        {
          name: "Day 1",
          description: "description",
          type: "upper",
        },
        {
          name: "Day 2",
          description: "description",
          type: "lower",
        },
        {
          name: "Day 3",
          description: "description",
          type: "upper",
        },
      ],
    },
  ],
};

export type WeekData = typeof weekData;
