
void FUN_ffff0000be010000(ulong param_1)

{
  uint uVar1;
  int *piVar2;
  undefined4 *puVar3;
  uint uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  long lVar9;
  uint uVar10;
  uint uVar11;
  ulong uVar12;
  
  uVar7 = param_1 | 0xff00000000000000;
  if (DAT_ffff0000be0fd0c0 != 0xb7) {
    uVar7 = param_1;
  }
  uVar5 = FUN_ffff0000be0101cc(uVar7);
  if ((uVar5 & 1) == 0) {
    return;
  }
  uVar5 = uVar7 & 0xfffffffffffff000;
  uVar12 = uVar5 - 0x4000;
  uVar8 = uVar12;
  if ((DAT_ffff0000be0fd0c0 == 0xb7) &&
     (uVar8 = *(ulong *)(DAT_ffff0000be0fb000 + 0x68), uVar7 <= uVar8 || uVar8 <= uVar12)) {
    uVar8 = uVar12;
  }
  do {
    uVar12 = uVar5;
    if ((uVar12 <= uVar8) || (uVar5 = uVar12 - 0x1000, uVar5 < uVar8)) break;
    uVar6 = FUN_ffff0000be0101cc(uVar5);
  } while ((uVar6 & 1) != 0);
  uVar5 = uVar7 + 0x4fff & 0xfffffffffffff000;
  uVar7 = uVar7 + 0xfff & 0xfffffffffffff000;
  do {
    uVar8 = uVar7;
    if ((uVar5 <= uVar8) || (uVar7 = FUN_ffff0000be0101cc(uVar8), (uVar7 & 1) == 0)) break;
    uVar7 = uVar8 + 0x1000;
  } while (uVar8 + 0x1000 <= uVar5);
  if (DAT_ffff0000be0fd0c0 != 0x28) {
    FUN_ffff0000be00feb8(uVar12,uVar8);
    return;
  }
  lVar9 = 0;
  do {
    piVar2 = (int *)((ulong)DAT_ffff0000be0fd0c4 + DAT_ffff0000be0fd0b8 + 0x10 + lVar9);
    if (piVar2[-4] == 1) {
      uVar4 = piVar2[-2];
      uVar10 = (uint)uVar12;
      uVar1 = *piVar2 + uVar4;
      uVar11 = (uint)uVar8;
      if (uVar4 <= uVar10 && uVar11 <= uVar1) {
        return;
      }
      if ((uVar4 < uVar11) && (uVar10 < uVar1)) {
        if (uVar10 <= uVar4) {
          uVar4 = uVar10;
        }
        uVar12 = (ulong)uVar4;
        if (uVar1 <= uVar11) {
          uVar1 = uVar11;
        }
        uVar8 = (ulong)uVar1;
LAB_ffff0000be010144:
        puVar3 = (undefined4 *)((ulong)DAT_ffff0000be0fd0c4 + DAT_ffff0000be0fd0b8 + lVar9);
        *puVar3 = 1;
        puVar3[2] = (int)uVar12;
        puVar3[3] = 0;
        puVar3[4] = (int)uVar8 - (int)uVar12;
        *(undefined8 *)(puVar3 + 5) = 0;
        puVar3[7] = 0;
        return;
      }
    }
    else if (piVar2[-4] == 0) goto LAB_ffff0000be010144;
    lVar9 = lVar9 + 0x20;
    if (lVar9 == 0x780) {
      FUN_ffff0000be063b40(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
      FUN_ffff0000be05fd98(s_mrdump__MINI_NR_SECTION_overflow_ffff0000be0b855b);
      return;
    }
  } while( true );
}


