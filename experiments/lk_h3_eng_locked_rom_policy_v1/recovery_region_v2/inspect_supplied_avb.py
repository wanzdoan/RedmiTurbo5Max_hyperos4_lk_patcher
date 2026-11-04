"""Verify supplied Global images and replay the existing LK trust gate.

This investigation emits evidence only. It does not create a new LK image.
"""
from pathlib import Path
import argparse
import hashlib
import json
import struct
import sys

HERE=Path(__file__).resolve().parent
sys.path.insert(0,str(HERE.parent))
from inspect_target_vbmeta import inspect

VBMETA_SHA='4d6ad1a94bcbf4edc3cfc3ffb94978b61c8441c94a72c6466e3d4f976e263837'
VENDOR_SHA='381a47c837e67fc46b9442f5a1c9ca0444c4b97f67c9f3b8fa82e7facf84a285'


def sha(d):return hashlib.sha256(d).hexdigest()


def descriptors(v):
    auth=struct.unpack_from('>Q',v,12)[0]
    off,length=struct.unpack_from('>QQ',v,96)
    pos=256+auth+off;end=pos+length;rows=[]
    while pos<end:
        tag,n=struct.unpack_from('>QQ',v,pos)
        if pos+16+n>end:raise ValueError('Descriptor outside signed auxiliary data')
        if tag==2:
            size=struct.unpack_from('>Q',v,pos+16)[0]
            algorithm=v[pos+24:pos+56].split(b'\0')[0].decode()
            nl,sl,dl=struct.unpack_from('>III',v,pos+56)
            start=pos+132
            name=v[start:start+nl].decode();start+=nl
            salt=v[start:start+sl];start+=sl;digest=v[start:start+dl]
            rows.append({'tag':tag,'partition':name,'image_bytes':size,'algorithm':algorithm,
                         'salt_hex':salt.hex(),'expected_digest_hex':digest.hex()})
        elif tag==4:
            location,nl,kl=struct.unpack_from('>III',v,pos+16)
            name=v[pos+92:pos+92+nl].decode();key=v[pos+92+nl:pos+92+nl+kl]
            bits=struct.unpack_from('>I',key)[0]
            rows.append({'tag':tag,'partition':name,'rollback_location':location,
                         'key_bits':bits,'key_modulus_sha256':sha(key[8:8+bits//8])})
        pos+=16+n
    return rows


def main():
    ap=argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--vbmeta',type=Path,default=Path(r'C:\Users\Administrator\Downloads\vbmeta.img'))
    ap.add_argument('--vendor-boot',type=Path,default=Path(r'C:\Users\Administrator\Downloads\vendor_boot.img'))
    args=ap.parse_args();v=args.vbmeta.read_bytes();data=args.vendor_boot.read_bytes()
    if sha(v)!=VBMETA_SHA or sha(data)!=VENDOR_SHA:
        raise ValueError('Expected the exact user-supplied Global input pair')
    report=inspect(args.vbmeta)
    report['limits']=['The real Global vbmeta signature and existing LK public-key callback were checked',
                      'This is not full AVB slot verification or a readback of flashed partitions',
                      'Android trigger for version_mismatch remains unidentified without normal ROM components']
    rows=descriptors(v)
    vendor=next(r for r in rows if r.get('partition')=='vendor_boot' and r['tag']==2)
    vendor['actual_supplied_image_matches']=hashlib.new(vendor['algorithm'],
        bytes.fromhex(vendor['salt_hex'])+data[:vendor['image_bytes']]).hexdigest()==vendor['expected_digest_hex']
    if not vendor['actual_supplied_image_matches']:raise ValueError('Supplied vendor_boot does not match vbmeta')
    country=next(r for r in rows if r.get('partition')=='countrycode' and r['tag']==2)
    signed_region=b'global'.ljust(32,b'\0')
    if country['image_bytes']==32 and hashlib.new(country['algorithm'],bytes.fromhex(country['salt_hex'])+signed_region).hexdigest()==country['expected_digest_hex']:
        country['signed_expected_content']='ASCII global followed by zero padding to32 bytes'
    local=HERE/'input/vbmeta.img'
    if local.exists() and local.read_bytes()!=v:raise ValueError('Different saved vbmeta input')
    local.write_bytes(v)
    (HERE/'global_vbmeta_validation.json').write_text(json.dumps(report,indent=2)+'\n')
    (HERE/'global_vbmeta_descriptors.json').write_text(json.dumps(rows,indent=2)+'\n')
    print(json.dumps({'Global_vbmeta_signature_valid':True,
                      'existing_v1_callback_trusts_actual_Global_key':bool(report['patched_callback']['trusted']),
                      'supplied_vendor_boot_matches_signed_descriptor':vendor['actual_supplied_image_matches'],
                      'new_LK_image_created':False},indent=2))


if __name__=='__main__':main()
