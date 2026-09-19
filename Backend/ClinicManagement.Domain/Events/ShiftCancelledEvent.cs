using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Events
{
    public class ShiftCancelledEvent : IDomainEvent
    {
        public Guid ShiftId { get; }
        public string Reason { get; }
        public DateTime OccurredOn { get; }

        public ShiftCancelledEvent(Guid shiftId, string reason)
        {
            ShiftId = shiftId;
            Reason = reason;
            OccurredOn = DateTime.UtcNow;
        }
    }
}
