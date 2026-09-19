using System;
using System.Threading;
using System.Threading.Tasks;
using ClinicManagement.Application.Common.Interfaces;
using ClinicManagement.Domain.Entities;
using MediatR;

namespace ClinicManagement.Application.Visits.Commands.RegisterForShift
{
    public record RegisterForShiftCommand(Guid PatientId, Guid ShiftId) : IRequest<Guid>;

    public class RegisterForShiftCommandHandler : IRequestHandler<RegisterForShiftCommand, Guid>
    {
        private readonly IApplicationDbContext _context;

        public RegisterForShiftCommandHandler(IApplicationDbContext context)
        {
            _context = context;
        }

        public async Task<Guid> Handle(RegisterForShiftCommand request, CancellationToken cancellationToken)
        {
            var shift = await _context.DoctorShifts.FindAsync(new object[] { request.ShiftId }, cancellationToken);
            if (shift == null || shift.Status != ClinicManagement.Domain.Enums.ShiftStatus.Active)
            {
                throw new Exception("Shift not found or is inactive.");
            }

            var patient = await _context.Patients.FindAsync(new object[] { request.PatientId }, cancellationToken);
            if (patient == null)
            {
                throw new Exception("Patient not found.");
            }

            var visit = new Visit(patient.Id, shift.Id);
            
            _context.Visits.Add(visit);
            await _context.SaveChangesAsync(cancellationToken);

            return visit.Id;
        }
    }
}
