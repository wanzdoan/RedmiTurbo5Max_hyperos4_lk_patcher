
void FUN_ffff0000907be450(ulong *param_1,long param_2,long param_3)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  long lVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  ulong uVar20;
  ulong uVar21;
  ulong uVar22;
  ulong uVar23;
  ulong uVar24;
  ulong uVar25;
  ulong uVar26;
  ulong uVar27;
  ulong local_2e8 [81];
  
  FUN_ffff00009077ee50(local_2e8,0,0x280);
  if (param_3 != 0) {
    uVar17 = *param_1;
    uVar18 = param_1[1];
    lVar16 = 0;
    param_2 = param_2 + 3;
    uVar19 = param_1[2];
    uVar20 = param_1[3];
    uVar21 = param_1[4];
    uVar22 = param_1[5];
    uVar23 = param_1[6];
    uVar7 = param_1[7];
    do {
      lVar8 = 0;
      do {
        uVar10 = *(ulong *)(param_2 + lVar8 + -3);
        uVar10 = (uVar10 & 0xff00ff00ff00ff00) >> 8 | (uVar10 & 0xff00ff00ff00ff) << 8;
        uVar10 = (uVar10 & 0xffff0000ffff0000) >> 0x10 | (uVar10 & 0xffff0000ffff) << 0x10;
        *(ulong *)((long)local_2e8 + lVar8) = uVar10 >> 0x20 | uVar10 << 0x20;
        lVar8 = lVar8 + 8;
      } while (lVar8 != 0x80);
      lVar8 = 0;
      uVar10 = local_2e8[0];
      do {
        lVar3 = lVar8 + 8;
        uVar12 = *(ulong *)((long)local_2e8 + lVar8 + 0x70);
        uVar14 = *(ulong *)((long)local_2e8 + lVar8 + 8);
        *(ulong *)((long)local_2e8 + lVar8 + 0x80) =
             ((uVar12 >> 0x13 | uVar12 << 0x2d) ^ (uVar12 >> 0x3d | uVar12 << 3) ^ uVar12 >> 6) +
             *(long *)((long)local_2e8 + lVar8 + 0x48) + uVar10 +
             ((uVar14 >> 1 | uVar14 << 0x3f) ^ (uVar14 >> 8 | uVar14 << 0x38) ^ uVar14 >> 7);
        lVar8 = lVar3;
        uVar10 = uVar14;
      } while (lVar3 != 0x200);
      lVar8 = 0;
      uVar14 = uVar23;
      uVar4 = uVar22;
      uVar13 = uVar20;
      uVar5 = uVar19;
      uVar6 = uVar18;
      uVar10 = uVar21;
      uVar12 = uVar17;
      uVar27 = uVar7;
      do {
        uVar26 = uVar12;
        uVar25 = uVar10;
        uVar24 = uVar6;
        uVar15 = uVar5;
        uVar11 = uVar4;
        uVar9 = uVar14;
        plVar1 = (long *)((long)&DAT_ffff000090814430 + lVar8);
        plVar2 = (long *)((long)local_2e8 + lVar8);
        lVar8 = lVar8 + 8;
        lVar3 = (uVar11 & uVar25) + uVar27 + (uVar9 & (uVar25 ^ 0xffffffffffffffff)) + *plVar1 +
                ((uVar25 >> 0xe | uVar25 << 0x32) ^ (uVar25 >> 0x12 | uVar25 << 0x2e) ^
                (uVar25 >> 0x29 | uVar25 << 0x17)) + *plVar2;
        uVar10 = uVar13 + lVar3;
        uVar12 = ((uVar26 >> 0x1c | uVar26 << 0x24) ^ (uVar26 >> 0x22 | uVar26 << 0x1e) ^
                 (uVar26 >> 0x27 | uVar26 << 0x19)) + ((uVar15 ^ uVar24) & uVar26 ^ uVar15 & uVar24)
                 + lVar3;
        uVar14 = uVar11;
        uVar4 = uVar25;
        uVar13 = uVar15;
        uVar5 = uVar24;
        uVar6 = uVar26;
        uVar27 = uVar9;
      } while (lVar8 != 0x280);
      uVar17 = uVar17 + uVar12;
      uVar18 = uVar18 + uVar26;
      uVar19 = uVar19 + uVar24;
      uVar20 = uVar20 + uVar15;
      uVar21 = uVar21 + uVar10;
      uVar22 = uVar22 + uVar25;
      uVar23 = uVar23 + uVar11;
      uVar7 = uVar7 + uVar9;
      lVar16 = lVar16 + 1;
      param_2 = param_2 + 0x80;
      *param_1 = uVar17;
      param_1[1] = uVar18;
      param_1[2] = uVar19;
      param_1[3] = uVar20;
      param_1[4] = uVar21;
      param_1[5] = uVar22;
      param_1[6] = uVar23;
      param_1[7] = uVar7;
    } while (lVar16 != param_3);
  }
  return;
}


