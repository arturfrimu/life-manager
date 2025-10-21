interface Exercise {
    id: string;
    name: string;
    type: string;
    imageUrl: string;
    description: string;
    createdByUserId: string;
    created: string;
    updated: string;
}

interface Workout {
    id: string;
    userId: string;
    name: string;
    notes: string;
    startedAt: string;
    completedAt: string;
    created: string;
    updated: string;
}