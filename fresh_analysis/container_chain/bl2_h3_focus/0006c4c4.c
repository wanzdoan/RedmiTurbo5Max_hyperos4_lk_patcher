// File offset entry: 0x6c4c4

/* WARNING: Type propagation algorithm not settling */

undefined8 FUN_ffff0000b806c4c4(long param_1,uint param_2,uint param_3)

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
  
  uVar9 = *(uint *)(param_1 + 0x34);
  DAT_ffff0000b81b7e60 = (byte *)(param_1 + (ulong)uVar9);
  DAT_ffff0000b81b7e70 = (ulong)(param_2 - uVar9);
  if ((param_3 & 1) != 0) {
    if (uVar9 != param_2) {
      DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
      DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
      iVar5 = FUN_ffff0000b806b888(3);
      while (iVar5 == 0) {
        iVar5 = FUN_ffff0000b806eac8(&DAT_ffff0000b81b787c);
        if ((iVar5 == 0) || (DAT_ffff0000b81b8110 == 0)) {
          if (iVar5 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          goto joined_r0xffff0000b806ca28;
        }
        iVar5 = FUN_ffff0000b806b888(3);
      }
    }
    DAT_ffff0000b81b8108 = (byte *)0x0;
    DAT_ffff0000b81b8110 = 0;
    return 0x7014;
  }
joined_r0xffff0000b806ca28:
  if (((param_3 >> 1 & 1) != 0) &&
     (uVar6 = FUN_ffff0000b806ca30(&DAT_ffff0000b81b7cd0,&DAT_ffff0000b81b7cd4), (int)uVar6 != 0)) {
    return uVar6;
  }
  if ((param_3 >> 2 & 1) == 0) {
joined_r0xffff0000b806c6e8:
    if ((param_3 >> 3 & 1) == 0) {
joined_r0xffff0000b806c524:
      if (((param_3 >> 10 & 1) != 0) &&
         (uVar6 = FUN_ffff0000b806cbe0
                            (PTR_DAT_ffff0000b8107728 + 0x408,PTR_DAT_ffff0000b8107728 + 0x40c,1),
         (int)uVar6 != 0)) {
        return uVar6;
      }
      if ((param_3 >> 0xb & 1) == 0) {
joined_r0xffff0000b806c548:
        if ((param_3 >> 4 & 1) == 0) {
          return 0;
        }
        puVar1 = PTR_DAT_ffff0000b8107728 + 0x418;
        puVar2 = (uint *)(PTR_DAT_ffff0000b8107728 + 0x448);
        DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
        DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
        while( true ) {
          if ((DAT_ffff0000b81b8110 == 0) ||
             (iVar5 = FUN_ffff0000b806b888(4), pbVar8 = DAT_ffff0000b81b8108, iVar5 != 0)) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b8110 == 0) {
            FUN_ffff0000b804beb0
                      (s___s__end_of_cert__not_found__ffff0000b809c0a2,&DAT_ffff0000b809770d);
            return 0x700e;
          }
          bVar3 = DAT_ffff0000b81b8108[1];
          uVar9 = (uint)bVar3;
          if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
            iVar5 = (uVar9 & 0x7f) + 1;
            uVar13 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar14 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar13 = uVar13 - 1;
              uVar9 = (uint)*pbVar14 | uVar9 << 8;
              pbVar14 = pbVar14 + 1;
            } while (uVar13 != 0);
          }
          else {
            iVar5 = 1;
          }
          if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar10 = (ulong)(iVar5 + uVar9 + 1);
          bVar4 = DAT_ffff0000b81b8110 < uVar10;
          DAT_ffff0000b81b8110 = DAT_ffff0000b81b8110 - uVar10;
          if (bVar4) break;
          if (*DAT_ffff0000b81b8108 != 3) {
            DAT_ffff0000b81b8108 = DAT_ffff0000b81b8108 + uVar10;
            return 0x700f;
          }
          bVar3 = DAT_ffff0000b81b8108[1];
          if (((char)bVar3 < '\0') && (uVar11 = (ulong)bVar3 & 0x7f, (int)uVar11 != 0)) {
            uVar9 = 0;
            lVar7 = uVar11 + 1;
            pbVar14 = DAT_ffff0000b81b8108 + 2;
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
          DAT_ffff0000b81b8108 = DAT_ffff0000b81b8108 + uVar10;
          *puVar2 = uVar9 - 1;
          if (uVar9 - 1 < 0x31) {
            FUN_ffff0000b80695a0(puVar1,pbVar8 + lVar7 + 2);
            DAT_ffff0000b81b8110 = 0;
            DAT_ffff0000b81b8108 = (byte *)0x0;
            return 0;
          }
        }
        DAT_ffff0000b81b8110 = 0;
        return 0x101028;
      }
      DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
      DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
      if (DAT_ffff0000b81b7e70 != 0) {
        do {
          iVar5 = FUN_ffff0000b806b888(0x10);
          if (iVar5 != 0) {
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            return 0x7014;
          }
          if (DAT_ffff0000b81b8110 == 0) {
LAB_ffff0000b806c9e4:
            FUN_ffff0000b804beb0
                      (s___s__end_of_cert__not_found__ffff0000b809c0a2,&DAT_ffff0000b809770d);
            return 0x700e;
          }
          pbVar8 = DAT_ffff0000b81b8108 + 1;
          bVar3 = *pbVar8;
          uVar9 = (uint)bVar3;
          if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
            iVar5 = (uVar9 & 0x7f) + 1;
            uVar13 = uVar9 & 0x7f;
            uVar9 = 0;
            pbVar14 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar13 = uVar13 - 1;
              uVar9 = (uint)*pbVar14 | uVar9 << 8;
              pbVar14 = pbVar14 + 1;
            } while (uVar13 != 0);
          }
          else {
            iVar5 = 1;
          }
          if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
            uVar9 = 0;
          }
          uVar11 = (ulong)(iVar5 + uVar9 + 1);
          uVar10 = DAT_ffff0000b81b8110 - uVar11;
          if (DAT_ffff0000b81b8110 < uVar11) {
            DAT_ffff0000b81b8110 = 0;
            return 0x101028;
          }
          pbVar14 = DAT_ffff0000b81b8108 + uVar11;
          if (*DAT_ffff0000b81b8108 != 2) {
            DAT_ffff0000b81b8108 = pbVar14;
            DAT_ffff0000b81b8110 = uVar10;
            return 0x700f;
          }
          bVar3 = *pbVar8;
          if (((char)bVar3 < '\0') && (uVar15 = (ulong)bVar3 & 0x7f, (int)uVar15 != 0)) {
            DAT_ffff0000b81b7c90 = 0;
            lVar7 = uVar15 + 1;
            pbVar12 = DAT_ffff0000b81b8108 + 2;
            do {
              uVar9 = (int)uVar15 - 1;
              uVar15 = (ulong)uVar9;
              DAT_ffff0000b81b7c90 = (uint)*pbVar12 | DAT_ffff0000b81b7c90 << 8;
              pbVar12 = pbVar12 + 1;
            } while (uVar9 != 0);
          }
          else {
            DAT_ffff0000b81b7c90 = (uint)bVar3;
            lVar7 = 1;
          }
          pbVar8 = pbVar8 + lVar7;
          DAT_ffff0000b81b8108 = pbVar14;
          if ((*pbVar8 == 0) && ((char)pbVar8[1] < '\0')) {
            DAT_ffff0000b81b7c90 = DAT_ffff0000b81b7c90 - 1;
            if (DAT_ffff0000b81b7c90 < 5) {
              pbVar8 = pbVar8 + 1;
LAB_ffff0000b806c9a0:
              DAT_ffff0000b81b8110 = uVar10;
              FUN_ffff0000b80695a0(0xffff0000b81b7c8c,pbVar8,DAT_ffff0000b81b7c90);
              DAT_ffff0000b81b8108 = (byte *)0x0;
              DAT_ffff0000b81b8110 = 0;
              goto joined_r0xffff0000b806c548;
            }
          }
          else if (DAT_ffff0000b81b7c90 < 5) goto LAB_ffff0000b806c9a0;
          bVar4 = DAT_ffff0000b81b8110 != uVar11;
          DAT_ffff0000b81b8110 = uVar10;
        } while (bVar4);
      }
      DAT_ffff0000b81b8108 = (byte *)0x0;
      DAT_ffff0000b81b8110 = 0;
      return 0x7014;
    }
    DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
    DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
    if (DAT_ffff0000b81b7e70 != 0) {
      do {
        iVar5 = FUN_ffff0000b806b888(7);
        if (iVar5 != 0) {
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806c9e4;
        pbVar8 = DAT_ffff0000b81b8108 + 1;
        bVar3 = *pbVar8;
        uVar9 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar5 = (uVar9 & 0x7f) + 1;
          uVar13 = uVar9 & 0x7f;
          uVar9 = 0;
          pbVar14 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar13 = uVar13 - 1;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          iVar5 = 1;
        }
        if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
          uVar9 = 0;
        }
        uVar11 = (ulong)(iVar5 + uVar9 + 1);
        uVar10 = DAT_ffff0000b81b8110 - uVar11;
        if (DAT_ffff0000b81b8110 < uVar11) {
          DAT_ffff0000b81b8110 = 0;
          return 0x101028;
        }
        pbVar14 = DAT_ffff0000b81b8108 + uVar11;
        if (*DAT_ffff0000b81b8108 != 2) {
          DAT_ffff0000b81b8108 = pbVar14;
          DAT_ffff0000b81b8110 = uVar10;
          return 0x700f;
        }
        bVar3 = *pbVar8;
        if (((char)bVar3 < '\0') && (uVar15 = (ulong)bVar3 & 0x7f, (int)uVar15 != 0)) {
          DAT_ffff0000b81b7ccc = 0;
          lVar7 = uVar15 + 1;
          pbVar12 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar9 = (int)uVar15 - 1;
            uVar15 = (ulong)uVar9;
            DAT_ffff0000b81b7ccc = (uint)*pbVar12 | DAT_ffff0000b81b7ccc << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar9 != 0);
        }
        else {
          DAT_ffff0000b81b7ccc = (uint)bVar3;
          lVar7 = 1;
        }
        pbVar8 = pbVar8 + lVar7;
        DAT_ffff0000b81b8108 = pbVar14;
        if ((*pbVar8 == 0) && ((char)pbVar8[1] < '\0')) {
          DAT_ffff0000b81b7ccc = DAT_ffff0000b81b7ccc - 1;
          if (DAT_ffff0000b81b7ccc < 5) {
            pbVar8 = pbVar8 + 1;
LAB_ffff0000b806c81c:
            DAT_ffff0000b81b8110 = uVar10;
            FUN_ffff0000b80695a0(&DAT_ffff0000b81b7cc8,pbVar8,DAT_ffff0000b81b7ccc);
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            goto joined_r0xffff0000b806c524;
          }
        }
        else if (DAT_ffff0000b81b7ccc < 5) goto LAB_ffff0000b806c81c;
        bVar4 = DAT_ffff0000b81b8110 != uVar11;
        DAT_ffff0000b81b8110 = uVar10;
      } while (bVar4);
    }
  }
  else {
    DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
    DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
    if (DAT_ffff0000b81b7e70 != 0) {
      do {
        iVar5 = FUN_ffff0000b806b888(0xb);
        if (iVar5 != 0) {
          DAT_ffff0000b81b8108 = (byte *)0x0;
          DAT_ffff0000b81b8110 = 0;
          return 0x7014;
        }
        if (DAT_ffff0000b81b8110 == 0) goto LAB_ffff0000b806c9e4;
        pbVar8 = DAT_ffff0000b81b8108 + 1;
        bVar3 = *pbVar8;
        uVar9 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar5 = (uVar9 & 0x7f) + 1;
          uVar13 = uVar9 & 0x7f;
          uVar9 = 0;
          pbVar14 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar13 = uVar13 - 1;
            uVar9 = (uint)*pbVar14 | uVar9 << 8;
            pbVar14 = pbVar14 + 1;
          } while (uVar13 != 0);
        }
        else {
          iVar5 = 1;
        }
        if ((*DAT_ffff0000b81b8108 & 0x20) != 0) {
          uVar9 = 0;
        }
        uVar11 = (ulong)(iVar5 + uVar9 + 1);
        uVar10 = DAT_ffff0000b81b8110 - uVar11;
        if (DAT_ffff0000b81b8110 < uVar11) {
          DAT_ffff0000b81b8110 = 0;
          return 0x101028;
        }
        pbVar14 = DAT_ffff0000b81b8108 + uVar11;
        if (*DAT_ffff0000b81b8108 != 2) {
          DAT_ffff0000b81b8108 = pbVar14;
          DAT_ffff0000b81b8110 = uVar10;
          return 0x700f;
        }
        bVar3 = *pbVar8;
        if (((char)bVar3 < '\0') && (uVar15 = (ulong)bVar3 & 0x7f, (int)uVar15 != 0)) {
          DAT_ffff0000b81b7cdc = 0;
          lVar7 = uVar15 + 1;
          pbVar12 = DAT_ffff0000b81b8108 + 2;
          do {
            uVar9 = (int)uVar15 - 1;
            uVar15 = (ulong)uVar9;
            DAT_ffff0000b81b7cdc = (uint)*pbVar12 | DAT_ffff0000b81b7cdc << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar9 != 0);
        }
        else {
          DAT_ffff0000b81b7cdc = (uint)bVar3;
          lVar7 = 1;
        }
        pbVar8 = pbVar8 + lVar7;
        DAT_ffff0000b81b8108 = pbVar14;
        if ((*pbVar8 == 0) && ((char)pbVar8[1] < '\0')) {
          DAT_ffff0000b81b7cdc = DAT_ffff0000b81b7cdc - 1;
          if (DAT_ffff0000b81b7cdc < 5) {
            pbVar8 = pbVar8 + 1;
LAB_ffff0000b806c6d4:
            DAT_ffff0000b81b8110 = uVar10;
            FUN_ffff0000b80695a0(&DAT_ffff0000b81b7cd8,pbVar8,DAT_ffff0000b81b7cdc);
            DAT_ffff0000b81b8108 = (byte *)0x0;
            DAT_ffff0000b81b8110 = 0;
            goto joined_r0xffff0000b806c6e8;
          }
        }
        else if (DAT_ffff0000b81b7cdc < 5) goto LAB_ffff0000b806c6d4;
        bVar4 = DAT_ffff0000b81b8110 != uVar11;
        DAT_ffff0000b81b8110 = uVar10;
      } while (bVar4);
    }
  }
  DAT_ffff0000b81b8110 = 0;
  DAT_ffff0000b81b8108 = (byte *)0x0;
  return 0x7014;
}


