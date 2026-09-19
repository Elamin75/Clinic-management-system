using System;
using System.Collections.Generic;
using ClinicManagement.Domain.Common;
using ClinicManagement.Domain.Enums;

namespace ClinicManagement.Domain.Entities
{
    public class DoctorShift : Entity
    {
        public Guid DoctorId { get; private set; }
        public DateTime Date { get; private set; }
        public string ShiftName { get; private set; }
        public TimeSpan StartTime { get; private set; }
        public TimeSpan EndTime { get; private set; }
        public ShiftStatus Status { get; private set; }
        
        public int NextAvailableQueueNumber { get; private set; } = 1;
        public int CurrentServingQueueNumber { get; private set; } = 0;
        public int MaxQueueSize { get; private set; }

        private readonly List<Visit> _visits = new();
        public IReadOnlyCollection<Visit> Visits => _visits.AsReadOnly();

        private DoctorShift() { }

        public DoctorShift(Guid doctorId, DateTime date, string shiftName, TimeSpan startTime, TimeSpan endTime, int maxQueueSize)
        {
            Id = Guid.NewGuid();
            DoctorId = doctorId;
            Date = date.Date;
            ShiftName = shiftName;
            StartTime = startTime;
            EndTime = endTime;
            MaxQueueSize = maxQueueSize;
            Status = ShiftStatus.Active;
        }

        public int GenerateNextQueueNumber()
        {
            if (NextAvailableQueueNumber > MaxQueueSize)
                throw new InvalidOperationException("Shift queue is full.");
                
            return NextAvailableQueueNumber++;
        }
        
        public void UpdateCurrentServing(int queueNumber)
        {
            CurrentServingQueueNumber = queueNumber;
        }

        public void CancelShift(string reason)
        {
            Status = ShiftStatus.Cancelled;
            // The application layer will handle iterating through visits and marking them for refund
            // and dispatching ShiftCancelledEvent
        }
    }
}
