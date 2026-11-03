
/* WARNING: Type propagation algorithm not settling */

ulong FUN_ffff0000907b0ec4(long param_1,uint param_2,uint param_3)

{
  byte bVar1;
  bool bVar2;
  int iVar3;
  ulong uVar4;
  byte *pbVar5;
  ulong uVar6;
  byte *pbVar7;
  uint uVar8;
  uint uVar9;
  long lVar10;
  ulong uVar11;
  byte *pbVar12;
  uint local_4c;
  byte *local_48;
  
  uVar9 = *(uint *)(param_1 + 0x34);
  local_48 = (byte *)0x0;
  local_4c = 0;
  DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar9);
  DAT_ffff00009090cc78 = (ulong)(param_2 - uVar9);
  if (uVar9 <= param_2 && param_2 - uVar9 != 0) {
    uVar4 = FUN_ffff0000907b05cc(&local_48,&local_4c);
    if ((int)uVar4 != 0) {
      return uVar4;
    }
    DAT_ffff00009090cc78 = (ulong)local_4c;
    DAT_ffff00009090cc68 = local_48;
    if ((param_3 >> 5 & 1) == 0) {
joined_r0xffff0000907b1248:
      if ((param_3 >> 6 & 1) == 0) {
joined_r0xffff0000907b0f34:
        if (((param_3 >> 1 & 1) != 0) &&
           (uVar4 = FUN_ffff0000907b07b8(&DAT_ffff00009090cb58,&DAT_ffff00009090cb5c),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 7 & 1) != 0) {
          DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
          DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
          if (DAT_ffff00009090cc78 != 0) {
            do {
              iVar3 = FUN_ffff0000907af524(9);
              if (iVar3 != 0) {
                DAT_ffff00009090cf48 = (byte *)0x0;
                DAT_ffff00009090cf50 = 0;
                return 0x7014;
              }
              if (DAT_ffff00009090cf50 == 0) {
LAB_ffff0000907b1360:
                FUN_ffff00009077e11c
                          (s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
                return 0x700e;
              }
              pbVar5 = DAT_ffff00009090cf48 + 1;
              bVar1 = *pbVar5;
              uVar9 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar9 & 0x7f) + 1;
                uVar8 = uVar9 & 0x7f;
                uVar9 = 0;
                pbVar12 = DAT_ffff00009090cf48 + 2;
                do {
                  uVar8 = uVar8 - 1;
                  uVar9 = (uint)*pbVar12 | uVar9 << 8;
                  pbVar12 = pbVar12 + 1;
                } while (uVar8 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
                uVar9 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar9 + 1);
              uVar4 = DAT_ffff00009090cf50 - uVar6;
              if (DAT_ffff00009090cf50 < uVar6) {
                DAT_ffff00009090cf50 = 0;
                return 0x101028;
              }
              pbVar12 = DAT_ffff00009090cf48 + uVar6;
              if (*DAT_ffff00009090cf48 != 2) {
                DAT_ffff00009090cf48 = pbVar12;
                DAT_ffff00009090cf50 = uVar4;
                return 0x700f;
              }
              bVar1 = *pbVar5;
              if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
                DAT_ffff00009090cb64 = 0;
                lVar10 = uVar11 + 1;
                pbVar7 = DAT_ffff00009090cf48 + 2;
                do {
                  uVar9 = (int)uVar11 - 1;
                  uVar11 = (ulong)uVar9;
                  DAT_ffff00009090cb64 = (uint)*pbVar7 | DAT_ffff00009090cb64 << 8;
                  pbVar7 = pbVar7 + 1;
                } while (uVar9 != 0);
              }
              else {
                DAT_ffff00009090cb64 = (uint)bVar1;
                lVar10 = 1;
              }
              pbVar5 = pbVar5 + lVar10;
              DAT_ffff00009090cf48 = pbVar12;
              if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
                DAT_ffff00009090cb64 = DAT_ffff00009090cb64 - 1;
                if (DAT_ffff00009090cb64 < 5) {
                  pbVar5 = pbVar5 + 1;
LAB_ffff0000907b1204:
                  DAT_ffff00009090cf50 = uVar4;
                  FUN_ffff0000907ad234(&DAT_ffff00009090cb60,pbVar5,DAT_ffff00009090cb64);
                  DAT_ffff00009090cf48 = (byte *)0x0;
                  DAT_ffff00009090cf50 = 0;
                  goto joined_r0xffff0000907b1218;
                }
              }
              else if (DAT_ffff00009090cb64 < 5) goto LAB_ffff0000907b1204;
              bVar2 = DAT_ffff00009090cf50 != uVar6;
              DAT_ffff00009090cf50 = uVar4;
            } while (bVar2);
          }
          DAT_ffff00009090cf48 = (byte *)0x0;
          DAT_ffff00009090cf50 = 0;
          return 0x7014;
        }
joined_r0xffff0000907b1218:
        if (((param_3 >> 10 & 1) != 0) &&
           (uVar4 = FUN_ffff0000907b0968(PTR_DAT_ffff000090839ab0,PTR_DAT_ffff000090839ab0 + 4,2),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0xc & 1) != 0) && (uVar4 = FUN_ffff0000907b13bc(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 0xd & 1) != 0) {
          FUN_ffff0000907b157c();
        }
        if (((param_3 >> 0xe & 1) != 0) && (uVar4 = FUN_ffff0000907b171c(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0xf & 1) != 0) && (uVar4 = FUN_ffff0000907b18dc(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0x10 & 1) != 0) && (uVar4 = FUN_ffff0000907b1a60(), (int)uVar4 != 0)) {
          return uVar4;
        }
        return 0;
      }
      DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
      DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
      if (DAT_ffff00009090cc78 != 0) {
        do {
          iVar3 = FUN_ffff0000907af524(5);
          if (iVar3 != 0) {
            DAT_ffff00009090cf48 = (byte *)0x0;
            DAT_ffff00009090cf50 = 0;
            return 0x7014;
          }
          if (DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b1360;
          bVar1 = DAT_ffff00009090cf48[1];
          uVar9 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar9 & 0x7f) + 1;
            uVar8 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar5 = DAT_ffff00009090cf48 + 2;
            do {
              uVar8 = uVar8 - 1;
              uVar9 = (uint)*pbVar5 | uVar9 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar8 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar9 + 1);
          uVar4 = DAT_ffff00009090cf50 - uVar6;
          if (DAT_ffff00009090cf50 < uVar6) goto LAB_ffff0000907b1384;
          pbVar5 = DAT_ffff00009090cf48 + uVar6;
          if (*DAT_ffff00009090cf48 != 3) {
            DAT_ffff00009090cf48 = pbVar5;
            DAT_ffff00009090cf50 = uVar4;
            return 0x700f;
          }
          bVar1 = DAT_ffff00009090cf48[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            uVar9 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = DAT_ffff00009090cf48 + 2;
            do {
              uVar8 = (int)uVar11 - 1;
              uVar11 = (ulong)uVar8;
              uVar9 = (uint)*pbVar12 | uVar9 << 8;
              pbVar12 = pbVar12 + 1;
            } while (uVar8 != 0);
          }
          else {
            uVar9 = (uint)bVar1;
            lVar10 = 1;
          }
          DAT_ffff00009090cb20 = uVar9 - 1;
          if (DAT_ffff00009090cb20 < 0x31) {
            pbVar12 = DAT_ffff00009090cf48 + lVar10 + 2;
            DAT_ffff00009090cf48 = pbVar5;
            DAT_ffff00009090cf50 = uVar4;
            FUN_ffff0000907ad234(&DAT_ffff00009090caf0,pbVar12);
            DAT_ffff00009090cf48 = (byte *)0x0;
            DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b0f34;
          }
          bVar2 = DAT_ffff00009090cf50 != uVar6;
          DAT_ffff00009090cf48 = pbVar5;
          DAT_ffff00009090cf50 = uVar4;
        } while (bVar2);
      }
    }
    else {
      DAT_ffff00009090cf48 = local_48;
      DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
      if (local_4c != 0) {
        do {
          iVar3 = FUN_ffff0000907af524(10);
          if (iVar3 != 0) {
            DAT_ffff00009090cf48 = (byte *)0x0;
            DAT_ffff00009090cf50 = 0;
            return 0x7014;
          }
          if (DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b1360;
          bVar1 = DAT_ffff00009090cf48[1];
          uVar9 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar9 & 0x7f) + 1;
            uVar8 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar5 = DAT_ffff00009090cf48 + 2;
            do {
              uVar8 = uVar8 - 1;
              uVar9 = (uint)*pbVar5 | uVar9 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar8 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar9 + 1);
          uVar4 = DAT_ffff00009090cf50 - uVar6;
          if (DAT_ffff00009090cf50 < uVar6) goto LAB_ffff0000907b1384;
          pbVar5 = DAT_ffff00009090cf48 + uVar6;
          if (*DAT_ffff00009090cf48 != 3) {
            DAT_ffff00009090cf48 = pbVar5;
            DAT_ffff00009090cf50 = uVar4;
            return 0x700f;
          }
          bVar1 = DAT_ffff00009090cf48[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            uVar9 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = DAT_ffff00009090cf48 + 2;
            do {
              uVar8 = (int)uVar11 - 1;
              uVar11 = (ulong)uVar8;
              uVar9 = (uint)*pbVar12 | uVar9 << 8;
              pbVar12 = pbVar12 + 1;
            } while (uVar8 != 0);
          }
          else {
            uVar9 = (uint)bVar1;
            lVar10 = 1;
          }
          DAT_ffff00009090cb54 = uVar9 - 1;
          if (DAT_ffff00009090cb54 < 0x31) {
            pbVar12 = DAT_ffff00009090cf48 + lVar10 + 2;
            DAT_ffff00009090cf48 = pbVar5;
            DAT_ffff00009090cf50 = uVar4;
            FUN_ffff0000907ad234(&DAT_ffff00009090cb24,pbVar12);
            DAT_ffff00009090cf48 = (byte *)0x0;
            DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b1248;
          }
          bVar2 = DAT_ffff00009090cf50 != uVar6;
          DAT_ffff00009090cf48 = pbVar5;
          DAT_ffff00009090cf50 = uVar4;
        } while (bVar2);
      }
    }
    DAT_ffff00009090cf48 = (byte *)0x0;
    DAT_ffff00009090cf50 = 0;
    return 0x7014;
  }
  uVar9 = 1;
LAB_ffff0000907b0fb4:
  return (ulong)(uVar9 | 0x100000);
LAB_ffff0000907b1384:
  uVar9 = 0x1028;
  DAT_ffff00009090cf50 = 0;
  goto LAB_ffff0000907b0fb4;
}


