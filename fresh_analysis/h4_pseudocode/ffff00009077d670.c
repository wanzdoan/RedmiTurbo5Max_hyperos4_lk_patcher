
void FUN_ffff00009077d670(undefined8 *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 *puVar4;
  
  puVar3 = PTR_LOOP_ffff000090832930;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    *param_1 = &PTR_LOOP_ffff000090832928;
    DAT_ffff00009087cfc8 = 0;
    param_1[1] = puVar3;
    puVar4 = param_1;
    *(undefined8 **)PTR_LOOP_ffff000090832930 = param_1;
    PTR_LOOP_ffff000090832930 = (undefined *)puVar4;
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
    return;
  }
  *param_1 = &PTR_LOOP_ffff000090832928;
  DAT_ffff00009087cfc8 = 0;
  param_1[1] = puVar3;
  puVar4 = param_1;
  *(undefined8 **)PTR_LOOP_ffff000090832930 = param_1;
  PTR_LOOP_ffff000090832930 = (undefined *)puVar4;
  return;
}


