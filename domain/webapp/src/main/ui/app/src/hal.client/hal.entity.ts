export interface HalEntity{
    _links?: {
        [index: string]: {
            href: string;
        };
    };
    _embedded? : any;
}