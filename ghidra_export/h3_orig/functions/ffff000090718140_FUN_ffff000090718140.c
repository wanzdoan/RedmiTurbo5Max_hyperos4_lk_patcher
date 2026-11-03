// ffff000090718140  size=196  FUN_ffff000090718140

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090718140(void)

{
  uint uVar1;
  undefined8 uVar2;
  ulong uVar3;
  long lVar4;
  long *plVar5;
  long lVar6;
  long *plVar7;
  long *plVar8;
  
  lVar4 = tpidr_el1;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  _DAT_ffff000090843678 = 1;
  *(undefined4 *)(lVar4 + 0x2c) = 1;
  if ((*(byte *)(lVar4 + 0x34) >> 4 & 1) == 0) {
    uVar1 = *(uint *)(lVar4 + 0x28);
    plVar5 = (long *)((long)(int)uVar1 * 0x10 + -0xffff6f7bc940);
    if (*(int *)(lVar4 + 0x30) < 1) {
      lVar6 = *plVar5;
      *(long **)(lVar4 + 0x20) = plVar5;
      *(long *)(lVar4 + 0x18) = lVar6;
      plVar7 = (long *)(*plVar5 + 8);
    }
    else {
      plVar8 = (long *)((long)(int)uVar1 * 0x10 + -0xffff6f7bc938);
      lVar6 = *plVar8;
      *(long **)(lVar4 + 0x18) = plVar5;
      *(long *)(lVar4 + 0x20) = lVar6;
      plVar7 = (long *)*plVar8;
      plVar5 = plVar8;
    }
    *plVar7 = lVar4 + 0x18;
    *plVar5 = lVar4 + 0x18;
    _DAT_ffff000090843b90 = _DAT_ffff000090843b90 | 1 << (ulong)(uVar1 & 0x1f);
  }
  FUN_ffff000090717fe8();
  _DAT_ffff000090843678 = 0;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return;
}


