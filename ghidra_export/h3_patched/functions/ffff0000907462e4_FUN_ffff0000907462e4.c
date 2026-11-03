// ffff0000907462e4  size=128  FUN_ffff0000907462e4

undefined4 FUN_ffff0000907462e4(undefined8 *param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 *puVar4;
  undefined4 uVar5;
  
  puVar3 = PTR_LOOP_ffff00009081a6a8;
  if ((param_1 == (undefined8 *)0x0) || (param_1[3] == 0)) {
    uVar5 = 0xfffffff8;
  }
  else {
    uVar1 = daif;
    if (((uint)uVar1 >> 7 & 1) == 0) {
      uVar2 = daif;
      daif = uVar2 | 0x80;
      *param_1 = &PTR_LOOP_ffff00009081a6a0;
      param_1[1] = puVar3;
      DAT_ffff00009081a6b0 = 0;
      uVar5 = 0;
      puVar4 = param_1;
      *(undefined8 **)PTR_LOOP_ffff00009081a6a8 = param_1;
      PTR_LOOP_ffff00009081a6a8 = (undefined *)puVar4;
      uVar2 = daif;
      daif = uVar2 & 0xffffffffffffff7f;
    }
    else {
      uVar5 = 0;
      *param_1 = &PTR_LOOP_ffff00009081a6a0;
      param_1[1] = puVar3;
      DAT_ffff00009081a6b0 = 0;
      puVar4 = param_1;
      *(undefined8 **)PTR_LOOP_ffff00009081a6a8 = param_1;
      PTR_LOOP_ffff00009081a6a8 = (undefined *)puVar4;
    }
  }
  return uVar5;
}


