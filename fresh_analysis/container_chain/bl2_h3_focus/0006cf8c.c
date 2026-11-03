// File offset entry: 0x6cf8c

/* WARNING: Type propagation algorithm not settling */

ulong FUN_ffff0000b806cf8c(long param_1,uint param_2,uint param_3)

{
  byte bVar1;
  bool bVar2;
  int iVar3;
  ulong uVar4;
  byte *pbVar5;
  ulong uVar6;
  long lVar7;
  byte *pbVar8;
  uint uVar9;
  uint uVar10;
  byte *pbVar11;
  ulong uVar12;
  
  uVar10 = *(uint *)(param_1 + 0x34);
  DAT_ffff0000b81b7e60 = (byte *)(param_1 + (ulong)uVar10);
  DAT_ffff0000b81b7e70 = (ulong)(param_2 - uVar10);
  if ((param_3 >> 5 & 1) == 0) {
joined_r0xffff0000b806d374:
    if ((param_3 >> 6 & 1) == 0) {
joined_r0xffff0000b806d5e0:
      if (((param_3 >> 1 & 1) != 0) &&
         (uVar4 = FUN_ffff0000b806ca30(&DAT_ffff0000b81b7d50,&DAT_ffff0000b81b7d54), (int)uVar4 != 0
         )) {
        return uVar4;
      }
      if ((param_3 >> 7 & 1) == 0) {
joined_r0xffff0000b806cfec:
        if (((param_3 >> 10 & 1) != 0) &&
           (uVar4 = FUN_ffff0000b806cbe0(PTR_DAT_ffff0000b8107730,PTR_DAT_ffff0000b8107730 + 4,2),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 0xc & 1) != 0) {
          DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
          DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
          if (DAT_ffff0000b81b7e70 == 0) {
            uVar4 = 0;
          }
          else {
            while (uVar4 = FUN_ffff0000b806b888(0x11), (int)uVar4 == 0) {
              if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
              bVar1 = DAT_ffff0000b81b8108[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = DAT_ffff0000b81b8110 - uVar6;
              if (DAT_ffff0000b81b8110 < uVar6) {
                DAT_ffff0000b81b8110 = 0;
                return 0x101028;
              }
              pbVar5 = DAT_ffff0000b81b8108 + uVar6;
              if (*DAT_ffff0000b81b8108 != 3) {
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                return 0x700f;
              }
              bVar1 = DAT_ffff0000b81b8108[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                uVar10 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar9;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                uVar10 = (uint)bVar1;
                lVar7 = 1;
              }
              DAT_ffff0000b81b7dcc = uVar10 - 1;
              if (DAT_ffff0000b81b7dcc < 0x21) {
                pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                FUN_ffff0000b80695a0(&DAT_ffff0000b81b7dac,pbVar11);
                DAT_ffff0000b81b8108 = (byte *)0x0;
                DAT_ffff0000b81b8110 = 0;
                goto LAB_ffff0000b806d00c;
              }
              bVar2 = DAT_ffff0000b81b8110 == uVar6;
              DAT_ffff0000b81b8108 = pbVar5;
              DAT_ffff0000b81b8110 = uVar4;
              if (bVar2) {
                uVar10 = 0x101028;
                if (*(int *)PTR_DAT_ffff0000b8107748 != 0) {
                  uVar10 = 0x7014;
                }
                DAT_ffff0000b81b8108 = (byte *)0x0;
                DAT_ffff0000b81b8110 = 0;
                return (ulong)uVar10;
              }
            }
          }
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          if (DAT_ffff0000b81b77d0 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          if ((int)uVar4 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return uVar4;
          }
        }
LAB_ffff0000b806d00c:
        if ((param_3 >> 0xd & 1) != 0) {
          DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
          DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
          pbVar5 = DAT_ffff0000b81b8108;
          uVar4 = DAT_ffff0000b81b8110;
          if (DAT_ffff0000b81b7e70 != 0) {
            do {
              iVar3 = FUN_ffff0000b806b888(0x12);
              pbVar5 = DAT_ffff0000b81b8108;
              uVar4 = DAT_ffff0000b81b8110;
              if (iVar3 != 0) break;
              if (DAT_ffff0000b81b8110 == 0) {
                FUN_ffff0000b804beb0
                          (s___s__end_of_cert__not_found__ffff0000b809c0a2,&DAT_ffff0000b809770d);
                pbVar5 = DAT_ffff0000b81b8108;
                uVar4 = DAT_ffff0000b81b8110;
                break;
              }
              bVar1 = DAT_ffff0000b81b8108[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar11 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = DAT_ffff0000b81b8110 - uVar6;
              if (DAT_ffff0000b81b8110 < uVar6) {
                DAT_ffff0000b81b8110 = 0;
                uVar4 = DAT_ffff0000b81b8110;
                break;
              }
              pbVar5 = DAT_ffff0000b81b8108 + uVar6;
              if (*DAT_ffff0000b81b8108 != 3) break;
              bVar1 = DAT_ffff0000b81b8108[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                uVar10 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar9;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                uVar10 = (uint)bVar1;
                lVar7 = 1;
              }
              if (uVar10 - 1 < 0x41) {
                pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                FUN_ffff0000b80695a0(&DAT_ffff0000b81b7dd0,pbVar11);
                goto LAB_ffff0000b806d688;
              }
              bVar2 = DAT_ffff0000b81b8110 != uVar6;
              DAT_ffff0000b81b8108 = pbVar5;
              DAT_ffff0000b81b8110 = uVar4;
            } while (bVar2);
          }
          DAT_ffff0000b81b8110 = uVar4;
          DAT_ffff0000b81b8108 = pbVar5;
          if (DAT_ffff0000b81b77d0 != 0) {
            FUN_ffff0000b804cbe4(PTR_DAT_ffff0000b8107730 + 0xf0,0,0x40);
            FUN_ffff0000b804beb0
                      (s___s__Decode_checksum_lost__ffff0000b808d700,&DAT_ffff0000b809770d);
          }
LAB_ffff0000b806d688:
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
        }
        if ((param_3 >> 0xe & 1) != 0) {
          DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
          DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
          if (DAT_ffff0000b81b7e70 != 0) {
            do {
              iVar3 = FUN_ffff0000b806b888(0x13);
              if (iVar3 != 0) break;
              if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
              bVar1 = DAT_ffff0000b81b8108[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = DAT_ffff0000b81b8110 - uVar6;
              if (DAT_ffff0000b81b8110 < uVar6) {
                DAT_ffff0000b81b8110 = 0;
                return 0x101028;
              }
              pbVar5 = DAT_ffff0000b81b8108 + uVar6;
              if (*DAT_ffff0000b81b8108 != 3) {
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                return 0x700f;
              }
              bVar1 = DAT_ffff0000b81b8108[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                uVar10 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar9;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                uVar10 = (uint)bVar1;
                lVar7 = 1;
              }
              DAT_ffff0000b81b7e30 = uVar10 - 1;
              if (DAT_ffff0000b81b7e30 < 0x21) {
                pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                FUN_ffff0000b80695a0(&DAT_ffff0000b81b7e10,pbVar11);
                goto LAB_ffff0000b806d804;
              }
              bVar2 = DAT_ffff0000b81b8110 != uVar6;
              DAT_ffff0000b81b8108 = pbVar5;
              DAT_ffff0000b81b8110 = uVar4;
            } while (bVar2);
          }
          if (DAT_ffff0000b81b77d0 != 0) {
            FUN_ffff0000b804cbe4(PTR_DAT_ffff0000b8107730 + 0x130,0,0x20);
            FUN_ffff0000b804beb0
                      (s___s__FE_without_cust_wrapped__ffff0000b808f533,&DAT_ffff0000b809770d);
          }
LAB_ffff0000b806d804:
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
        }
        if ((param_3 >> 0xf & 1) != 0) {
          DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
          DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
          if (DAT_ffff0000b81b7e70 != 0) {
            do {
              iVar3 = FUN_ffff0000b806b888(0x14);
              if (iVar3 != 0) break;
              if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
              bVar1 = DAT_ffff0000b81b8108[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = DAT_ffff0000b81b8110 - uVar6;
              if (DAT_ffff0000b81b8110 < uVar6) {
                DAT_ffff0000b81b8110 = 0;
                return 0x101028;
              }
              pbVar5 = DAT_ffff0000b81b8108 + uVar6;
              if (*DAT_ffff0000b81b8108 != 3) {
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                return 0x700f;
              }
              bVar1 = DAT_ffff0000b81b8108[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                uVar10 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = DAT_ffff0000b81b8108 + 2;
                do {
                  uVar9 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar9;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                uVar10 = (uint)bVar1;
                lVar7 = 1;
              }
              DAT_ffff0000b81b7e44 = uVar10 - 1;
              if (DAT_ffff0000b81b7e44 < 0x11) {
                pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
                DAT_ffff0000b81b8108 = pbVar5;
                DAT_ffff0000b81b8110 = uVar4;
                FUN_ffff0000b80695a0(&DAT_ffff0000b81b7e34,pbVar11);
                break;
              }
              bVar2 = DAT_ffff0000b81b8110 != uVar6;
              DAT_ffff0000b81b8108 = pbVar5;
              DAT_ffff0000b81b8110 = uVar4;
            } while (bVar2);
          }
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
        }
        if ((param_3 >> 0x10 & 1) == 0) {
          return 0;
        }
        DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
        DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
        if (DAT_ffff0000b81b7e70 != 0) {
          do {
            iVar3 = FUN_ffff0000b806b888(0x15);
            if (iVar3 != 0) {
              DAT_ffff0000b81b8108 = (byte *)0x0;
              DAT_ffff0000b81b8110 = 0;
              return 0;
            }
            if (DAT_ffff0000b81b8110 == 0) {
LAB_ffff0000b806da54:
              FUN_ffff0000b804beb0
                        (s___s__end_of_cert__not_found__ffff0000b809c0a2,&DAT_ffff0000b809770d);
              return 0x700e;
            }
            bVar1 = DAT_ffff0000b81b8108[1];
            uVar10 = (uint)bVar1;
            if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
              iVar3 = (uVar10 & 0x7f) + 1;
              uVar9 = uVar10 & 0x7f;
              uVar10 = 0;
              pbVar5 = DAT_ffff0000b81b8108 + 2;
              do {
                uVar9 = uVar9 - 1;
                uVar10 = (uint)*pbVar5 | uVar10 << 8;
                pbVar5 = pbVar5 + 1;
              } while (uVar9 != 0);
            }
            else {
              iVar3 = 1;
            }
            if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
              uVar10 = 0;
            }
            uVar6 = (ulong)(iVar3 + uVar10 + 1);
            uVar4 = DAT_ffff0000b81b8110 - uVar6;
            if (DAT_ffff0000b81b8110 < uVar6) {
              DAT_ffff0000b81b8110 = 0;
              return 0x101028;
            }
            pbVar5 = DAT_ffff0000b81b8108 + uVar6;
            if (*DAT_ffff0000b81b8108 != 3) {
              DAT_ffff0000b81b8110 = uVar4;
              DAT_ffff0000b81b8108 = pbVar5;
              return 0x700f;
            }
            bVar1 = DAT_ffff0000b81b8108[1];
            if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
              uVar10 = 0;
              lVar7 = uVar12 + 1;
              pbVar11 = DAT_ffff0000b81b8108 + 2;
              do {
                uVar9 = (int)uVar12 - 1;
                uVar12 = (ulong)uVar9;
                uVar10 = (uint)*pbVar11 | uVar10 << 8;
                pbVar11 = pbVar11 + 1;
              } while (uVar9 != 0);
            }
            else {
              uVar10 = (uint)bVar1;
              lVar7 = 1;
            }
            DAT_ffff0000b81b7e58 = uVar10 - 1;
            if (DAT_ffff0000b81b7e58 < 0x11) {
              pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
              DAT_ffff0000b81b8108 = pbVar5;
              DAT_ffff0000b81b8110 = uVar4;
              FUN_ffff0000b80695a0(&DAT_ffff0000b81b7e48,pbVar11);
              DAT_ffff0000b81b8108 = (byte *)0x0;
              DAT_ffff0000b81b8110 = 0;
              return 0;
            }
            bVar2 = DAT_ffff0000b81b8110 != uVar6;
            DAT_ffff0000b81b8108 = pbVar5;
            DAT_ffff0000b81b8110 = uVar4;
          } while (bVar2);
        }
        DAT_ffff0000b81b8108 = (byte *)0x0;
        DAT_ffff0000b81b8110 = 0;
        return 0;
      }
      DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
      DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
      if (DAT_ffff0000b81b7e70 != 0) {
        do {
          iVar3 = FUN_ffff0000b806b888(9);
          if (iVar3 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
          pbVar5 = DAT_ffff0000b81b8108 + 1;
          bVar1 = *pbVar5;
          uVar10 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar10 & 0x7f) + 1;
            uVar9 = uVar10 & 0x7f;
            uVar10 = 0;
            pbVar11 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar10 = (uint)*pbVar11 | uVar10 << 8;
              pbVar11 = pbVar11 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
            uVar10 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar10 + 1);
          uVar4 = DAT_ffff0000b81b8110 - uVar6;
          if (DAT_ffff0000b81b8110 < uVar6) {
            DAT_ffff0000b81b8110 = 0;
            return 0x101028;
          }
          pbVar11 = DAT_ffff0000b81b8108 + uVar6;
          if (*DAT_ffff0000b81b8108 != 2) {
            DAT_ffff0000b81b8108 = pbVar11;
            DAT_ffff0000b81b8110 = uVar4;
            return 0x700f;
          }
          bVar1 = *pbVar5;
          if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
            DAT_ffff0000b81b7d5c = 0;
            lVar7 = uVar12 + 1;
            pbVar8 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar10 = (int)uVar12 - 1;
              uVar12 = (ulong)uVar10;
              DAT_ffff0000b81b7d5c = (uint)*pbVar8 | DAT_ffff0000b81b7d5c << 8;
              pbVar8 = pbVar8 + 1;
            } while (uVar10 != 0);
          }
          else {
            DAT_ffff0000b81b7d5c = (uint)bVar1;
            lVar7 = 1;
          }
          pbVar5 = pbVar5 + lVar7;
          DAT_ffff0000b81b8108 = pbVar11;
          if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
            DAT_ffff0000b81b7d5c = DAT_ffff0000b81b7d5c - 1;
            if (DAT_ffff0000b81b7d5c < 5) {
              pbVar5 = pbVar5 + 1;
LAB_ffff0000b806d340:
              DAT_ffff0000b81b8110 = uVar4;
              FUN_ffff0000b80695a0(&DAT_ffff0000b81b7d58,pbVar5,DAT_ffff0000b81b7d5c);
              DAT_ffff0000b81b8108 = (byte *)0x0;
              DAT_ffff0000b81b8110 = 0;
              goto joined_r0xffff0000b806cfec;
            }
          }
          else if (DAT_ffff0000b81b7d5c < 5) goto LAB_ffff0000b806d340;
          bVar2 = DAT_ffff0000b81b8110 != uVar6;
          DAT_ffff0000b81b8110 = uVar4;
        } while (bVar2);
      }
    }
    else {
      DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
      DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
      if (DAT_ffff0000b81b7e70 != 0) {
        do {
          iVar3 = FUN_ffff0000b806b888(5);
          if (iVar3 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
          bVar1 = DAT_ffff0000b81b8108[1];
          uVar10 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar10 & 0x7f) + 1;
            uVar9 = uVar10 & 0x7f;
            uVar10 = 0;
            pbVar5 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar10 = (uint)*pbVar5 | uVar10 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
            uVar10 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar10 + 1);
          uVar4 = DAT_ffff0000b81b8110 - uVar6;
          if (DAT_ffff0000b81b8110 < uVar6) {
            DAT_ffff0000b81b8110 = 0;
            return 0x101028;
          }
          pbVar5 = DAT_ffff0000b81b8108 + uVar6;
          if (*DAT_ffff0000b81b8108 != 3) {
            DAT_ffff0000b81b8108 = pbVar5;
            DAT_ffff0000b81b8110 = uVar4;
            return 0x700f;
          }
          bVar1 = DAT_ffff0000b81b8108[1];
          if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
            uVar10 = 0;
            lVar7 = uVar12 + 1;
            pbVar11 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar9 = (int)uVar12 - 1;
              uVar12 = (ulong)uVar9;
              uVar10 = (uint)*pbVar11 | uVar10 << 8;
              pbVar11 = pbVar11 + 1;
            } while (uVar9 != 0);
          }
          else {
            uVar10 = (uint)bVar1;
            lVar7 = 1;
          }
          DAT_ffff0000b81b7d18 = uVar10 - 1;
          if (DAT_ffff0000b81b7d18 < 0x31) {
            pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
            DAT_ffff0000b81b8108 = pbVar5;
            DAT_ffff0000b81b8110 = uVar4;
            FUN_ffff0000b80695a0(&DAT_ffff0000b81b7ce8,pbVar11);
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            goto joined_r0xffff0000b806d5e0;
          }
          bVar2 = DAT_ffff0000b81b8110 != uVar6;
          DAT_ffff0000b81b8108 = pbVar5;
          DAT_ffff0000b81b8110 = uVar4;
        } while (bVar2);
      }
    }
  }
  else {
    DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
    DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
    if (uVar10 != param_2) {
      do {
        iVar3 = FUN_ffff0000b806b888(10);
        if (iVar3 != 0) {
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806da54;
        bVar1 = DAT_ffff0000b81b8108[1];
        uVar10 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar3 = (uVar10 & 0x7f) + 1;
          uVar9 = uVar10 & 0x7f;
          uVar10 = 0;
          pbVar5 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar9 = uVar9 - 1;
            uVar10 = (uint)*pbVar5 | uVar10 << 8;
            pbVar5 = pbVar5 + 1;
          } while (uVar9 != 0);
        }
        else {
          iVar3 = 1;
        }
        if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
          uVar10 = 0;
        }
        uVar6 = (ulong)(iVar3 + uVar10 + 1);
        uVar4 = DAT_ffff0000b81b8110 - uVar6;
        if (DAT_ffff0000b81b8110 < uVar6) {
          DAT_ffff0000b81b8110 = 0;
          return 0x101028;
        }
        pbVar5 = DAT_ffff0000b81b8108 + uVar6;
        if (*DAT_ffff0000b81b8108 != 3) {
          DAT_ffff0000b81b8108 = pbVar5;
          DAT_ffff0000b81b8110 = uVar4;
          return 0x700f;
        }
        bVar1 = DAT_ffff0000b81b8108[1];
        if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
          uVar10 = 0;
          lVar7 = uVar12 + 1;
          pbVar11 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar9 = (int)uVar12 - 1;
            uVar12 = (ulong)uVar9;
            uVar10 = (uint)*pbVar11 | uVar10 << 8;
            pbVar11 = pbVar11 + 1;
          } while (uVar9 != 0);
        }
        else {
          uVar10 = (uint)bVar1;
          lVar7 = 1;
        }
        DAT_ffff0000b81b7d4c = uVar10 - 1;
        if (DAT_ffff0000b81b7d4c < 0x31) {
          pbVar11 = DAT_ffff0000b81b8108 + lVar7 + 2;
          DAT_ffff0000b81b8108 = pbVar5;
          DAT_ffff0000b81b8110 = uVar4;
          FUN_ffff0000b80695a0(&DAT_ffff0000b81b7d1c,pbVar11);
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          goto joined_r0xffff0000b806d374;
        }
        bVar2 = DAT_ffff0000b81b8110 != uVar6;
        DAT_ffff0000b81b8108 = pbVar5;
        DAT_ffff0000b81b8110 = uVar4;
      } while (bVar2);
    }
  }
  DAT_ffff0000b81b8108 = (byte *)0x0;
  DAT_ffff0000b81b8110 = 0;
  return 0x7014;
}


