SELECT setval('feirinhas_id_feirinha_seq', (SELECT MAX(id_feirinha) FROM feirinhas));
