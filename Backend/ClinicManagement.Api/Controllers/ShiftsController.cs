using System;
using System.Threading.Tasks;
using ClinicManagement.Application.Visits.Commands.CallNextPatient;
using MediatR;
using Microsoft.AspNetCore.Mvc;

namespace ClinicManagement.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ShiftsController : ControllerBase
    {
        private readonly IMediator _mediator;
        private readonly ClinicManagement.Application.Common.Interfaces.IApplicationDbContext _context;

        public ShiftsController(IMediator mediator, ClinicManagement.Application.Common.Interfaces.IApplicationDbContext context)
        {
            _mediator = mediator;
            _context = context;
        }

        [HttpGet]
        public IActionResult GetAvailableShifts([FromQuery] Guid doctorId, [FromQuery] DateTime date)
        {
            // For testing, just return all active shifts
            var shifts = System.Linq.Enumerable.Select(_context.DoctorShifts, s => new
            {
                ShiftId = s.Id,
                ShiftName = s.ShiftName,
                StartTime = s.StartTime.ToString(@"hh\:mm"),
                EndTime = s.EndTime.ToString(@"hh\:mm"),
                CurrentQueueSize = s.NextAvailableQueueNumber - 1
            });
            return Ok(shifts);
        }

        [HttpPost("{id}/call-next")]
        public async Task<IActionResult> CallNextPatient(Guid id)
        {
            try
            {
                var visitId = await _mediator.Send(new CallNextPatientCommand(id));
                return Ok(new { VisitId = visitId });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }
}
