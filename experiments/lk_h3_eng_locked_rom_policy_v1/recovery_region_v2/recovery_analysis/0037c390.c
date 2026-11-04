
/* decrypt_identity(char const*, unsigned char**) */

int decrypt_identity(char *param_1,uchar **param_2)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  char cVar5;
  undefined *puVar6;
  int iVar7;
  int iVar8;
  BIO *bp;
  RSA *rsa;
  size_t sVar9;
  uchar *from;
  uchar *to;
  char *__s;
  ulong uVar10;
  long lVar11;
  char cVar12;
  uint uVar13;
  long lVar14;
  undefined4 uVar15;

  printf("Decrypt identity start %s\n",param_1);
  bp = BIO_new_mem_buf(PTR_s______BEGIN_PUBLIC_KEY______MIIBI_0042cbc0,-1);
  if (bp == (BIO *)0x0) {
    __s = "Decrypt identity bio is NULL. ";
  }
  else {
    rsa = PEM_read_bio_RSA_PUBKEY(bp,(RSA **)0x0,(undefined1 *)0x0,(void *)0x0);
    BIO_set_close(bp,1);
    BIO_free(bp);
    if (rsa == (RSA *)0x0) {
      __s = "Decrypt identity rsa_public is NULL.";
    }
    else {
      iVar7 = RSA_size(rsa);
      sVar9 = strlen(param_1);
      from = malloc((sVar9 * 3 >> 2) + 4);
      if (from == (uchar *)0x0) {
        RSA_free(rsa);
        __s = "crypt_str malloc blk fail";
      }
      else {
        sVar9 = strlen(param_1);
        memset(from,0,(sVar9 * 3 >> 2) + 4);
        puVar6 = PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_0042cbb8;
        cVar12 = *param_1;
        if (cVar12 != '\0') {
          uVar10 = 0;
          lVar11 = 0;
          do {
            lVar14 = 0;
            iVar8 = 0xff;
            do {
              iVar1 = (int)lVar14;
              if (puVar6[lVar14] != cVar12) {
                iVar1 = iVar8;
              }
              lVar14 = lVar14 + 1;
              iVar8 = iVar1;
            } while (lVar14 != 0x40);
            lVar14 = 0;
            uVar13 = 0xff;
            do {
              uVar2 = (uint)lVar14;
              if (puVar6[lVar14] != param_1[uVar10 | 1]) {
                uVar2 = uVar13;
              }
              lVar14 = lVar14 + 1;
              uVar13 = uVar2;
            } while (lVar14 != 0x40);
            lVar14 = 0;
            cVar12 = param_1[uVar10 | 2];
            uVar13 = 0xff;
            do {
              uVar3 = (uint)lVar14;
              if (puVar6[lVar14] != cVar12) {
                uVar3 = uVar13;
              }
              lVar14 = lVar14 + 1;
              uVar13 = uVar3;
            } while (lVar14 != 0x40);
            lVar14 = 0;
            cVar5 = param_1[uVar10 | 3];
            uVar15 = 0xff;
            do {
              uVar4 = (int)lVar14;
              if (puVar6[lVar14] != cVar5) {
                uVar4 = uVar15;
              }
              lVar14 = lVar14 + 1;
              uVar15 = uVar4;
            } while (lVar14 != 0x40);
            from[lVar11] = (byte)(iVar1 << 2) | (byte)(uVar2 >> 4) & 3;
            if ((cVar12 == '=') ||
               (from[lVar11 + 1] = (byte)(uVar2 << 4) | (byte)(uVar3 >> 2) & 0xf, cVar5 == '='))
            break;
            uVar10 = uVar10 + 4;
            from[lVar11 + 2] = (byte)uVar4 & 0x3f | (byte)(uVar3 << 6);
            cVar12 = param_1[uVar10];
            lVar11 = lVar11 + 3;
          } while (cVar12 != '\0');
        }
        to = malloc((long)iVar7);
        *param_2 = to;
        if (to == (uchar *)0x0) {
          free(from);
          RSA_free(rsa);
          __s = "plain_str malloc blk fail";
        }
        else {
          memset(to,0,(long)iVar7);
          iVar8 = RSA_public_decrypt(0x100,from,to,rsa,1);
          if (-1 < iVar8) {
            puts("Decrypt identity successed. ");
            free(from);
            RSA_free(rsa);
            return iVar7;
          }
          free(from);
          free(*param_2);
          RSA_free(rsa);
          __s = "Decrypt identity RSA_public_decrypt failed. ";
        }
      }
    }
  }
  puts(__s);
  return -1;
}
