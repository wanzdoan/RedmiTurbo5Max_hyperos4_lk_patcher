"""Read a target vbmeta, verify its own signature and replay original/new trust.

Does not verify all partition descriptors, rollback or a whole boot chain.
"""
import argparse
import json
import struct
from pathlib import Path
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.primitives import hashes
from build_stock_flash_region import HERE, INPUT, NAME, CN_HASH, GL_HASH, sha
from validate_stock_flash_region import key_case


def inspect(path):
    data=path.read_bytes()
    if data[:4]!=b'AVB0':raise ValueError('Expected top-level AVB vbmeta header')
    auth,aux=struct.unpack_from('>QQ',data,12)
    algorithm=struct.unpack_from('>I',data,28)[0]
    hash_off,hash_len,sig_off,sig_len,key_off,key_len=struct.unpack_from('>QQQQQQ',data,32)
    if 256+auth+aux>len(data):raise ValueError('Truncated vbmeta')
    key=data[256+auth+key_off:256+auth+key_off+key_len]
    bits=struct.unpack_from('>I',key)[0]
    modulus=key[8:8+bits//8]
    if len(key)!=8+2*(bits//8):raise ValueError('Unexpected AVB RSA key size')
    digest_algorithm={1:hashes.SHA256,2:hashes.SHA256,3:hashes.SHA256,
                      4:hashes.SHA512,5:hashes.SHA512,6:hashes.SHA512}[algorithm]()
    signed=data[:256]+data[256+auth:256+auth+aux]
    h=hashes.Hash(digest_algorithm);h.update(signed)
    if h.finalize()!=data[256+hash_off:256+hash_off+hash_len]:raise ValueError('AVB signed hash mismatch')
    rsa.RSAPublicNumbers(65537,int.from_bytes(modulus,'big')).public_key().verify(
        data[256+sig_off:256+sig_off+sig_len],signed,padding.PKCS1v15(),digest_algorithm)
    identity={CN_HASH:'known H3 CN',GL_HASH:'known H3 Global'}.get(sha(modulus),'unknown to new allowlist')
    source=INPUT.read_bytes();output=(HERE/NAME).read_bytes()
    original_trusted=bits==2048 and modulus==source[512+0x130728:512+0x130828]
    new_trusted=original_trusted or sha(modulus) in (CN_HASH,GL_HASH)
    return {'path':str(path.resolve()),'vbmeta_sha256':sha(data),'algorithm_id':algorithm,
            'public_key_bits':bits,'public_key_blob_bytes':len(key),'modulus_sha256':sha(modulus),
            'key_identity':identity,'signed_header_auxiliary_hash_valid':True,'rsa_pkcs1_signature_valid':True,
            'original_eng_callback':key_case(source,bits,modulus,(0,int(original_trusted))),
            'patched_callback':key_case(output,bits,modulus,(0,int(new_trusted))),
            'limits':['Own embedded key verifies signature, not the entire partition chain',
                      'No payload descriptor hashes, rollback or hardware compatibility checked',
                      'Accepting an H4 vbmeta key does not prove H3 ENG LK can boot H4 firmware']}


if __name__=='__main__':
    ap=argparse.ArgumentParser(description=__doc__)
    ap.add_argument('vbmeta',type=Path)
    ap.add_argument('--report',type=Path)
    args=ap.parse_args();report=inspect(args.vbmeta)
    if args.report:args.report.write_text(json.dumps(report,indent=2)+'\n')
    print(json.dumps(report,indent=2))
