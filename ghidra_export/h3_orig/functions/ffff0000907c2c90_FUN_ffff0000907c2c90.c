// ffff0000907c2c90  size=100  FUN_ffff0000907c2c90

void FUN_ffff0000907c2c90(undefined8 *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 *puVar4;
  
  puVar3 = PTR_LOOP_ffff000090838440;
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    *param_1 = &PTR_LOOP_ffff000090838438;
    param_1[1] = puVar3;
    puVar4 = param_1;
    *(undefined8 **)PTR_LOOP_ffff000090838440 = param_1;
    PTR_LOOP_ffff000090838440 = (undefined *)puVar4;
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
    return;
  }
  *param_1 = &PTR_LOOP_ffff000090838438;
  param_1[1] = puVar3;
  puVar4 = param_1;
  *(undefined8 **)PTR_LOOP_ffff000090838440 = param_1;
  PTR_LOOP_ffff000090838440 = (undefined *)puVar4;
  return;
}


