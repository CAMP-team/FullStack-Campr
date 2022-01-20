export interface IPaginationBaseState {
    itemsPerPage: number;
    sort: string;
    order: string;
    activePage: number;
}
export declare const getSortState: (location: {
    search: string;
}, itemsPerPage: number, idField: string) => IPaginationBaseState;
/**
 * Retrieve new data when infinite scrolling
 * @param currentData
 * @param incomingData
 * @param links
 */
export declare const loadMoreDataWhenScrolled: (currentData: any, incomingData: any, links: any) => any;
