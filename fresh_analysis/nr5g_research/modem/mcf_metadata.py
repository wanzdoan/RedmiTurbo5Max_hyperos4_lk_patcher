from ext4_readonly import *
fs=Ext4((BASE/'mcf_ota.img').read_bytes());rows=list(fs.walk());metadata=[]
for r in rows:
 if r['mode'].startswith('0x4'):continue
 d=fs.read(r['inode']);item=dict(r,sha256=hashlib.sha256(d).hexdigest(),md5=hashlib.md5(d).hexdigest(),head64_hex=d[:64].hex())
 metadata.append(item)
(OUT/'mcf_file_metadata.json').write_text(json.dumps(metadata,indent=2))
print('groupcounts',collections.Counter(r['path'].split('/')[1] for r in rows)); print('miscfiles',[(r['path'],r['size']) for r in rows if '/misc_data/' in r['path']][:60])
print('sampleheads')
for r in metadata:
 if r['path'] in ['/custom/MTK_OPOTA_GENERAL.mcfopota','/custom/MTK_NWOTA_GENERAL.mcfnwota','/mtk_default/MTK_OPOTA_GENERAL.mcfopota']:
  print(r['path'],r['size'],r['head64_hex']);print(fs.read(r['inode'])[:520].hex())
