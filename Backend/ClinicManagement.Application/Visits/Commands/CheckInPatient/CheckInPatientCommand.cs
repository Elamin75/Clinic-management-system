using System;
using System.Threading;
using System.Threading.Tasks;
using ClinicManagement.Application.Common.Interfaces;
using MediatR;

namespace ClinicManagement.Application.Visits.Commands.CheckInPatient
{
    public record CheckInPatientCommand(Guid VisitId) : IRequest<Unit>;

    public class CheckInPatientCommandHandler : IRequestHandler<CheckInPatientCommand, Unit>
    {
        private readonly IApplicationDbContext _context;

        public CheckInPatientCommandHandler(IApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<Unit> Handle(CheckInPatientCommand request, CancellationToken cancellationToken)
        {
            var visit = await _context.Visits.FindAsync(new object[] { request.VisitId }, cancellationToken);
            
            if (visit == null)
                throw new Exception("Visit not found.");

            if (visit.Status != ClinicManagement.Domain.Enums.VisitStatus.PaymentConfirmed)
                throw new Exception("Patient must have payment confirmed to check in.");

            visit.MarkAsArrived();
            
            await _context.SaveChangesAsync(cancellationToken);

            return Unit.Value;
        }
    }
}
