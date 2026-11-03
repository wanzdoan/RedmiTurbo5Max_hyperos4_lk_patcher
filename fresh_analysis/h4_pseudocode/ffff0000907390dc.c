
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907390dc(long param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  undefined4 *puVar5;
  int iVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  
  uVar2 = _DAT_ffff00001cf3008c;
  lVar4 = 0;
  uVar8 = _DAT_ffff00001cf3008c & 0x1f;
  uVar9 = _DAT_ffff00001cf30010 & 0xfff0ff0f;
  do {
    uVar10 = (ulong)(uVar8 * 8 + (int)lVar4 & 0x18);
    puVar5 = (undefined4 *)(param_1 + lVar4);
    lVar4 = lVar4 + 8;
    *puVar5 = *(undefined4 *)(uVar10 | 0xffff00001cf300e0);
    puVar5[1] = *(undefined4 *)(uVar10 | 0xffff00001cf300e4);
  } while ((int)lVar4 != 0x100);
  lVar7 = 0;
  uVar8 = uVar2 >> 8;
  do {
    uVar10 = (ulong)((uVar2 >> 5 & 0x7fffff8) + (int)lVar7 & 0x18);
    _DAT_ffff00001cf30010 = (uVar8 >> 2 & 7) * 0x10010 + 0x80080 + uVar9;
    uVar8 = uVar8 + 1;
    puVar5 = (undefined4 *)(param_1 + lVar7 + lVar4);
    lVar7 = lVar7 + 8;
    *puVar5 = *(undefined4 *)(uVar10 | 0xffff00001cf300e0);
    puVar5[1] = *(undefined4 *)(uVar10 | 0xffff00001cf300e4);
    uVar3 = _DAT_ffff00001cf4008c;
  } while ((int)lVar7 != 0x100);
  iVar6 = 0;
  puVar5 = (undefined4 *)(param_1 + lVar4 + lVar7);
  uVar2 = _DAT_ffff00001cf40010 & 0xfff0ff0f;
  uVar9 = (_DAT_ffff00001cf4008c & 0x1f) << 3;
  do {
    uVar8 = uVar9 & 0x18;
    iVar6 = iVar6 + 1;
    uVar9 = uVar9 + 8;
    *puVar5 = *(undefined4 *)((ulong)uVar8 | 0xffff00001cf400e0);
    puVar5[1] = *(undefined4 *)((ulong)uVar8 | 0xffff00001cf400e4);
    puVar5 = puVar5 + 2;
  } while (iVar6 != 0x20);
  iVar6 = 0;
  uVar9 = (uVar3 >> 8) << 3;
  do {
    uVar8 = (uVar3 >> 8) + iVar6;
    uVar1 = uVar9 & 0x18;
    iVar6 = iVar6 + 1;
    _DAT_ffff00001cf40010 = (uVar8 >> 2 & 7) * 0x10010 + 0x80080 + uVar2;
    uVar9 = uVar9 + 8;
    *puVar5 = *(undefined4 *)((ulong)uVar1 | 0xffff00001cf400e0);
    puVar5[1] = *(undefined4 *)((ulong)uVar1 | 0xffff00001cf400e4);
    puVar5 = puVar5 + 2;
  } while (iVar6 != 0x20);
  return;
}


