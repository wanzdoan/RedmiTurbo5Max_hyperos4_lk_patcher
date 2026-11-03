
/* WARNING: Type propagation algorithm not settling */

ulong FUN_ffff0000b806d6bc(long param_1,uint param_2,uint param_3)

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
  DAT_ffff0000b81b8ea0 = (byte *)(param_1 + (ulong)uVar9);
  DAT_ffff0000b81b8eb0 = (ulong)(param_2 - uVar9);
  if (uVar9 <= param_2 && param_2 - uVar9 != 0) {
    uVar4 = FUN_ffff0000b806cdc4(&local_48,&local_4c);
    if ((int)uVar4 != 0) {
      return uVar4;
    }
    DAT_ffff0000b81b8eb0 = (ulong)local_4c;
    DAT_ffff0000b81b8ea0 = local_48;
    if ((param_3 >> 5 & 1) == 0) {
joined_r0xffff0000b806da40:
      if ((param_3 >> 6 & 1) == 0) {
joined_r0xffff0000b806d72c:
        if (((param_3 >> 1 & 1) != 0) &&
           (uVar4 = FUN_ffff0000b806cfb0(&DAT_ffff0000b81b8d90,&DAT_ffff0000b81b8d94),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 7 & 1) != 0) {
          DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
          DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
          if (DAT_ffff0000b81b8eb0 != 0) {
            do {
              iVar3 = FUN_ffff0000b806bd1c(9);
              if (iVar3 != 0) {
                DAT_ffff0000b81b9148 = (byte *)0x0;
                DAT_ffff0000b81b9150 = 0;
                return 0x7014;
              }
              if (DAT_ffff0000b81b9150 == 0) {
LAB_ffff0000b806db58:
                FUN_ffff0000b804c33c
                          (s___s__end_of_cert__not_found__ffff0000b809d1a1,&DAT_ffff0000b80987e6);
                return 0x700e;
              }
              pbVar5 = DAT_ffff0000b81b9148 + 1;
              bVar1 = *pbVar5;
              uVar9 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar9 & 0x7f) + 1;
                uVar8 = uVar9 & 0x7f;
                uVar9 = 0;
                pbVar12 = DAT_ffff0000b81b9148 + 2;
                do {
                  uVar8 = uVar8 - 1;
                  uVar9 = (uint)*pbVar12 | uVar9 << 8;
                  pbVar12 = pbVar12 + 1;
                } while (uVar8 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
                uVar9 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar9 + 1);
              uVar4 = DAT_ffff0000b81b9150 - uVar6;
              if (DAT_ffff0000b81b9150 < uVar6) {
                DAT_ffff0000b81b9150 = 0;
                return 0x101028;
              }
              pbVar12 = DAT_ffff0000b81b9148 + uVar6;
              if (*DAT_ffff0000b81b9148 != 2) {
                DAT_ffff0000b81b9148 = pbVar12;
                DAT_ffff0000b81b9150 = uVar4;
                return 0x700f;
              }
              bVar1 = *pbVar5;
              if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
                DAT_ffff0000b81b8d9c = 0;
                lVar10 = uVar11 + 1;
                pbVar7 = DAT_ffff0000b81b9148 + 2;
                do {
                  uVar9 = (int)uVar11 - 1;
                  uVar11 = (ulong)uVar9;
                  DAT_ffff0000b81b8d9c = (uint)*pbVar7 | DAT_ffff0000b81b8d9c << 8;
                  pbVar7 = pbVar7 + 1;
                } while (uVar9 != 0);
              }
              else {
                DAT_ffff0000b81b8d9c = (uint)bVar1;
                lVar10 = 1;
              }
              pbVar5 = pbVar5 + lVar10;
              DAT_ffff0000b81b9148 = pbVar12;
              if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
                DAT_ffff0000b81b8d9c = DAT_ffff0000b81b8d9c - 1;
                if (DAT_ffff0000b81b8d9c < 5) {
                  pbVar5 = pbVar5 + 1;
LAB_ffff0000b806d9fc:
                  DAT_ffff0000b81b9150 = uVar4;
                  FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8d98,pbVar5,DAT_ffff0000b81b8d9c);
                  DAT_ffff0000b81b9148 = (byte *)0x0;
                  DAT_ffff0000b81b9150 = 0;
                  goto joined_r0xffff0000b806da10;
                }
              }
              else if (DAT_ffff0000b81b8d9c < 5) goto LAB_ffff0000b806d9fc;
              bVar2 = DAT_ffff0000b81b9150 != uVar6;
              DAT_ffff0000b81b9150 = uVar4;
            } while (bVar2);
          }
          DAT_ffff0000b81b9148 = (byte *)0x0;
          DAT_ffff0000b81b9150 = 0;
          return 0x7014;
        }
joined_r0xffff0000b806da10:
        if (((param_3 >> 10 & 1) != 0) &&
           (uVar4 = FUN_ffff0000b806d160(PTR_DAT_ffff0000b81087f8,PTR_DAT_ffff0000b81087f8 + 4,2),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0xc & 1) != 0) && (uVar4 = FUN_ffff0000b806dbb4(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 0xd & 1) != 0) {
          FUN_ffff0000b806dd74();
        }
        if (((param_3 >> 0xe & 1) != 0) && (uVar4 = FUN_ffff0000b806df14(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0xf & 1) != 0) && (uVar4 = FUN_ffff0000b806e0d4(), (int)uVar4 != 0)) {
          return uVar4;
        }
        if (((param_3 >> 0x10 & 1) != 0) && (uVar4 = FUN_ffff0000b806e258(), (int)uVar4 != 0)) {
          return uVar4;
        }
        return 0;
      }
      DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
      DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
      if (DAT_ffff0000b81b8eb0 != 0) {
        do {
          iVar3 = FUN_ffff0000b806bd1c(5);
          if (iVar3 != 0) {
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b9150 == 0) goto LAB_ffff0000b806db58;
          bVar1 = DAT_ffff0000b81b9148[1];
          uVar9 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar9 & 0x7f) + 1;
            uVar8 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar5 = DAT_ffff0000b81b9148 + 2;
            do {
              uVar8 = uVar8 - 1;
              uVar9 = (uint)*pbVar5 | uVar9 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar8 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar9 + 1);
          uVar4 = DAT_ffff0000b81b9150 - uVar6;
          if (DAT_ffff0000b81b9150 < uVar6) goto LAB_ffff0000b806db7c;
          pbVar5 = DAT_ffff0000b81b9148 + uVar6;
          if (*DAT_ffff0000b81b9148 != 3) {
            DAT_ffff0000b81b9148 = pbVar5;
            DAT_ffff0000b81b9150 = uVar4;
            return 0x700f;
          }
          bVar1 = DAT_ffff0000b81b9148[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            uVar9 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = DAT_ffff0000b81b9148 + 2;
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
          DAT_ffff0000b81b8d58 = uVar9 - 1;
          if (DAT_ffff0000b81b8d58 < 0x31) {
            pbVar12 = DAT_ffff0000b81b9148 + lVar10 + 2;
            DAT_ffff0000b81b9148 = pbVar5;
            DAT_ffff0000b81b9150 = uVar4;
            FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8d28,pbVar12);
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            goto joined_r0xffff0000b806d72c;
          }
          bVar2 = DAT_ffff0000b81b9150 != uVar6;
          DAT_ffff0000b81b9148 = pbVar5;
          DAT_ffff0000b81b9150 = uVar4;
        } while (bVar2);
      }
    }
    else {
      DAT_ffff0000b81b9148 = local_48;
      DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
      if (local_4c != 0) {
        do {
          iVar3 = FUN_ffff0000b806bd1c(10);
          if (iVar3 != 0) {
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b9150 == 0) goto LAB_ffff0000b806db58;
          bVar1 = DAT_ffff0000b81b9148[1];
          uVar9 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar9 & 0x7f) + 1;
            uVar8 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar5 = DAT_ffff0000b81b9148 + 2;
            do {
              uVar8 = uVar8 - 1;
              uVar9 = (uint)*pbVar5 | uVar9 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar8 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar9 + 1);
          uVar4 = DAT_ffff0000b81b9150 - uVar6;
          if (DAT_ffff0000b81b9150 < uVar6) goto LAB_ffff0000b806db7c;
          pbVar5 = DAT_ffff0000b81b9148 + uVar6;
          if (*DAT_ffff0000b81b9148 != 3) {
            DAT_ffff0000b81b9148 = pbVar5;
            DAT_ffff0000b81b9150 = uVar4;
            return 0x700f;
          }
          bVar1 = DAT_ffff0000b81b9148[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            uVar9 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = DAT_ffff0000b81b9148 + 2;
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
          DAT_ffff0000b81b8d8c = uVar9 - 1;
          if (DAT_ffff0000b81b8d8c < 0x31) {
            pbVar12 = DAT_ffff0000b81b9148 + lVar10 + 2;
            DAT_ffff0000b81b9148 = pbVar5;
            DAT_ffff0000b81b9150 = uVar4;
            FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8d5c,pbVar12);
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            goto joined_r0xffff0000b806da40;
          }
          bVar2 = DAT_ffff0000b81b9150 != uVar6;
          DAT_ffff0000b81b9148 = pbVar5;
          DAT_ffff0000b81b9150 = uVar4;
        } while (bVar2);
      }
    }
    DAT_ffff0000b81b9148 = (byte *)0x0;
    DAT_ffff0000b81b9150 = 0;
    return 0x7014;
  }
  uVar9 = 1;
LAB_ffff0000b806d7ac:
  return (ulong)(uVar9 | 0x100000);
LAB_ffff0000b806db7c:
  uVar9 = 0x1028;
  DAT_ffff0000b81b9150 = 0;
  goto LAB_ffff0000b806d7ac;
}


