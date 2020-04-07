import { Item } from './item';

export class Project {
    id: string;
    name: string;
    startDate: Date;
    endDate: Date;
    bookedItems: Array<Item>;
}
