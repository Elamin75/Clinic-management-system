using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Events
{
    public class PatientCalledEvent : IDomainEvent
    {
        public Guid VisitId { get; }
        public Guid PatientId { get; }
        public Guid ShiftId { get; }
        public int QueueNumber { get; }
        public DateTime OccurredOn { get; }

        public PatientCalledEvent(Guid visitId, Guid patientId, Guid shiftId, int queueNumber)
        {
            VisitId = visitId;
            PatientId = patientId;
            ShiftId = shiftId;
            QueueNumber = queueNumber;
            OccurredOn = DateTime.UtcNow;
        }
    }
}
