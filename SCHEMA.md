-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.usuarios (
id integer GENERATED ALWAYS AS IDENTITY NOT NULL,
nombre character varying NOT NULL,
email character varying NOT NULL UNIQUE,
activo boolean NOT NULL DEFAULT true,
CONSTRAINT usuarios_pkey PRIMARY KEY (id)
);
CREATE TABLE public.eventos (
id integer GENERATED ALWAYS AS IDENTITY NOT NULL,
nombre character varying NOT NULL,
importe_total numeric NOT NULL CHECK (importe_total > 0::numeric),
creador_id integer NOT NULL,
fecha_creacion date NOT NULL,
fecha_pago_limite date NOT NULL,
descripcion text,
CONSTRAINT eventos_pkey PRIMARY KEY (id),
CONSTRAINT fk_eventos_creador FOREIGN KEY (creador_id) REFERENCES public.usuarios(id)
);
CREATE TABLE public.participantes_evento (
id integer GENERATED ALWAYS AS IDENTITY NOT NULL,
usuario_id integer NOT NULL,
evento_id integer NOT NULL,
importe_base numeric NOT NULL CHECK (importe_base >= 0::numeric),
penalizacion_aplicada numeric NOT NULL DEFAULT 0 CHECK (penalizacion_aplicada >= 0::numeric),
importe_final numeric NOT NULL CHECK (importe_final >= 0::numeric),
fecha_pago date,
estado_pago character varying NOT NULL DEFAULT 'PENDIENTE'::character varying CHECK (estado_pago::text = ANY (ARRAY['PENDIENTE'::character varying, 'PENDIENTE_CONFIRMAR'::character varying, 'PAGADO'::character varying, 'RECHAZADO'::character varying]::text[])),
CONSTRAINT participantes_evento_pkey PRIMARY KEY (id),
CONSTRAINT fk_participante_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id),
CONSTRAINT fk_participante_evento FOREIGN KEY (evento_id) REFERENCES public.eventos(id)
);