// ffff0000907179f0  size=156  FUN_ffff0000907179f0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907179f0(void)

{
  long *plVar1;
  undefined8 uVar2;
  ulong uVar3;
  long lVar4;
  long *plVar5;
  long lVar6;
  long lVar7;
  
  lVar4 = tpidr_el1;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  _DAT_ffff000090843678 = 1;
  *(undefined8 *)(lVar4 + 0x2c) = 1;
  if ((*(byte *)(lVar4 + 0x34) >> 4 & 1) == 0) {
    lVar7 = (long)(int)*(uint *)(lVar4 + 0x28) * 0x10;
    plVar1 = (long *)(lVar7 + -0xffff6f7bc940);
    _DAT_ffff000090843b90 = _DAT_ffff000090843b90 | 1 << (ulong)(*(uint *)(lVar4 + 0x28) & 0x1f);
    plVar5 = (long *)(lVar4 + 0x18);
    *plVar5 = *plVar1;
    lVar6 = *plVar1;
    *(long **)(lVar4 + 0x20) = plVar1;
    *(long **)(lVar7 + -0xffff6f7bc940) = plVar5;
    *(long **)(lVar6 + 8) = plVar5;
  }
  FUN_ffff000090717fe8();
  _DAT_ffff000090843678 = 0;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return;
}


