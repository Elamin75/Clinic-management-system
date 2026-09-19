using System;
using ClinicManagement.Domain.Common;
using ClinicManagement.Domain.Enums;

namespace ClinicManagement.Domain.Entities
{
    public class Visit : Entity
    {
        public Guid PatientId { get; private set; }
        public Guid ShiftId { get; private set; }
        public int? QueueNumber { get; private set; }
        public VisitStatus Status { get; private set; }
        public string? PaymentReceiptUrl { get; private set; }
        public string? BankTransactionId { get; private set; }
        public bool IsCheckedIn { get; private set; }

        private Visit() { }

        public Visit(Guid patientId, Guid shiftId)
        {
            Id = Guid.NewGuid();
            PatientId = patientId;
            ShiftId = shiftId;
            Status = VisitStatus.PendingPayment;
            IsCheckedIn = false;
        }

        public void UploadReceipt(string receiptUrl, string transactionId)
        {
            if (Status != VisitStatus.PendingPayment)
                throw new InvalidOperationException("Cannot upload receipt for current status.");

            PaymentReceiptUrl = receiptUrl;
            BankTransactionId = transactionId;
            Status = VisitStatus.PaymentUnderReview;
        }

        public void ConfirmPayment(int generatedQueueNumber)
        {
            if (Status != VisitStatus.PaymentUnderReview && Status != VisitStatus.PendingPayment)
                throw new InvalidOperationException("Cannot confirm payment from current status.");

            Status = VisitStatus.PaymentConfirmed;
            QueueNumber = generatedQueueNumber;
        }

        public void RejectPayment()
        {
            if (Status != VisitStatus.PaymentUnderReview)
                throw new InvalidOperationException("Can only reject payments under review.");

            Status = VisitStatus.PendingPayment;
            PaymentReceiptUrl = null;
            BankTransactionId = null;
        }

        public void MarkAsArrived()
        {
            IsCheckedIn = true;
        }

        public void ChangeQueueNumber(int newNumber)
        {
            QueueNumber = newNumber;
        }

        public void MarkForRefund()
        {
            if (Status == VisitStatus.PaymentConfirmed)
            {
                Status = VisitStatus.RefundDue;
            }
        }
    }
}
