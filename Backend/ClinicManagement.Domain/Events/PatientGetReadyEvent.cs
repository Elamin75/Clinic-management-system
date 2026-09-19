using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Events
{
    public class PatientGetReadyEvent : IDomainEvent
    {
        public Guid VisitId { get; }
        public Guid PatientId { get; }
        public int QueueNumber { get; }
        public int SpotsAway { get; }
        public DateTime OccurredOn { get; }

        public PatientGetReadyEvent(Guid visitId, Guid patientId, int queueNumber, int spotsAway)
        {
            VisitId = visitId;
            PatientId = patientId;
            QueueNumber = queueNumber;
            SpotsAway = spotsAway;
            OccurredOn = DateTime.UtcNow;
        }
    }
}
