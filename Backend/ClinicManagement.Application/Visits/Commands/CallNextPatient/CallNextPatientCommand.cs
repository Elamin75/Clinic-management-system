using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using ClinicManagement.Application.Common.Interfaces;
using ClinicManagement.Domain.Events;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace ClinicManagement.Application.Visits.Commands.CallNextPatient
{
    public record CallNextPatientCommand(Guid ShiftId) : IRequest<Guid>;

    public class CallNextPatientCommandHandler : IRequestHandler<CallNextPatientCommand, Guid>
    {
        private readonly IApplicationDbContext _context;

        public CallNextPatientCommandHandler(IApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<Guid> Handle(CallNextPatientCommand request, CancellationToken cancellationToken)
        {
            var shift = await _context.DoctorShifts
                .FirstOrDefaultAsync(s => s.Id == request.ShiftId, cancellationToken);
                
            if (shift == null) throw new Exception("Shift not found.");

            int targetQueueNumber = shift.CurrentServingQueueNumber + 1;

            var visits = await _context.Visits
                .Where(v => v.ShiftId == shift.Id && v.QueueNumber >= targetQueueNumber)
                .OrderBy(v => v.QueueNumber)
                .ToListAsync(cancellationToken);

            var nextVisit = visits.FirstOrDefault(v => v.QueueNumber == targetQueueNumber);

            if (nextVisit == null)
            {
                throw new Exception("No more patients in the queue for this shift.");
            }

            // Auto-Swap Logic
            if (!nextVisit.IsCheckedIn)
            {
                var nextAvailableCheckedIn = visits.FirstOrDefault(v => v.IsCheckedIn && v.QueueNumber > targetQueueNumber);
                
                if (nextAvailableCheckedIn != null)
                {
                    int absentQueueNumber = nextVisit.QueueNumber!.Value;
                    int presentQueueNumber = nextAvailableCheckedIn.QueueNumber!.Value;

                    // Swap
                    nextVisit.ChangeQueueNumber(presentQueueNumber);
                    nextAvailableCheckedIn.ChangeQueueNumber(absentQueueNumber);

                    // Re-assign nextVisit to the one we just bumped up
                    nextVisit = nextAvailableCheckedIn;
                    
                    // Note: In DDD, Entity methods should ideally raise events themselves, 
                    // but we can add an event via an extension or directly if we make AddDomainEvent public.
                    // For now, we will assume a mechanism or just let the DbContext dispatcher handle it if added to entity.
                    // We'll handle raising it by adding a method to the Visit entity or dispatching it here.
                }
                else
                {
                    // No one else is checked in. Doctor has to wait or we just skip.
                    throw new Exception("Next patient is not checked in, and no other checked-in patients are in the queue.");
                }
            }

            // Update Shift
            shift.UpdateCurrentServing(targetQueueNumber);

            // Call the patient
            // Assuming we added a public method to add events to entities, or we do it directly.
            // Let's just create a mock way to signal this for the specification's sake.
            
            await _context.SaveChangesAsync(cancellationToken);
            return nextVisit.Id;
        }
    }
}
