--liquibase formatted sql

--changeset order-service:010-seed-bars-and-shots
INSERT INTO bars (id, bar_name, address) VALUES
('7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'Neon Mule', 'Lenina 12'),
('f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Rocket Shot Bar', 'Sovetskaya 21'),
('7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Velvet Hangar', 'Pushkina 48');

INSERT INTO bar_menu_items (id, bar_id, name, price, discount_percent) VALUES
('0fe72fb1-b736-4ea8-a4de-ebf5d8f49e77', '7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'B-52', 280.00, 0),
('76fe1d3c-7505-44df-8435-a50f4c4790bb', '7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'Kamikaze', 240.00, 0),
('6f49452f-2e46-47a7-9108-c30901a7e851', '7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'Green Mexican', 260.00, 0),
('8ea16dd6-f9f3-4676-a98e-f9bc42bf5fb5', '7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'Hiroshima', 310.00, 0),
('20c95f0f-fb4e-4111-941f-a1c8442d8ec2', '7f1fd5d3-1f50-4dd0-a31a-06d8967e4782', 'Boyarsky', 220.00, 0),

('430cc0f4-a573-4dcf-ba60-6a839f858169', 'f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Jagerbomb', 350.00, 0),
('f24db2f1-ab56-413b-a9fa-cfd0ad500790', 'f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Lemon Drop', 250.00, 0),
('4f8275d6-89f5-45cd-8af1-10f584f1b2f8', 'f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Tequila Slammer', 290.00, 0),
('c8d2cf2b-a4d5-4664-8d1c-e5f46b33a598', 'f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Orgasm', 300.00, 0),
('4f0d0f72-74a4-4f6f-8d2b-0f2bb8f5af68', 'f2f2e53d-0f33-4a44-a7a4-5770da372bf7', 'Mad Dog', 230.00, 0),

('849d89a6-3d8b-4463-b6b4-cf4ad6aee5f7', '7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Mexican Frog', 270.00, 0),
('ffe388af-8be8-4708-bff6-d86d70ddf0ec', '7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Woo Woo Shot', 245.00, 0),
('7a0b576c-b3f9-4d9f-ab2c-9c4efe22e336', '7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Baby Guinness', 285.00, 0),
('823cc8f6-9db0-4e5c-b596-f4599f1d35bb', '7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Red Snapper', 255.00, 0),
('f36d3bc7-cfe3-441c-a8cb-0f15f670517a', '7088fd7e-6c06-4bc7-95d9-05470ecff8d2', 'Blue Kamikaze', 295.00, 0);
