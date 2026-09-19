namespace ClinicManagement.Domain.Enums
{
    public enum VisitStatus
    {
        PendingPayment = 0,
        PaymentUnderReview = 1,
        PaymentConfirmed = 2,
        Completed = 3,
        Cancelled = 4,
        RefundDue = 5
    }
}
