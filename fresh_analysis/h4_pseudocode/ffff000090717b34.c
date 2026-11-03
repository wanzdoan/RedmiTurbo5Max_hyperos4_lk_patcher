
void FUN_ffff000090717b34(void)

{
  long *plVar1;
  uint uVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  long *plVar6;
  long lVar7;
  
  lVar5 = tpidr_el1;
  uVar3 = daif;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 | 0x80;
  }
  DAT_ffff0000908436b0 = 1;
  *(undefined8 *)(lVar5 + 0x2c) = 1;
  if ((*(byte *)(lVar5 + 0x34) >> 4 & 1) == 0) {
    uVar2 = *(uint *)(lVar5 + 0x28);
    plVar1 = &DAT_ffff0000908436f8 + (long)(int)uVar2 * 2;
    DAT_ffff000090843bc8 = DAT_ffff000090843bc8 | 1 << (ulong)(uVar2 & 0x1f);
    plVar6 = (long *)(lVar5 + 0x18);
    *plVar6 = *plVar1;
    lVar7 = *plVar1;
    *(long **)(lVar5 + 0x20) = plVar1;
    (&DAT_ffff0000908436f8)[(long)(int)uVar2 * 2] = plVar6;
    *(long **)(lVar7 + 8) = plVar6;
  }
  FUN_ffff00009071812c();
  DAT_ffff0000908436b0 = 0;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    daif = uVar4 & 0xffffffffffffff7f;
  }
  return;
}


