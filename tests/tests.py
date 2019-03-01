import os
import json
import time

with open('query.json') as f:
	datas = json.load(f)

err = lambda x : "[ERR] {} not found !!".format(x)
for i, q in enumerate(datas['query']):
	for j, v in enumerate(datas['query'][q]):
		result = os.popen('cd ..; echo "{}" | java Main'.format(v)).read()

		if ":" in datas['result'][q][j]:
			for r in datas['result'][q][j].split(':'):
				for c in r.split(','):
					if c not in result:
						exit(err(c))
		elif datas['result'][q][j] not in result:
			exit(err(c))
		
		print '>> {} [PASSED]'.format(v)