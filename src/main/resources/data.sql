INSERT INTO t_vehicle_type (id, name) VALUES
(1, 'CAR'),
(2, 'MOTORCYCLE'),
(3, 'TRUCK');

INSERT INTO t_parking_lot (lot_id, location, capacity, occupied_spaces) VALUES
('LOT-A', 'Downtown', 3, 3),
('LOT-B', 'Mall Area', 10, 0);

INSERT INTO t_vehicle (license_plate, owner_name, vehicle_type_id, lot_id) VALUES
('ABC-123', 'John Doe', '1', 'LOT-A'),
('XYZ-789', 'Jane Smith', '2', 'LOT-A'),
('TRK-456', 'Bob Lee', '3', 'LOT-A');
