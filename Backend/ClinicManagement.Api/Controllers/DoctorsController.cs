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
                id = d.Id,
                name = d.Name,
                specialization = d.Specialty,
                bio = "Passionate about healthcare and providing compassionate care to all patients.",
                rating = 4.8,
                reviewCount = 1200,
                clinicLocation = "NYC Medical Center",
                imageUrl = "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?auto=format&fit=crop&q=80&w=300&h=300"
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
