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

--
-- Name: auth_sequence; Type: SEQUENCE; Schema: public; Owner: allnet
--

CREATE SEQUENCE public.auth_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.auth_sequence OWNER TO allnet;

--
-- Name: auth_sequence; Type: SEQUENCE SET; Schema: public; Owner: allnet
--

SELECT pg_catalog.setval('public.auth_sequence', 3, true);


--
-- PostgreSQL database dump complete
--

