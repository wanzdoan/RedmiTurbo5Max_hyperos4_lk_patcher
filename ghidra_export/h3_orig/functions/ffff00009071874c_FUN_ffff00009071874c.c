// ffff00009071874c  size=228  FUN_ffff00009071874c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009071874c(long param_1)

{
  undefined1 *puVar1;
  undefined1 *puVar2;
  long *plVar3;
  undefined8 uVar4;
  ulong uVar5;
  int iVar6;
  undefined1 *puVar7;
  
  uVar4 = daif;
  if (((uint)uVar4 >> 7 & 1) == 0) {
    uVar5 = daif;
    daif = uVar5 | 0x80;
  }
  plVar3 = *(long **)(param_1 + 0x10);
  _DAT_ffff000090843b98 = 1;
  puVar7 = (undefined1 *)0x0;
  if (_DAT_ffff000090843bc8 != &DAT_ffff000090843bc0) {
    puVar7 = _DAT_ffff000090843bc8;
  }
  puVar1 = (undefined1 *)0x0;
  if (puVar7 != (undefined1 *)0x0) {
    puVar1 = puVar7 + -8;
  }
  puVar7 = _DAT_ffff000090843bc8;
  if (*(long *)(param_1 + 8) != 0 || plVar3 != (long *)0x0) {
    *plVar3 = *(long *)(param_1 + 8);
    *(long **)(*(long *)(param_1 + 8) + 8) = plVar3;
    puVar7 = _DAT_ffff000090843bc8;
    *(undefined8 *)(param_1 + 8) = 0;
    *(undefined8 *)(param_1 + 0x10) = 0;
  }
  *(undefined4 *)(param_1 + 0x1c) = 0;
  puVar2 = (undefined1 *)0x0;
  if (puVar7 != &DAT_ffff000090843bc0) {
    puVar2 = puVar7;
  }
  *(undefined8 *)(param_1 + 0x20) = 0;
  *(undefined8 *)(param_1 + 0x28) = 0;
  if ((puVar2 == (undefined1 *)0x0) || (puVar2 == (undefined1 *)0x8)) {
    FUN_ffff000090716754();
  }
  else if (puVar1 != puVar2 + -8) {
    iVar6 = FUN_ffff0000907167d4();
    FUN_ffff0000907166a8
              (FUN_ffff000090718830,0,
               *(int *)(puVar2 + 0x10) - iVar6 &
               (*(int *)(puVar2 + 0x10) - iVar6 >> 0x1f ^ 0xffffffffU));
  }
  if (((uint)uVar4 >> 7 & 1) == 0) {
    uVar5 = daif;
    daif = uVar5 & 0xffffffffffffff7f;
  }
  _DAT_ffff000090843b98 = 0;
  return;
}


