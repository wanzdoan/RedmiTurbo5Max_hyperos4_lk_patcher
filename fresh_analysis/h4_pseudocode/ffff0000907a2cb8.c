
void FUN_ffff0000907a2cb8(long param_1,long param_2)

{
  uint *puVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
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
  ulong uVar28;
  ulong uVar29;
  ulong uVar30;
  ulong uVar31;
  ulong uVar32;
  ulong local_e0 [16];
  
  lVar10 = 0;
  param_2 = param_2 + 3;
  local_e0[0xe] = 0;
  local_e0[0xf] = 0;
  local_e0[0xc] = 0;
  local_e0[0xd] = 0;
  local_e0[10] = 0;
  local_e0[0xb] = 0;
  local_e0[8] = 0;
  local_e0[9] = 0;
  local_e0[6] = 0;
  local_e0[7] = 0;
  local_e0[4] = 0;
  local_e0[5] = 0;
  local_e0[2] = 0;
  local_e0[3] = 0;
  local_e0[0] = 0;
  local_e0[1] = 0;
  do {
    puVar1 = (uint *)(param_2 + -3);
    param_2 = param_2 + 4;
    uVar7 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
    *(ulong *)((long)local_e0 + lVar10) = (ulong)(uVar7 >> 0x10 | uVar7 << 0x10);
    lVar10 = lVar10 + 8;
  } while (lVar10 != 0x80);
  uVar3 = *(ulong *)(param_1 + 0x10);
  uVar5 = *(ulong *)(param_1 + 0x18);
  uVar4 = *(ulong *)(param_1 + 0x20);
  uVar6 = *(ulong *)(param_1 + 0x28);
  uVar13 = uVar5 << 0x1e | uVar5 >> 2 & 0x3fffffff;
  uVar2 = (uVar3 >> 0x1b & 0x1f | uVar3 << 5) + *(long *)(param_1 + 0x30) + local_e0[0] +
          (uVar4 & uVar5 | uVar6 & (uVar5 ^ 0xffffffffffffffff)) + 0x5a827999;
  uVar8 = uVar3 << 0x1e | uVar3 >> 2 & 0x3fffffff;
  uVar23 = uVar6 + local_e0[1] + (uVar13 & uVar3 | uVar4 & (uVar3 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar9 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar4 + local_e0[2] + (uVar8 & uVar2 | uVar13 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar14 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar13 + local_e0[3] + (uVar9 & uVar23 | uVar8 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar13 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar8 + local_e0[4] + (uVar14 & uVar2 | uVar9 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar8 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = local_e0[5] + uVar9 + (uVar13 & uVar23 | uVar14 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar21 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = local_e0[6] + uVar14 + (uVar8 & uVar2 | uVar13 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar23 = local_e0[7] + uVar13 + (uVar9 & uVar23 | uVar8 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar13 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = local_e0[8] + uVar8 + (uVar21 & uVar2 | uVar9 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar14 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = local_e0[9] + uVar9 + (uVar13 & uVar23 | uVar21 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar9 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = local_e0[10] + uVar21 + (uVar14 & uVar2 | uVar13 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar8 = local_e0[0xb] + uVar13 + (uVar9 & uVar23 | uVar14 & (uVar23 ^ 0xffffffffffffffff)) +
          uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar13 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar21 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = local_e0[0xc] + uVar14 + (uVar13 & uVar2 | uVar9 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar8 * 0x20 + ((uVar8 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar23 = local_e0[0xd] + uVar9 + (uVar21 & uVar8 | uVar13 & (uVar8 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar8 = uVar8 * 0x40000000 | uVar8 >> 2 & 0x3fffffff;
  uVar14 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = local_e0[0xe] + uVar13 + (uVar8 & uVar2 | uVar21 & (uVar2 ^ 0xffffffffffffffff)) +
          uVar23 * 0x20 + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar9 = local_e0[2] ^ local_e0[0] ^ local_e0[8] ^ local_e0[0xd];
  uVar13 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar11 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = local_e0[0xf] + uVar21 + (uVar14 & uVar23 | uVar8 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar21 = local_e0[3] ^ local_e0[1] ^ local_e0[9] ^ local_e0[0xe];
  uVar9 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar12 = uVar21 << 1 | uVar21 >> 0x1f & 1;
  uVar2 = uVar11 + uVar8 + (uVar13 & uVar2 | uVar14 & (uVar2 ^ 0xffffffffffffffff)) + uVar23 * 0x20
          + ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar8 = local_e0[4] ^ local_e0[2] ^ local_e0[10] ^ local_e0[0xf];
  uVar21 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar8 = uVar8 << 1 | uVar8 >> 0x1f & 1;
  uVar23 = uVar12 + uVar14 + (uVar9 & uVar23 | uVar13 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar14 = local_e0[5] ^ local_e0[3] ^ local_e0[0xb] ^ uVar11;
  uVar28 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar8 + uVar13 + (uVar21 & uVar2 | uVar9 & (uVar2 ^ 0xffffffffffffffff)) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar14 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar13 = local_e0[6] ^ local_e0[4] ^ local_e0[0xc] ^ uVar12;
  uVar30 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar14 + uVar9 + (uVar28 & uVar23 | uVar21 & (uVar23 ^ 0xffffffffffffffff)) +
           uVar2 * 0x20 + ((uVar2 & 0xffffffff) >> 0x1b) + 0x5a827999;
  uVar22 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar13 = local_e0[7] ^ local_e0[5] ^ local_e0[0xd] ^ uVar8;
  uVar24 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar26 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar2 = uVar22 + uVar21 + (uVar30 ^ uVar28 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar13 = local_e0[8] ^ local_e0[6] ^ local_e0[0xe] ^ uVar14;
  uVar32 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar27 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar23 = uVar26 + uVar28 + (uVar24 ^ uVar30 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar13 = local_e0[9] ^ local_e0[7] ^ local_e0[0xf] ^ uVar22;
  uVar9 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar29 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar2 = uVar27 + uVar30 + (uVar32 ^ uVar24 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar13 = local_e0[10] ^ local_e0[8] ^ uVar11 ^ uVar26;
  uVar21 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar25 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar23 = uVar29 + uVar24 + (uVar9 ^ uVar32 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar13 = local_e0[0xb] ^ local_e0[9] ^ uVar12 ^ uVar27;
  uVar28 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar25 + uVar32 + (uVar21 ^ uVar9 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar15 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar13 = local_e0[0xc] ^ local_e0[10] ^ uVar8 ^ uVar29;
  uVar24 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar15 + uVar9 + (uVar28 ^ uVar21 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar13 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar9 = local_e0[0xd] ^ local_e0[0xb] ^ uVar14 ^ uVar25;
  uVar30 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar13 + uVar21 + (uVar24 ^ uVar28 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar9 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar21 = local_e0[0xe] ^ local_e0[0xc] ^ uVar22 ^ uVar15;
  uVar18 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar9 + uVar28 + (uVar30 ^ uVar24 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar21 = uVar21 << 1 | uVar21 >> 0x1f & 1;
  uVar28 = local_e0[0xf] ^ local_e0[0xd] ^ uVar26 ^ uVar13;
  uVar31 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar21 + uVar24 + (uVar18 ^ uVar30 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar24 = uVar28 << 1 | uVar28 >> 0x1f & 1;
  uVar28 = uVar11 ^ local_e0[0xe] ^ uVar27 ^ uVar9;
  uVar17 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar24 + uVar30 + (uVar31 ^ uVar18 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar30 = uVar28 << 1 | uVar28 >> 0x1f & 1;
  uVar32 = uVar12 ^ local_e0[0xf] ^ uVar29 ^ uVar21;
  uVar28 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar16 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar2 = uVar30 + uVar18 + (uVar17 ^ uVar31 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar32 = uVar8 ^ uVar11 ^ uVar25 ^ uVar24;
  uVar11 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar19 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar23 = uVar16 + uVar31 + (uVar28 ^ uVar17 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar32 = uVar14 ^ uVar12 ^ uVar15 ^ uVar30;
  uVar12 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar20 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar2 = uVar19 + uVar17 + (uVar11 ^ uVar28 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar32 = uVar22 ^ uVar8 ^ uVar13 ^ uVar16;
  uVar8 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar31 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar23 = uVar20 + uVar28 + (uVar12 ^ uVar11 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar14 = uVar26 ^ uVar14 ^ uVar9 ^ uVar19;
  uVar32 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar31 + uVar11 + (uVar8 ^ uVar12 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar18 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar14 = uVar27 ^ uVar22 ^ uVar21 ^ uVar20;
  uVar11 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar28 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar23 = uVar18 + uVar12 + (uVar32 ^ uVar8 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar14 = uVar29 ^ uVar26 ^ uVar24 ^ uVar31;
  uVar12 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar28 + uVar8 + (uVar11 ^ uVar32 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar14 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar8 = uVar25 ^ uVar27 ^ uVar30 ^ uVar18;
  uVar26 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar17 = uVar8 << 1 | uVar8 >> 0x1f & 1;
  uVar23 = uVar14 + uVar32 + (uVar12 ^ uVar11 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar32 = uVar15 ^ uVar29 ^ uVar16 ^ uVar28;
  uVar8 = uVar17 + uVar11 + (uVar26 ^ uVar12 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar29 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar11 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar22 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar32 = uVar13 ^ uVar25 ^ uVar19 ^ uVar14;
  uVar2 = uVar11 + uVar12 + (uVar29 ^ uVar26 ^ uVar23) + uVar8 * 0x20 +
          ((uVar8 & 0xffffffff) >> 0x1b) + 0x6ed9eba1;
  uVar12 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar25 = uVar8 * 0x40000000 | uVar8 >> 2 & 0x3fffffff;
  uVar23 = uVar9 ^ uVar15 ^ uVar20 ^ uVar17;
  uVar15 = uVar23 << 1 | uVar23 >> 0x1f & 1;
  uVar23 = uVar12 + uVar26 + ((uVar8 | uVar22) & uVar29 | uVar8 & uVar22) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar27 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar8 = uVar21 ^ uVar13 ^ uVar31 ^ uVar11;
  uVar8 = uVar8 << 1 | uVar8 >> 0x1f & 1;
  uVar2 = uVar15 + uVar29 + ((uVar2 | uVar25) & uVar22 | uVar2 & uVar25) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar32 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar13 = uVar24 ^ uVar9 ^ uVar18 ^ uVar12;
  uVar13 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar23 = uVar8 + uVar22 + ((uVar23 | uVar27) & uVar25 | uVar23 & uVar27) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar26 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar30 ^ uVar21 ^ uVar28 ^ uVar15;
  uVar21 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar13 + uVar25 + ((uVar2 | uVar32) & uVar27 | uVar2 & uVar32) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar29 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar16 ^ uVar24 ^ uVar14 ^ uVar8;
  uVar24 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar21 + uVar27 + ((uVar23 | uVar26) & uVar32 | uVar23 & uVar26) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar25 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar19 ^ uVar30 ^ uVar17 ^ uVar13;
  uVar30 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar24 + uVar32 + ((uVar2 | uVar29) & uVar26 | uVar2 & uVar29) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar27 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar20 ^ uVar16 ^ uVar11 ^ uVar21;
  uVar22 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar30 + uVar26 + ((uVar23 | uVar25) & uVar29 | uVar23 & uVar25) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar26 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar31 ^ uVar19 ^ uVar12 ^ uVar24;
  uVar19 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar22 + uVar29 + ((uVar2 | uVar27) & uVar25 | uVar2 & uVar27) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar32 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar18 ^ uVar20 ^ uVar15 ^ uVar30;
  uVar20 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar19 + uVar25 + ((uVar23 | uVar26) & uVar27 | uVar23 & uVar26) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar25 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar28 ^ uVar31 ^ uVar8 ^ uVar22;
  uVar31 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar20 + uVar27 + ((uVar2 | uVar32) & uVar26 | uVar2 & uVar32) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar27 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar14 ^ uVar18 ^ uVar13 ^ uVar19;
  uVar16 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar31 + uVar26 + ((uVar23 | uVar25) & uVar32 | uVar23 & uVar25) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar18 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar17 ^ uVar28 ^ uVar21 ^ uVar20;
  uVar28 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar16 + uVar32 + ((uVar2 | uVar27) & uVar25 | uVar2 & uVar27) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar26 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar9 = uVar11 ^ uVar14 ^ uVar24 ^ uVar31;
  uVar9 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar28 + uVar25 + ((uVar23 | uVar18) & uVar27 | uVar23 & uVar18) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar29 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar14 = uVar12 ^ uVar17 ^ uVar30 ^ uVar16;
  uVar17 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar2 = uVar9 + uVar27 + ((uVar2 | uVar26) & uVar18 | uVar2 & uVar26) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar25 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar14 = uVar15 ^ uVar11 ^ uVar22 ^ uVar28;
  uVar32 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar23 = uVar17 + uVar18 + ((uVar23 | uVar29) & uVar26 | uVar23 & uVar29) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar27 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar14 = uVar8 ^ uVar12 ^ uVar19 ^ uVar9;
  uVar11 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar2 = uVar32 + uVar26 + ((uVar2 | uVar25) & uVar29 | uVar2 & uVar25) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar26 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar14 = uVar13 ^ uVar15 ^ uVar20 ^ uVar17;
  uVar18 = uVar14 << 1 | uVar14 >> 0x1f & 1;
  uVar23 = uVar11 + uVar29 + ((uVar23 | uVar27) & uVar25 | uVar23 & uVar27) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar14 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar8 = uVar21 ^ uVar8 ^ uVar31 ^ uVar32;
  uVar8 = uVar8 << 1 | uVar8 >> 0x1f & 1;
  uVar2 = uVar18 + uVar25 + ((uVar2 | uVar26) & uVar27 | uVar2 & uVar26) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar13 = uVar24 ^ uVar13 ^ uVar16 ^ uVar11;
  uVar12 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar8 + uVar27 + ((uVar23 | uVar14) & uVar26 | uVar23 & uVar14) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar13 = uVar13 << 1 | uVar13 >> 0x1f & 1;
  uVar21 = uVar30 ^ uVar21 ^ uVar28 ^ uVar18;
  uVar15 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar13 + uVar26 + ((uVar2 | uVar12) & uVar14 | uVar2 & uVar12) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0x8f1bbcdc;
  uVar21 = uVar21 << 1 | uVar21 >> 0x1f & 1;
  uVar24 = uVar22 ^ uVar24 ^ uVar9 ^ uVar8;
  uVar25 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar24 = uVar24 << 1 | uVar24 >> 0x1f & 1;
  uVar23 = uVar21 + uVar14 + (uVar15 ^ uVar12 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar30 = uVar19 ^ uVar30 ^ uVar17 ^ uVar13;
  uVar14 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar24 + uVar12 + (uVar25 ^ uVar15 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar30 = uVar30 << 1 | uVar30 >> 0x1f & 1;
  uVar12 = uVar20 ^ uVar22 ^ uVar32 ^ uVar21;
  uVar27 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar30 + uVar15 + (uVar14 ^ uVar25 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar22 = uVar12 << 1 | uVar12 >> 0x1f & 1;
  uVar12 = uVar31 ^ uVar19 ^ uVar11 ^ uVar24;
  uVar15 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar22 + uVar25 + (uVar27 ^ uVar14 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar19 = uVar12 << 1 | uVar12 >> 0x1f & 1;
  uVar12 = uVar16 ^ uVar20 ^ uVar18 ^ uVar30;
  uVar26 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar19 + uVar14 + (uVar15 ^ uVar27 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar20 = uVar12 << 1 | uVar12 >> 0x1f & 1;
  uVar12 = uVar28 ^ uVar31 ^ uVar8 ^ uVar22;
  uVar14 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar20 + uVar27 + (uVar26 ^ uVar15 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar31 = uVar12 << 1 | uVar12 >> 0x1f & 1;
  uVar12 = uVar9 ^ uVar16 ^ uVar13 ^ uVar19;
  uVar27 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar31 + uVar15 + (uVar14 ^ uVar26 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar16 = uVar12 << 1 | uVar12 >> 0x1f & 1;
  uVar28 = uVar17 ^ uVar28 ^ uVar21 ^ uVar20;
  uVar25 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar16 + uVar26 + (uVar27 ^ uVar14 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar28 = uVar28 << 1 | uVar28 >> 0x1f & 1;
  uVar9 = uVar32 ^ uVar9 ^ uVar24 ^ uVar31;
  uVar26 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar28 + uVar14 + (uVar25 ^ uVar27 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar14 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar9 = uVar11 ^ uVar17 ^ uVar30 ^ uVar16;
  uVar17 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = uVar14 + uVar27 + (uVar26 ^ uVar25 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar15 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar9 = uVar18 ^ uVar32 ^ uVar22 ^ uVar28;
  uVar27 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar12 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar15 + uVar25 + (uVar17 ^ uVar26 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar9 = uVar8 ^ uVar11 ^ uVar19 ^ uVar14;
  uVar25 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar9 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar2 = uVar12 + uVar26 + (uVar27 ^ uVar17 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar32 = uVar13 ^ uVar18 ^ uVar20 ^ uVar15;
  uVar18 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar32 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar23 = uVar9 + uVar17 + (uVar25 ^ uVar27 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar8 = uVar21 ^ uVar8 ^ uVar31 ^ uVar12;
  uVar31 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar17 = uVar8 << 1 | uVar8 >> 0x1f & 1;
  uVar2 = uVar32 + uVar27 + (uVar18 ^ uVar25 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar9 = uVar24 ^ uVar13 ^ uVar16 ^ uVar9;
  uVar16 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar11 = uVar9 << 1 | uVar9 >> 0x1f & 1;
  uVar23 = uVar17 + uVar25 + (uVar31 ^ uVar18 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar32 = uVar30 ^ uVar21 ^ uVar28 ^ uVar32;
  uVar21 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar13 = uVar32 << 1 | uVar32 >> 0x1f & 1;
  uVar2 = uVar11 + uVar18 + (uVar16 ^ uVar31 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar17 = uVar22 ^ uVar24 ^ uVar14 ^ uVar17;
  uVar9 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = uVar13 + uVar31 + (uVar21 ^ uVar16 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar11 = uVar19 ^ uVar30 ^ uVar15 ^ uVar11;
  uVar8 = uVar2 * 0x40000000 | uVar2 >> 2 & 0x3fffffff;
  uVar2 = (uVar17 << 1 | uVar17 >> 0x1f & 1) + uVar16 + (uVar9 ^ uVar21 ^ uVar2) + uVar23 * 0x20 +
          ((uVar23 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  uVar13 = uVar20 ^ uVar22 ^ uVar12 ^ uVar13;
  uVar14 = uVar23 * 0x40000000 | uVar23 >> 2 & 0x3fffffff;
  uVar23 = (uVar11 << 1 | uVar11 >> 0x1f & 1) + uVar21 + (uVar8 ^ uVar9 ^ uVar23) + uVar2 * 0x20 +
           ((uVar2 & 0xffffffff) >> 0x1b) + 0xca62c1d6;
  *(ulong *)(param_1 + 0x10) =
       uVar3 + (uVar13 << 1 | uVar13 >> 0x1f & 1) + uVar9 + (uVar14 ^ uVar8 ^ uVar2) + uVar23 * 0x20
       + (uVar23 >> 0x1b & 0x1f) + 0xca62c1d6;
  *(ulong *)(param_1 + 0x18) = uVar23 + uVar5;
  *(ulong *)(param_1 + 0x20) = uVar4 + uVar2 * 0x40000000 + ((uVar2 & 0xffffffff) >> 2);
  *(ulong *)(param_1 + 0x28) = uVar14 + uVar6;
  *(ulong *)(param_1 + 0x30) = uVar8 + *(long *)(param_1 + 0x30);
  return;
}


