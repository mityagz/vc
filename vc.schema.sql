--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: adjs; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE adjs (
    id integer NOT NULL,
    node_id integer NOT NULL,
    adj_node_id integer NOT NULL,
    interface_id_oif integer NOT NULL,
    interface_id_iif integer NOT NULL,
    name character varying(255)
);


ALTER TABLE adjs OWNER TO vc;

--
-- Name: adjs_id_seq; Type: SEQUENCE; Schema: public; Owner: vc
--

CREATE SEQUENCE adjs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE adjs_id_seq OWNER TO vc;

--
-- Name: adjs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vc
--

ALTER SEQUENCE adjs_id_seq OWNED BY adjs.id;


--
-- Name: interface; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE interface (
    id integer NOT NULL,
    node_id integer NOT NULL,
    type character varying(255),
    name character varying(255),
    description character varying(255)
);


ALTER TABLE interface OWNER TO vc;

--
-- Name: interface_id_seq; Type: SEQUENCE; Schema: public; Owner: vc
--

CREATE SEQUENCE interface_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE interface_id_seq OWNER TO vc;

--
-- Name: interface_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vc
--

ALTER SEQUENCE interface_id_seq OWNED BY interface.id;


--
-- Name: node; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE node (
    id integer NOT NULL,
    type_id integer,
    hostname character varying(255),
    ip character varying(15),
    id_sg_node integer
);


ALTER TABLE node OWNER TO vc;

--
-- Name: node_id_seq; Type: SEQUENCE; Schema: public; Owner: vc
--

CREATE SEQUENCE node_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE node_id_seq OWNER TO vc;

--
-- Name: node_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vc
--

ALTER SEQUENCE node_id_seq OWNED BY node.id;


--
-- Name: type; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE type (
    id integer NOT NULL,
    type character varying(255),
    role character varying(255),
    name character varying(255)
);


ALTER TABLE type OWNER TO vc;

--
-- Name: type_id_seq; Type: SEQUENCE; Schema: public; Owner: vc
--

CREATE SEQUENCE type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE type_id_seq OWNER TO vc;

--
-- Name: type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: vc
--

ALTER SEQUENCE type_id_seq OWNED BY type.id;


--
-- Name: vc; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE vc (
    id integer NOT NULL,
    circuit_id integer NOT NULL,
    vlan_id_outer integer NOT NULL,
    vlan_id_inner integer NOT NULL,
    description character varying(255),
    vc_type_id integer NOT NULL
);


ALTER TABLE vc OWNER TO vc;

--
-- Name: vc_type; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE vc_type (
    id integer NOT NULL,
    type character varying(255)
);


ALTER TABLE vc_type OWNER TO vc;

--
-- Name: vlans; Type: TABLE; Schema: public; Owner: vc; Tablespace: 
--

CREATE TABLE vlans (
    id integer NOT NULL,
    interface_id integer NOT NULL,
    node_id integer NOT NULL,
    unit integer NOT NULL,
    vid integer NOT NULL,
    vid_range0 integer NOT NULL,
    vid_range1 integer NOT NULL,
    name character varying(255),
    sub_interface_id integer NOT NULL
);


ALTER TABLE vlans OWNER TO vc;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vc
--

ALTER TABLE ONLY adjs ALTER COLUMN id SET DEFAULT nextval('adjs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vc
--

ALTER TABLE ONLY interface ALTER COLUMN id SET DEFAULT nextval('interface_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vc
--

ALTER TABLE ONLY node ALTER COLUMN id SET DEFAULT nextval('node_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: vc
--

ALTER TABLE ONLY type ALTER COLUMN id SET DEFAULT nextval('type_id_seq'::regclass);


--
-- Name: adjs_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY adjs
    ADD CONSTRAINT adjs_pkey PRIMARY KEY (id, node_id, adj_node_id, interface_id_oif, interface_id_iif);


--
-- Name: interface_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY interface
    ADD CONSTRAINT interface_pkey PRIMARY KEY (id);


--
-- Name: node_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY node
    ADD CONSTRAINT node_pkey PRIMARY KEY (id);


--
-- Name: type_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY type
    ADD CONSTRAINT type_pkey PRIMARY KEY (id);


--
-- Name: vc_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY vc
    ADD CONSTRAINT vc_pkey PRIMARY KEY (id);


--
-- Name: vc_type_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY vc_type
    ADD CONSTRAINT vc_type_pkey PRIMARY KEY (id);


--
-- Name: vlans_pkey; Type: CONSTRAINT; Schema: public; Owner: vc; Tablespace: 
--

ALTER TABLE ONLY vlans
    ADD CONSTRAINT vlans_pkey PRIMARY KEY (id);


--
-- Name: adjs_adj_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY adjs
    ADD CONSTRAINT adjs_adj_node_id_fkey FOREIGN KEY (adj_node_id) REFERENCES node(id);


--
-- Name: adjs_interface_id_iif_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY adjs
    ADD CONSTRAINT adjs_interface_id_iif_fkey FOREIGN KEY (interface_id_iif) REFERENCES interface(id);


--
-- Name: adjs_interface_id_oif_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY adjs
    ADD CONSTRAINT adjs_interface_id_oif_fkey FOREIGN KEY (interface_id_oif) REFERENCES interface(id);


--
-- Name: adjs_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY adjs
    ADD CONSTRAINT adjs_node_id_fkey FOREIGN KEY (node_id) REFERENCES node(id);


--
-- Name: interface_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY interface
    ADD CONSTRAINT interface_node_id_fkey FOREIGN KEY (node_id) REFERENCES node(id);


--
-- Name: node_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY node
    ADD CONSTRAINT node_type_id_fkey FOREIGN KEY (type_id) REFERENCES type(id);


--
-- Name: vc_vc_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vc
    ADD CONSTRAINT vc_vc_type_id_fkey FOREIGN KEY (vc_type_id) REFERENCES vc_type(id);


--
-- Name: vc_vlan_id_inner_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vc
    ADD CONSTRAINT vc_vlan_id_inner_fkey FOREIGN KEY (vlan_id_inner) REFERENCES vlans(id);


--
-- Name: vc_vlan_id_outer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vc
    ADD CONSTRAINT vc_vlan_id_outer_fkey FOREIGN KEY (vlan_id_outer) REFERENCES vlans(id);


--
-- Name: vlans_interface_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vlans
    ADD CONSTRAINT vlans_interface_id_fkey FOREIGN KEY (interface_id) REFERENCES interface(id);


--
-- Name: vlans_node_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vlans
    ADD CONSTRAINT vlans_node_id_fkey FOREIGN KEY (node_id) REFERENCES node(id);


--
-- Name: vlans_sub_interface_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: vc
--

ALTER TABLE ONLY vlans
    ADD CONSTRAINT vlans_sub_interface_id_fkey FOREIGN KEY (sub_interface_id) REFERENCES interface(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

