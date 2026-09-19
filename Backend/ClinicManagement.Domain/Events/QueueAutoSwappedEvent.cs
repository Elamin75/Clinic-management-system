using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Events
{
    public class QueueAutoSwappedEvent : IDomainEvent
    {
        public Guid AbsentVisitId { get; }
        public Guid PresentVisitId { get; }
        public int NewQueueNumberOfAbsentPatient { get; }
        public DateTime OccurredOn { get; }

        public QueueAutoSwappedEvent(Guid absentVisitId, Guid presentVisitId, int newQueueNumberOfAbsentPatient)
        {
            AbsentVisitId = absentVisitId;
            PresentVisitId = presentVisitId;
            NewQueueNumberOfAbsentPatient = newQueueNumberOfAbsentPatient;
            OccurredOn = DateTime.UtcNow;
        }
    }
}
