
void FUN_ffff000090718890(long param_1)

{
  undefined8 *puVar1;
  undefined8 *puVar2;
  long *plVar3;
  undefined8 uVar4;
  ulong uVar5;
  int iVar6;
  undefined8 *puVar7;
  
  uVar4 = daif;
  if (((uint)uVar4 >> 7 & 1) == 0) {
    uVar5 = daif;
    daif = uVar5 | 0x80;
  }
  plVar3 = *(long **)(param_1 + 0x10);
  DAT_ffff000090843bd0 = 1;
  puVar7 = (undefined8 *)0x0;
  if (DAT_ffff000090843c08 != &DAT_ffff000090843c00) {
    puVar7 = DAT_ffff000090843c08;
  }
  puVar1 = (undefined8 *)0x0;
  if (puVar7 != (undefined8 *)0x0) {
    puVar1 = puVar7 + -1;
  }
  puVar7 = DAT_ffff000090843c08;
  if (*(long *)(param_1 + 8) != 0 || plVar3 != (long *)0x0) {
    *plVar3 = *(long *)(param_1 + 8);
    *(long **)(*(long *)(param_1 + 8) + 8) = plVar3;
    puVar7 = DAT_ffff000090843c08;
    *(undefined8 *)(param_1 + 8) = 0;
    *(undefined8 *)(param_1 + 0x10) = 0;
  }
  *(undefined4 *)(param_1 + 0x1c) = 0;
  puVar2 = (undefined8 *)0x0;
  if (puVar7 != &DAT_ffff000090843c00) {
    puVar2 = puVar7;
  }
  *(undefined8 *)(param_1 + 0x20) = 0;
  *(undefined8 *)(param_1 + 0x28) = 0;
  if ((puVar2 == (undefined8 *)0x0) || (puVar2 == (undefined8 *)0x8)) {
    FUN_ffff000090716898();
  }
  else if (puVar1 != puVar2 + -1) {
    iVar6 = FUN_ffff000090716918();
    FUN_ffff0000907167ec
              (&LAB_ffff000090718974,0,
               *(int *)(puVar2 + 2) - iVar6 & (*(int *)(puVar2 + 2) - iVar6 >> 0x1f ^ 0xffffffffU));
  }
  if (((uint)uVar4 >> 7 & 1) == 0) {
    uVar5 = daif;
    daif = uVar5 & 0xffffffffffffff7f;
  }
  DAT_ffff000090843bd0 = 0;
  return;
}


