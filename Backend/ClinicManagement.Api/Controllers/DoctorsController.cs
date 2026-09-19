using System;
using System.Linq;
using ClinicManagement.Application.Common.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace ClinicManagement.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class DoctorsController : ControllerBase
    {
        private readonly IApplicationDbContext _context;

        public DoctorsController(IApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult GetDoctors()
        {
            var doctors = _context.Doctors.Select(d => new
            {
                Id = d.Id,
                Name = d.Name,
                Specialty = d.Specialty
            }).ToList();

            return Ok(doctors);
        }

        [HttpGet("{id}")]
        public IActionResult GetDoctor(Guid id)
        {
            var doctor = _context.Doctors.FirstOrDefault(d => d.Id == id);
            
            if (doctor == null)
            {
                return NotFound();
            }

            return Ok(new
            {
                Id = doctor.Id,
                Name = doctor.Name,
                Specialty = doctor.Specialty
            });
        }
    }
}
