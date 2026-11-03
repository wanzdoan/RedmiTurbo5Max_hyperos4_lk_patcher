
/* WARNING: Type propagation algorithm not settling */

undefined8 FUN_ffff0000b806c958(long param_1,uint param_2,uint param_3)

{
  undefined *puVar1;
  uint *puVar2;
  byte bVar3;
  bool bVar4;
  int iVar5;
  undefined8 uVar6;
  long lVar7;
  byte *pbVar8;
  uint uVar9;
  ulong uVar10;
  ulong uVar11;
  byte *pbVar12;
  uint uVar13;
  byte *pbVar14;
  ulong uVar15;
  uint local_4c;
  byte *local_48;
  
  uVar9 = *(uint *)(param_1 + 0x34);
  local_48 = (byte *)0x0;
  local_4c = 0;
  DAT_ffff0000b81b8ea0 = (byte *)(param_1 + (ulong)uVar9);
  DAT_ffff0000b81b8eb0 = (ulong)(param_2 - uVar9);
  if (param_2 < uVar9 || param_2 - uVar9 == 0) {
    return 0x100001;
  }
  uVar6 = FUN_ffff0000b806cdc4(&local_48,&local_4c);
  if ((int)uVar6 != 0) {
    return uVar6;
  }
  DAT_ffff0000b81b8eb0 = (ulong)local_4c;
  DAT_ffff0000b81b8ea0 = local_48;
  if ((param_3 & 1) != 0) {
    DAT_ffff0000b81b9148 = local_48;
    if (local_4c != 0) {
      DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
      iVar5 = FUN_ffff0000b806bd1c(3);
      while (iVar5 == 0) {
        iVar5 = FUN_ffff0000b806f304(&DAT_ffff0000b81b88bc);
        if ((iVar5 == 0) || (DAT_ffff0000b81b9150 == 0)) {
          if (iVar5 != 0) {
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            return 0x7014;
          }
          DAT_ffff0000b81b9148 = (byte *)0x0;
          DAT_ffff0000b81b9150 = 0;
          goto joined_r0xffff0000b806cdbc;
        }
        iVar5 = FUN_ffff0000b806bd1c(3);
      }
    }
    DAT_ffff0000b81b9148 = (byte *)0x0;
    DAT_ffff0000b81b9150 = 0;
    return 0x7014;
  }
joined_r0xffff0000b806cdbc:
  if (((param_3 >> 1 & 1) != 0) &&
     (uVar6 = FUN_ffff0000b806cfb0(&DAT_ffff0000b81b8d10,&DAT_ffff0000b81b8d14), (int)uVar6 != 0)) {
    return uVar6;
  }
  if ((param_3 >> 2 & 1) == 0) {
joined_r0xffff0000b806cbd4:
    if ((param_3 >> 3 & 1) == 0) {
joined_r0xffff0000b806c9e8:
      if (((param_3 >> 10 & 1) != 0) &&
         (uVar6 = FUN_ffff0000b806d160
                            (PTR_DAT_ffff0000b81087f0 + 0x408,PTR_DAT_ffff0000b81087f0 + 0x40c,1),
         (int)uVar6 != 0)) {
        return uVar6;
      }
      if (((param_3 >> 0xb & 1) != 0) && (uVar6 = FUN_ffff0000b806d50c(), (int)uVar6 != 0)) {
        return uVar6;
      }
      if ((param_3 >> 4 & 1) == 0) {
        return 0;
      }
      puVar1 = PTR_DAT_ffff0000b81087f0 + 0x418;
      puVar2 = (uint *)(PTR_DAT_ffff0000b81087f0 + 0x448);
      DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
      DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
      while( true ) {
        if ((DAT_ffff0000b81b9150 == 0) ||
           (iVar5 = FUN_ffff0000b806bd1c(4), pbVar8 = DAT_ffff0000b81b9148, iVar5 != 0)) {
          DAT_ffff0000b81b9148 = (byte *)0x0;
          DAT_ffff0000b81b9150 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b9150 == 0) {
          FUN_ffff0000b804c33c
                    (s___s__end_of_cert__not_found__ffff0000b809d1a1,&DAT_ffff0000b80987e6);
          return 0x700e;
        }
        bVar3 = DAT_ffff0000b81b9148[1];
        uVar9 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar5 = (uVar9 & 0x7f) + 1;
          uVar13 = uVar9 & 0x7f;
          uVar9 = 0;
          pbVar14 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar13 = uVar13 - 1;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          iVar5 = 1;
        }
        if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
          uVar9 = 0;
        }
        uVar10 = (ulong)(iVar5 + uVar9 + 1);
        bVar4 = DAT_ffff0000b81b9150 < uVar10;
        DAT_ffff0000b81b9150 = DAT_ffff0000b81b9150 - uVar10;
        if (bVar4) break;
        if (*DAT_ffff0000b81b9148 != 3) {
          DAT_ffff0000b81b9148 = DAT_ffff0000b81b9148 + uVar10;
          return 0x700f;
        }
        bVar3 = DAT_ffff0000b81b9148[1];
        if (((char)bVar3 < '\0') && (uVar11 = (ulong)bVar3 & 0x7f, (int)uVar11 != 0)) {
          uVar9 = 0;
          lVar7 = uVar11 + 1;
          pbVar14 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar13 = (int)uVar11 - 1;
            uVar11 = (ulong)uVar13;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          uVar9 = (uint)bVar3;
          lVar7 = 1;
        }
        DAT_ffff0000b81b9148 = DAT_ffff0000b81b9148 + uVar10;
        *puVar2 = uVar9 - 1;
        if (uVar9 - 1 < 0x31) {
          FUN_ffff0000b8069a2c(puVar1,pbVar8 + lVar7 + 2);
          DAT_ffff0000b81b9150 = 0;
          DAT_ffff0000b81b9148 = (byte *)0x0;
          return 0;
        }
      }
      DAT_ffff0000b81b9150 = 0;
      return 0x101028;
    }
    DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
    DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
    if (DAT_ffff0000b81b8eb0 != 0) {
      do {
        iVar5 = FUN_ffff0000b806bd1c(7);
        if (iVar5 != 0) {
          DAT_ffff0000b81b9148 = (byte *)0x0;
          DAT_ffff0000b81b9150 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b9150 == 0) {
LAB_ffff0000b806cd50:
          FUN_ffff0000b804c33c
                    (s___s__end_of_cert__not_found__ffff0000b809d1a1,&DAT_ffff0000b80987e6);
          return 0x700e;
        }
        pbVar8 = DAT_ffff0000b81b9148 + 1;
        bVar3 = *pbVar8;
        uVar9 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar5 = (uVar9 & 0x7f) + 1;
          uVar13 = uVar9 & 0x7f;
          uVar9 = 0;
          pbVar14 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar13 = uVar13 - 1;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          iVar5 = 1;
        }
        if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
          uVar9 = 0;
        }
        uVar11 = (ulong)(iVar5 + uVar9 + 1);
        uVar10 = DAT_ffff0000b81b9150 - uVar11;
        if (DAT_ffff0000b81b9150 < uVar11) {
          DAT_ffff0000b81b9150 = 0;
          return 0x101028;
        }
        pbVar14 = DAT_ffff0000b81b9148 + uVar11;
        if (*DAT_ffff0000b81b9148 != 2) {
          DAT_ffff0000b81b9148 = pbVar14;
          DAT_ffff0000b81b9150 = uVar10;
          return 0x700f;
        }
        bVar3 = *pbVar8;
        if (((char)bVar3 < '\0') && (uVar15 = (ulong)bVar3 & 0x7f, (int)uVar15 != 0)) {
          DAT_ffff0000b81b8d0c = 0;
          lVar7 = uVar15 + 1;
          pbVar12 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar9 = (int)uVar15 - 1;
            uVar15 = (ulong)uVar9;
            DAT_ffff0000b81b8d0c = (uint)*pbVar12 | DAT_ffff0000b81b8d0c << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar9 != 0);
        }
        else {
          DAT_ffff0000b81b8d0c = (uint)bVar3;
          lVar7 = 1;
        }
        pbVar8 = pbVar8 + lVar7;
        DAT_ffff0000b81b9148 = pbVar14;
        if ((*pbVar8 == 0) && ((char)pbVar8[1] < '\0')) {
          DAT_ffff0000b81b8d0c = DAT_ffff0000b81b8d0c - 1;
          if (DAT_ffff0000b81b8d0c < 5) {
            pbVar8 = pbVar8 + 1;
LAB_ffff0000b806cd08:
            DAT_ffff0000b81b9150 = uVar10;
            FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8d08,pbVar8,DAT_ffff0000b81b8d0c);
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            goto joined_r0xffff0000b806c9e8;
          }
        }
        else if (DAT_ffff0000b81b8d0c < 5) goto LAB_ffff0000b806cd08;
        bVar4 = DAT_ffff0000b81b9150 != uVar11;
        DAT_ffff0000b81b9150 = uVar10;
      } while (bVar4);
    }
  }
  else {
    DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
    DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
    if (DAT_ffff0000b81b8eb0 != 0) {
      do {
        iVar5 = FUN_ffff0000b806bd1c(0xb);
        if (iVar5 != 0) {
          DAT_ffff0000b81b9148 = (byte *)0x0;
          DAT_ffff0000b81b9150 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b9150 == 0) goto LAB_ffff0000b806cd50;
        pbVar8 = DAT_ffff0000b81b9148 + 1;
        bVar3 = *pbVar8;
        uVar9 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar5 = (uVar9 & 0x7f) + 1;
          uVar13 = uVar9 & 0x7f;
          uVar9 = 0;
          pbVar14 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar13 = uVar13 - 1;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          iVar5 = 1;
        }
        if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
          uVar9 = 0;
        }
        uVar11 = (ulong)(iVar5 + uVar9 + 1);
        uVar10 = DAT_ffff0000b81b9150 - uVar11;
        if (DAT_ffff0000b81b9150 < uVar11) {
          DAT_ffff0000b81b9150 = 0;
          return 0x101028;
        }
        pbVar14 = DAT_ffff0000b81b9148 + uVar11;
        if (*DAT_ffff0000b81b9148 != 2) {
          DAT_ffff0000b81b9148 = pbVar14;
          DAT_ffff0000b81b9150 = uVar10;
          return 0x700f;
        }
        bVar3 = *pbVar8;
        if (((char)bVar3 < '\0') && (uVar15 = (ulong)bVar3 & 0x7f, (int)uVar15 != 0)) {
          DAT_ffff0000b81b8d1c = 0;
          lVar7 = uVar15 + 1;
          pbVar12 = DAT_ffff0000b81b9148 + 2;
          do {
            uVar9 = (int)uVar15 - 1;
            uVar15 = (ulong)uVar9;
            DAT_ffff0000b81b8d1c = (uint)*pbVar12 | DAT_ffff0000b81b8d1c << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar9 != 0);
        }
        else {
          DAT_ffff0000b81b8d1c = (uint)bVar3;
          lVar7 = 1;
        }
        pbVar8 = pbVar8 + lVar7;
        DAT_ffff0000b81b9148 = pbVar14;
        if ((*pbVar8 == 0) && ((char)pbVar8[1] < '\0')) {
          DAT_ffff0000b81b8d1c = DAT_ffff0000b81b8d1c - 1;
          if (DAT_ffff0000b81b8d1c < 5) {
            pbVar8 = pbVar8 + 1;
LAB_ffff0000b806cbc0:
            DAT_ffff0000b81b9150 = uVar10;
            FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8d18,pbVar8,DAT_ffff0000b81b8d1c);
            DAT_ffff0000b81b9148 = (byte *)0x0;
            DAT_ffff0000b81b9150 = 0;
            goto joined_r0xffff0000b806cbd4;
          }
        }
        else if (DAT_ffff0000b81b8d1c < 5) goto LAB_ffff0000b806cbc0;
        bVar4 = DAT_ffff0000b81b9150 != uVar11;
        DAT_ffff0000b81b9150 = uVar10;
      } while (bVar4);
    }
  }
  DAT_ffff0000b81b9150 = 0;
  DAT_ffff0000b81b9148 = (byte *)0x0;
  return 0x7014;
}


