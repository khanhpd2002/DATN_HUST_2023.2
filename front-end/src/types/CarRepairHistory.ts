export interface ICarRepairHistory {
    customerId: number;
    customerName: string;
    repairOrderId: number;
    repairOrderDescription: string;
    appointmentDate: string;
    totalPrice: number;
    typeService: string;
}