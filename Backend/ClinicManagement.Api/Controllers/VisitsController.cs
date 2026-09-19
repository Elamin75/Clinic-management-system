using System;
using System.Threading.Tasks;
using ClinicManagement.Application.Visits.Commands.CheckInPatient;
using ClinicManagement.Application.Visits.Commands.RegisterForShift;
using MediatR;
using Microsoft.AspNetCore.Mvc;

namespace ClinicManagement.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class VisitsController : ControllerBase
    {
        private readonly IMediator _mediator;

        public VisitsController(IMediator mediator)
        {
            _mediator = mediator;
        }

        [HttpPost]
        public async Task<IActionResult> RegisterForShift([FromBody] RegisterForShiftRequest request)
        {
            try
            {
                var command = new RegisterForShiftCommand(request.PatientId, request.ShiftId);
                var visitId = await _mediator.Send(command);
                return Ok(new { VisitId = visitId, Status = "PENDING_PAYMENT" });
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }

        [HttpPost("{id}/check-in")]
        public async Task<IActionResult> CheckIn(Guid id)
        {
            try
            {
                await _mediator.Send(new CheckInPatientCommand(id));
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(new { Error = ex.Message });
            }
        }
    }

    public class RegisterForShiftRequest
    {
        public Guid PatientId { get; set; }
        public Guid ShiftId { get; set; }
    }
}
