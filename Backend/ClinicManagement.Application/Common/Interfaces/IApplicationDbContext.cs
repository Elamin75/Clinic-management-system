using System.Threading;
using System.Threading.Tasks;
using ClinicManagement.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace ClinicManagement.Application.Common.Interfaces
{
    public interface IApplicationDbContext
    {
        DbSet<Patient> Patients { get; }
        DbSet<DoctorShift> DoctorShifts { get; }
        DbSet<Visit> Visits { get; }
        DbSet<Doctor> Doctors { get; }

        Task<int> SaveChangesAsync(CancellationToken cancellationToken);
    }
}
