using System;
using ClinicManagement.Domain.Common;

namespace ClinicManagement.Domain.Entities
{
    public class Doctor : Entity
    {
        public string Name { get; private set; }
        public string Specialty { get; private set; }
        
        public Doctor(Guid id, string name, string specialty)
        {
            Id = id;
            Name = name;
            Specialty = specialty;
        }

        private Doctor() { } // EF Core
    }
}
