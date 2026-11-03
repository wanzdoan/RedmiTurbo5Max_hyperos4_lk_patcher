// File offset entry: 0x6b888

/* WARNING: Removing unreachable block (ram,0xffff0000b806bbbc) */

undefined8 FUN_ffff0000b806b888(ulong param_1)

{
  ulong uVar1;
  byte bVar2;
  uint uVar3;
  uint uVar4;
  bool bVar5;
  bool bVar6;
  char cVar7;
  char cVar8;
  uint uVar9;
  long lVar10;
  undefined8 uVar11;
  ulong uVar12;
  uint uVar13;
  byte *pbVar14;
  uint uVar15;
  int iVar16;
  ulong uVar17;
  long lVar18;
  ulong uVar19;
  uint uVar20;
  long lVar21;
  undefined1 *puVar22;
  byte *pbVar23;
  int iVar24;
  uint uVar25;
  ulong uVar26;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined4 local_84;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_84 = 0;
  FUN_ffff0000b80695a0(&local_80,(param_1 & 0xffffffff) * 0x40 + -0xffff47f56e78,0x40);
  FUN_ffff0000b80696ac(&local_80,&DAT_ffff0000b809ec32);
  cVar7 = FUN_ffff0000b8069668();
  FUN_ffff0000b80696ac(0,&DAT_ffff0000b809ec32);
  cVar8 = FUN_ffff0000b8069668();
  local_c8 = CONCAT71(local_c8._1_7_,cVar8 + cVar7 * '(');
  lVar10 = FUN_ffff0000b80696ac(0,&DAT_ffff0000b809ec32);
  if (lVar10 == 0) {
    iVar24 = 1;
  }
  else {
    iVar24 = 1;
    uVar26 = 1;
    do {
      uVar9 = FUN_ffff0000b8069668();
      uVar25 = (uint)uVar26;
      if (uVar9 < 0x80) {
        *(char *)((long)&local_c8 + uVar26) = (char)uVar9;
        uVar25 = uVar25 + 1;
        iVar24 = iVar24 + 1;
        if (0x3f < uVar25) break;
      }
      else {
        lVar10 = 0;
        uVar20 = 0;
        uVar4 = 0xfffffff9;
        do {
          uVar15 = uVar4;
          uVar13 = uVar20;
          lVar18 = lVar10;
          uVar12 = (ulong)uVar13;
          lVar10 = lVar18 + -1;
          uVar20 = uVar13 + 1;
          uVar4 = uVar15 + 7;
        } while (uVar9 >> (ulong)((uVar13 & 3) << 3) != 0);
        if (uVar13 != 0) {
          uVar20 = uVar13 - 1;
          if (uVar20 == 0) {
            uVar19 = 0;
LAB_ffff0000b806ba28:
            lVar10 = uVar19 + lVar18;
            lVar21 = ~(uint)lVar18 - uVar19;
            uVar15 = (int)uVar19 * -7 + uVar15;
            pbVar14 = (byte *)((long)&local_84 + uVar19);
            do {
              bVar6 = lVar21 != 0;
              uVar20 = uVar15 & 0x1f;
              lVar21 = lVar21 + -1;
              uVar15 = uVar15 - 7;
              bVar5 = lVar10 != -1;
              lVar10 = lVar10 + 1;
              *pbVar14 = (byte)(uVar9 >> (ulong)uVar20) & 0x7f | bVar6 << 7;
              pbVar14 = pbVar14 + 1;
            } while (bVar5);
          }
          else {
            uVar19 = uVar12 & 0x7ffffffe;
            uVar17 = 0;
            uVar4 = uVar15;
            do {
              uVar3 = uVar4 & 0x1f;
              uVar4 = uVar4 - 0xe;
              uVar1 = uVar17 + 2;
              *(byte *)((long)&local_84 + uVar17) =
                   (byte)(uVar9 >> (ulong)uVar3) & 0x7f | (uVar17 != uVar20) << 7;
              *(byte *)((long)&local_84 + uVar17 + 1) =
                   (byte)(uVar9 >> (ulong)((uVar13 + ((uint)uVar17 ^ 0xfffffffe)) * 7 & 0x1f)) &
                   0x7f | (uVar17 + 1 != (ulong)uVar20) << 7;
              uVar17 = uVar1;
            } while (uVar1 != uVar19);
            if (uVar12 != uVar19) goto LAB_ffff0000b806ba28;
          }
          if (uVar13 < 2) {
            uVar17 = 0;
          }
          else {
            uVar17 = 0;
            if ((!CARRY4(uVar25,(uint)(uVar12 - 1))) && (uVar12 - 1 >> 0x20 == 0)) {
              uVar17 = uVar12 & 0x7ffffffe;
              lVar10 = ((ulong)(-(uint)lVar18 >> 1) & 0x3fffffff) << 1;
              puVar22 = (undefined1 *)((ulong)&local_84 | 1);
              do {
                iVar16 = (int)uVar26;
                lVar10 = lVar10 + -2;
                *(undefined1 *)((long)&local_c8 + uVar26) = puVar22[-1];
                uVar26 = (ulong)(iVar16 + 2);
                *(undefined1 *)((long)&local_c8 + (ulong)(iVar16 + 1)) = *puVar22;
                puVar22 = puVar22 + 2;
              } while (lVar10 != 0);
              if (uVar12 == uVar17) goto LAB_ffff0000b806ba90;
            }
          }
          do {
            puVar22 = (undefined1 *)((long)&local_84 + uVar17);
            iVar16 = (int)uVar17;
            uVar17 = uVar17 + 1;
            *(undefined1 *)((long)&local_c8 + (ulong)(uVar25 + iVar16)) = *puVar22;
          } while (lVar18 + uVar17 != 0);
        }
LAB_ffff0000b806ba90:
        uVar25 = uVar13 + uVar25;
        iVar24 = uVar13 + iVar24;
      }
      uVar26 = (ulong)uVar25;
      lVar10 = FUN_ffff0000b80696ac(0,&DAT_ffff0000b809ec32);
    } while (lVar10 != 0);
  }
  do {
    if (DAT_ffff0000b81b8110 == 0) {
      return 0x7013;
    }
    while( true ) {
      pbVar14 = DAT_ffff0000b81b8108 + 1;
      bVar2 = *pbVar14;
      uVar25 = (uint)bVar2;
      if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
        uVar9 = (uVar25 & 0x7f) + 1;
        uVar20 = uVar25 & 0x7f;
        uVar25 = 0;
        pbVar23 = DAT_ffff0000b81b8108 + 2;
        do {
          uVar20 = uVar20 - 1;
          uVar25 = (uint)*pbVar23 | uVar25 << 8;
          pbVar23 = pbVar23 + 1;
        } while (uVar20 != 0);
      }
      else {
        uVar9 = 1;
      }
      bVar2 = *DAT_ffff0000b81b8108;
      if ((bVar2 & 0x20) != 0) {
        uVar25 = 0;
      }
      uVar26 = (ulong)(uVar9 + uVar25 + 1);
      bVar5 = DAT_ffff0000b81b8110 < uVar26;
      DAT_ffff0000b81b8110 = DAT_ffff0000b81b8110 - uVar26;
      if (bVar5) {
        DAT_ffff0000b81b8110 = 0;
        return 0x7013;
      }
      DAT_ffff0000b81b8108 = DAT_ffff0000b81b8108 + uVar26;
      if (bVar2 != 6) break;
      uVar11 = FUN_ffff0000b8069604(pbVar14 + uVar9,&local_c8,iVar24);
      if ((int)uVar11 == 0) {
        return uVar11;
      }
      if (DAT_ffff0000b81b8110 == 0) {
        return 0x7013;
      }
    }
  } while( true );
}


