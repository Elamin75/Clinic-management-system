using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Entities
{
    public class Patient : Entity
    {
        public string PhoneNumber { get; private set; }
        public string? NationalId { get; private set; }
        public string FullName { get; private set; }
        public DateTime DateOfBirth { get; private set; }

        private Patient() { } // EF Core

        public Patient(string phoneNumber, string fullName, DateTime dateOfBirth, string? nationalId = null)
        {
            Id = Guid.NewGuid();
            PhoneNumber = phoneNumber ?? throw new ArgumentNullException(nameof(phoneNumber));
            FullName = fullName ?? throw new ArgumentNullException(nameof(fullName));
            DateOfBirth = dateOfBirth;
            NationalId = nationalId;
        }

        public void UpdateDetails(string fullName, string? nationalId)
        {
            FullName = fullName;
            NationalId = nationalId;
        }
    }
}
