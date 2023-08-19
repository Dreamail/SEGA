--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: auth_logs; Type: TABLE; Schema: public; Owner: allnet; Tablespace: 
--

CREATE TABLE public.auth_logs (
    auth_seq character(10) NOT NULL,
    keychip_id character(11) NOT NULL,
    continue boolean,
    iv_keychip_challenge character(32),
    auth_keychip_challenge character(32),
    auth_result integer,
    cause text,
    request_1 character(1200),
    response_1 character(1200),
    request_2 character(1200),
    expires timestamp with time zone,
    response_2 character(1200),
    create_date timestamp without time zone DEFAULT now()
);


ALTER TABLE public.auth_logs OWNER TO allnet;

--
-- Data for Name: auth_logs; Type: TABLE DATA; Schema: public; Owner: allnet
--

COPY public.auth_logs (auth_seq, keychip_id, continue, iv_keychip_challenge, auth_keychip_challenge, auth_result, cause, request_1, response_1, request_2, expires, response_2, create_date) FROM stdin;
0000000002	TESTID00001	f	98228ec43c6e0c6397aee9f158c529e7	f30111da4260e0321b1e1466b09a2a65	0		packet_data=GgAAAAAAAAAAAAAAAAAAAcSoviV%2FQA2KqzX9FYjCKnUpzIK0yPCxSL0pzrY92yjm8nL4ieMBrlQqEQGBDmHPM0PpEz31kNICHCEzJPbD6Cg%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	{"packet_data":"tcRA9Eg\\/IpMBfDeAHcfc6IzFowv\\/wbHFxtsQhadmfAaN1DOBg5CTxXkiWJLG0yTIP3fcqq5HPbOxVQYT5p72bkZ6K7zW\\/YZ5\\/k5hzWDEC7g5B3Ek7eaburbzPI\\/Zx0p4","expires":"","verify_data":""}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           	packet_data=1LCID5ibltg0gm4bSuDwBW7s%2Bn%2FW7%2BcRhdT813vIQMNkesww1j2zRp381Js5peXJmAT52K7uPh2CuJxAClUvmg%3D%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  	2020-12-22 10:00:00+09	{"packet_data":"","expires":"2020-12-22T01:00:00Z","verify_data":"MIICTgYJKoZIhvcNAQcCoIICPzCCAjsCAQExDTALBglghkgBZQMEAgEwdAYJKoZIhvcNAQcBoGcEZXJlc3VsdD0wJmtleV9pZD1URVNUSUQwMDAwMSZsaW1pdD0yMDIwLTEyLTIyVDAxOjAwOjAwWiZyZW5ld2FsPTIwMjAtMTItMjFUMjI6MDA6MDBaJnNlcmlhbD0wMDAwMDAwMDAyMYIBrzCCAasCAQEwgYUwbTELMAkGA1UEBhMCSlAxDjAMBgNVBAgMBVRva3lvMRIwEAYDVQQHDAlNaW5hdG8ta3UxFjAUBgNVBAoMDVRFU1QgQ28uLEx0ZC4xDjAMBgNVBAsMBVNhbGVzMRIwEAYDVQQDDAl0ZXN0LnRlc3QCFBRPMbA8UeYXoqK\\/pp9ugjXQG4brMAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAQC68BOl7uNV8rRrJzH2ozpFUv2MLYlOukY\\/fxk8N9MJsxvdcXn4DTPhAxTt9AwDTZtvmUdM9K+qYAc3fk7G8RSJXtwzmJrGpsXx3bv\\/FRvYBu83LSA9VzYgS2Bcx0yn4QRlzD6Wt4dgDXy8dmohbG1ED\\/TZjQOLVcY3vV0cqOzCcjyxBoCXIfjQ8cV+U6SwkWp0odEBReY9hk3QfAo1JIUzyqCLWMH9rWUR7lP3gKahukqJik0QCLZGslBHHlUEL4LrbE+MoCkzMPVSe3aFzsDcDZIocGXDiY5L5d9j\\/Bih9TW5qybeimExM\\/lKPam3ZNUcZXFKMBfMu4S7GrrjQXGT"}                                                                                                                                                                                                                                                                                                                                              	2020-12-21 16:22:13.852
0000000003	TESTID00001	t	c7ef52d47b58f6f76e66d5320bb540be	2e50d4f3ec273e20aaf8cab7eb379f27	\N	\N	packet_data=GgAAAAAAAAAAAAAAAAAAAcSoviV%2FQA2KqzX9FYjCKnUpzIK0yPCxSL0pzrY92yjm8nL4ieMBrlQqEQGBDmHPM0PpEz31kNICHCEzJPbD6Cg%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	packet_data=tcRA9Eg/IpMBfDeAHcfc6IzFowv/wbHFxtsQhadmfAaN1DOBg5CTxXkiWJLG0yTIjH9eHTHG8PD7M1TtE79YJe3sRwuVb29YpJz9uqQNwWB3B/QMON3aTd7qvY5z5obA&auth_data=                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         	\N	\N	\N	2020-12-21 17:37:30.707
0000000004	TESTID00001	t	31abd8df09c6053f902bdd8a95726f57	0629f2eedb99d746624631656e2100bb	\N	\N	packet_data=GgAAAAAAAAAAAAAAAAAAAcSoviV%2FQA2KqzX9FYjCKnUpzIK0yPCxSL0pzrY92yjm8nL4ieMBrlQqEQGBDmHPM0PpEz31kNICHCEzJPbD6Cg%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	packet_data=tcRA9Eg/IpMBfDeAHcfc6IzFowv/wbHFxtsQhadmfAaN1DOBg5CTxXkiWJLG0yTIOo409ungwFTuWKng1LhHZh5fLYhUxUXXyVQTij4DzOqfN5yW2M0wDa/K5dB1DrOk&auth_data=                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         	\N	\N	\N	2020-12-21 19:13:08.819
0000000005	TESTID00001	t	6afd51c96209a64f9a31259d7ee1d389	6e26586ada290b2cc117ed17c48cf2de	0		packet_data=GgAAAAAAAAAAAAAAAAAAAcSoviV%2FQA2KqzX9FYjCKnUpzIK0yPCxSL0pzrY92yjm8nL4ieMBrlQqEQGBDmHPM0PpEz31kNICHCEzJPbD6Cg%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	packet_data=tcRA9Eg/IpMBfDeAHcfc6IzFowv/wbHFxtsQhadmfAaN1DOBg5CTxXkiWJLG0yTIS8jP7hNQPbtrVbbFW/DM2kvKbFThnToSEMKh+Dg/CbWlLO9VyskR1PzSHT/KKLJ3&auth_data=                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         	packet_data=lGA1vSPSjx8wgi2Mn5NGLyAKAsAMwpPhDA3ZXAhbY%2B6qWTTM6jh%2Bt%2FrN8Xoj%2B61z9PeHGbfWeFQMMR6C9%2B9TxQ%3D%3D                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              	2020-12-23 10:00:00+09	packet_data=&auth_data=MIICTgYJKoZIhvcNAQcCoIICPzCCAjsCAQExDTALBglghkgBZQMEAgEwdAYJKoZIhvcNAQcBoGcEZXJlc3VsdD0wJmtleV9pZD1URVNUSUQwMDAwMSZsaW1pdD0yMDIwLTEyLTIzVDAxOjAwOjAwWiZyZW5ld2FsPTIwMjAtMTItMjJUMjI6MDA6MDBaJnNlcmlhbD0wMDAwMDAwMDA1MYIBrzCCAasCAQEwgYUwbTELMAkGA1UEBhMCSlAxDjAMBgNVBAgMBVRva3lvMRIwEAYDVQQHDAlNaW5hdG8ta3UxFjAUBgNVBAoMDVRFU1QgQ28uLEx0ZC4xDjAMBgNVBAsMBVNhbGVzMRIwEAYDVQQDDAl0ZXN0LnRlc3QCFBRPMbA8UeYXoqK/pp9ugjXQG4brMAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAQCdAz2JVPYJ95X+EV/iu0FDJoAik06ciwRSFH4nQQbNSFtaUQmp2f/dSA0oy42s8ZmtXPK7bFRYGlQeYvFpPZmI6mAvUDJcvhx9hC/LvtYIOCdhUr2nErCqUmP7NcnqIII2e1SQmyBAIL6DWVK+P9Ty13k8heSbodxmtIFh/I/wMQC1mxx4sO3lA/x1jmLV8Lt5THUFthAszdsjF9p3KR5DBeh3GAIgVuA5NY005rxjOPgZV18vzjaCQ4dG45lOuMUgh2qXuSCyJ5KW4qje3xGdvwG6a7EFhn62f+W482T4vAXAnC89T7rn/wINAJiQlYqqFnVPTJzcl6OJG2aq4gpJ                                                                                                                                                                                                                                                                                                                                                                                                 	2020-12-22 07:10:54.757
\.


--
-- Name: auth_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: allnet; Tablespace: 
--

ALTER TABLE ONLY public.auth_logs
    ADD CONSTRAINT auth_logs_pkey PRIMARY KEY (auth_seq);


--
-- PostgreSQL database dump complete
--
