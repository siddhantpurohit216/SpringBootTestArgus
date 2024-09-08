    package com.example.employeeManagement.Controllers;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import com.example.employeeManagement.Entities.Address;
    import com.example.employeeManagement.Services.AddressService;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/addresses")
    public class AddressController {

        private final AddressService addressService;

        @Autowired
        public AddressController(AddressService addressService) {
            this.addressService = addressService;
        }

        @GetMapping
        public List<Address> getAllAddresses() {
            return addressService.getAllAddresses();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
            Address address = addressService.getAddressById(id);
            if(address!=null)
            {
                return ResponseEntity.ok(address);
            }
            //The .build() method in ResponseEntity is used to construct and return a ResponseEntity object.
            return ResponseEntity.notFound().build();
        }

        @PostMapping("/{id}")
        public Address createAddress(@RequestBody Address address) {
            return addressService.createAddress(address);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
            Address updatedAddress = addressService.updateAddress(id, addressDetails);
            return ResponseEntity.ok(updatedAddress);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        }
    }
