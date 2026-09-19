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

        public ShiftsController(IMediator mediator)
        {
            _mediator = mediator;
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
