
void FUN_ffff0000be036524(long param_1,long param_2)

{
  uint *puVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  long lVar20;
  int iVar21;
  ulong uVar22;
  int iVar23;
  ulong uVar24;
  ulong uVar25;
  ulong uVar26;
  ulong auStack_2d8 [8];
  long alStack_298 [5];
  ulong auStack_270 [66];
  
  FUN_ffff0000be060ae0(auStack_270 + 2,0,0x200);
  uVar3 = *(ulong *)(param_1 + 8);
  uVar7 = *(ulong *)(param_1 + 0x10);
  lVar20 = 0;
  param_2 = param_2 + 3;
  uVar4 = *(ulong *)(param_1 + 0x18);
  uVar8 = *(ulong *)(param_1 + 0x20);
  uVar5 = *(ulong *)(param_1 + 0x28);
  uVar9 = *(ulong *)(param_1 + 0x30);
  uVar6 = *(ulong *)(param_1 + 0x38);
  uVar10 = *(ulong *)(param_1 + 0x40);
  do {
    puVar1 = (uint *)(param_2 + -3);
    param_2 = param_2 + 4;
    uVar11 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
    *(ulong *)((long)auStack_270 + lVar20 + 0x10) = (ulong)(uVar11 >> 0x10 | uVar11 << 0x10);
    lVar20 = lVar20 + 8;
  } while (lVar20 != 0x80);
  lVar20 = 0x80;
  do {
    lVar2 = lVar20 + 8;
    uVar25 = *(ulong *)((long)auStack_270 + lVar20);
    uVar26 = *(ulong *)((long)auStack_2d8 + lVar20);
    *(ulong *)((long)auStack_270 + lVar20 + 0x10) =
         auStack_270[2] + *(long *)((long)alStack_298 + lVar20) +
         (((ulong)(uint)((int)uVar25 << 0xd) | uVar25 >> 0x13 & 0x1fff) ^
          (uVar25 & 0xffffffff) >> 10 ^
         ((ulong)(uint)((int)uVar25 << 0xf) | uVar25 >> 0x11 & 0x7fff)) +
         (((ulong)(uint)((int)uVar26 << 0xe) | uVar26 >> 0x12 & 0x3fff) ^ (uVar26 & 0xffffffff) >> 3
         ^ ((ulong)(uint)((int)uVar26 << 0x19) | uVar26 >> 7 & 0x1ffffff));
    lVar20 = lVar2;
    auStack_270[2] = uVar26;
  } while (lVar2 != 0x200);
  lVar20 = 0;
  uVar12 = uVar4;
  uVar26 = uVar3;
  uVar16 = uVar8;
  uVar17 = uVar10;
  uVar25 = uVar5;
  uVar13 = uVar6;
  uVar18 = uVar9;
  uVar15 = uVar7;
  do {
    uVar24 = uVar15;
    uVar22 = uVar18;
    uVar19 = uVar13;
    uVar18 = uVar25;
    uVar15 = uVar26;
    uVar14 = uVar12;
    iVar21 = (int)uVar18;
    iVar23 = (int)uVar15;
    lVar2 = *(long *)((long)&DAT_ffff0000be0caca0 + lVar20) + uVar17 +
            *(long *)((long)auStack_270 + lVar20 + 0x10) +
            (((ulong)(uint)(iVar21 << 0x1a) | uVar18 >> 6 & 0x3ffffff) ^
             ((ulong)(uint)(iVar21 << 0x15) | uVar18 >> 0xb & 0x1fffff) ^
            ((ulong)(uint)(iVar21 << 7) | uVar18 >> 0x19 & 0x7f)) +
            (uVar22 & uVar18 | uVar19 & (uVar18 ^ 0xffffffffffffffff));
    uVar25 = lVar2 + uVar16;
    uVar26 = lVar2 + ((uVar24 | uVar15) & uVar14 | uVar24 & uVar15) +
             (((ulong)(uint)(iVar23 << 0x1e) | uVar15 >> 2 & 0x3fffffff) ^
              ((ulong)(uint)(iVar23 << 0x13) | uVar15 >> 0xd & 0x7ffff) ^
             ((ulong)(uint)(iVar23 << 10) | uVar15 >> 0x16 & 0x3ff));
    lVar20 = lVar20 + 8;
    uVar12 = uVar24;
    uVar16 = uVar14;
    uVar17 = uVar19;
    uVar13 = uVar22;
  } while (lVar20 != 0x200);
  *(ulong *)(param_1 + 8) = uVar26 + uVar3;
  *(ulong *)(param_1 + 0x10) = uVar15 + uVar7;
  *(ulong *)(param_1 + 0x18) = uVar24 + uVar4;
  *(ulong *)(param_1 + 0x20) = uVar14 + uVar8;
  *(ulong *)(param_1 + 0x28) = uVar25 + uVar5;
  *(ulong *)(param_1 + 0x30) = uVar18 + uVar9;
  *(ulong *)(param_1 + 0x38) = uVar22 + uVar6;
  *(ulong *)(param_1 + 0x40) = uVar19 + uVar10;
  return;
}


