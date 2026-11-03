// ffff0000907a5424  size=472  FUN_ffff0000907a5424

void FUN_ffff0000907a5424(long param_1,long param_2)

{
  long *plVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  long lVar19;
  ulong uVar20;
  ulong uVar21;
  ulong uVar22;
  ulong uVar23;
  ulong auStack_358 [8];
  long alStack_318 [5];
  ulong auStack_2f0 [82];
  
  FUN_ffff00009077ee50(auStack_2f0 + 2,0,0x280);
  uVar3 = *(ulong *)(param_1 + 0x10);
  uVar7 = *(ulong *)(param_1 + 0x18);
  lVar19 = 0;
  uVar4 = *(ulong *)(param_1 + 0x20);
  uVar8 = *(ulong *)(param_1 + 0x28);
  uVar5 = *(ulong *)(param_1 + 0x30);
  uVar9 = *(ulong *)(param_1 + 0x38);
  uVar6 = *(ulong *)(param_1 + 0x40);
  uVar10 = *(ulong *)(param_1 + 0x48);
  *(undefined8 *)(param_1 + 0x58) = 0x80;
  do {
    uVar22 = (*(ulong *)(param_2 + lVar19) & 0xff00ff00ff00ff00) >> 8 |
             (*(ulong *)(param_2 + lVar19) & 0xff00ff00ff00ff) << 8;
    uVar22 = (uVar22 & 0xffff0000ffff0000) >> 0x10 | (uVar22 & 0xffff0000ffff) << 0x10;
    *(ulong *)((long)auStack_2f0 + lVar19 + 0x10) = uVar22 >> 0x20 | uVar22 << 0x20;
    lVar19 = lVar19 + 8;
  } while (lVar19 != 0x80);
  lVar19 = 0x80;
  do {
    lVar2 = lVar19 + 8;
    uVar22 = *(ulong *)((long)auStack_2f0 + lVar19);
    uVar23 = *(ulong *)((long)auStack_358 + lVar19);
    *(ulong *)((long)auStack_2f0 + lVar19 + 0x10) =
         ((uVar22 >> 0x13 | uVar22 << 0x2d) ^ (uVar22 >> 0x3d | uVar22 << 3) ^ uVar22 >> 6) +
         *(long *)((long)alStack_318 + lVar19) + auStack_2f0[2] +
         ((uVar23 >> 1 | uVar23 << 0x3f) ^ (uVar23 >> 8 | uVar23 << 0x38) ^ uVar23 >> 7);
    lVar19 = lVar2;
    auStack_2f0[2] = uVar23;
  } while (lVar2 != 0x280);
  lVar19 = 0;
  uVar11 = uVar7;
  uVar13 = uVar4;
  uVar23 = uVar3;
  uVar22 = uVar5;
  uVar17 = uVar10;
  uVar12 = uVar6;
  uVar16 = uVar9;
  uVar21 = uVar8;
  do {
    uVar20 = uVar16;
    uVar18 = uVar12;
    uVar16 = uVar22;
    uVar15 = uVar23;
    uVar14 = uVar13;
    uVar13 = uVar11;
    plVar1 = (long *)((long)&DAT_ffff000090812ba8 + lVar19);
    lVar2 = lVar19 + 0x10;
    lVar19 = lVar19 + 8;
    lVar2 = ((uVar16 >> 0xe | uVar16 << 0x32) ^ (uVar16 >> 0x12 | uVar16 << 0x2e) ^
            (uVar16 >> 0x29 | uVar16 << 0x17)) + uVar17 + *plVar1 +
            *(long *)((long)auStack_2f0 + lVar2) +
            (uVar20 & uVar16 | uVar18 & (uVar16 ^ 0xffffffffffffffff));
    uVar22 = uVar21 + lVar2;
    uVar23 = lVar2 + ((uVar15 >> 0x1c | uVar15 << 0x24) ^ (uVar15 >> 0x22 | uVar15 << 0x1e) ^
                     (uVar15 >> 0x27 | uVar15 << 0x19)) +
             ((uVar13 | uVar15) & uVar14 | uVar13 & uVar15);
    uVar11 = uVar15;
    uVar17 = uVar18;
    uVar12 = uVar20;
    uVar21 = uVar14;
  } while (lVar19 != 0x280);
  *(ulong *)(param_1 + 0x10) = uVar23 + uVar3;
  *(ulong *)(param_1 + 0x18) = uVar15 + uVar7;
  *(ulong *)(param_1 + 0x20) = uVar13 + uVar4;
  *(ulong *)(param_1 + 0x28) = uVar14 + uVar8;
  *(ulong *)(param_1 + 0x30) = uVar22 + uVar5;
  *(ulong *)(param_1 + 0x38) = uVar16 + uVar9;
  *(ulong *)(param_1 + 0x40) = uVar20 + uVar6;
  *(ulong *)(param_1 + 0x48) = uVar18 + uVar10;
  return;
}


