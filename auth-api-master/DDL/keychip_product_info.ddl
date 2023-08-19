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
-- Name: keychip_product_info; Type: TABLE; Schema: public; Owner: allnet; Tablespace: 
--

CREATE TABLE public.keychip_product_info (
    keychip_id character(11) NOT NULL,
    key_server character(32),
    key_keychip character(32),
    create_date timestamp without time zone DEFAULT now()
);


ALTER TABLE public.keychip_product_info OWNER TO allnet;

--
-- Data for Name: keychip_product_info; Type: TABLE DATA; Schema: public; Owner: allnet
--

COPY public.keychip_product_info (keychip_id, key_server, key_keychip, create_date) FROM stdin;
TESTID00001	9e221f8acf24fb1ddfb56bb8247502fb	554772f9c825ee56474f4e07e3a20972	2020-11-18 17:29:38.22
TESTID00002	9e221f8acf24fb1ddfb56bb8247502fb	f6b2f9baa286ea449facf0b451110f7f	2020-11-18 17:29:46.737
\.


--
-- Name: keychip_product_info_pkey; Type: CONSTRAINT; Schema: public; Owner: allnet; Tablespace: 
--

ALTER TABLE ONLY public.keychip_product_info
    ADD CONSTRAINT keychip_product_info_pkey PRIMARY KEY (keychip_id);


--
-- PostgreSQL database dump complete
--

