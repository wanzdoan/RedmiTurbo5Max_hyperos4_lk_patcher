
undefined8
FUN_ffff000090709330(undefined8 *param_1,undefined8 param_2,undefined8 param_3,uint param_4)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  
  *(uint *)(param_1 + 2) = param_4;
  param_1[3] = param_2;
  param_1[4] = param_3;
  if ((param_4 & 1) == 0) {
    lVar1 = FUN_ffff00009077b004(1,0);
    if (lVar1 == 0) {
      uVar3 = 0xfffffffb;
    }
    else {
      param_1[1] = lVar1;
      uVar2 = FUN_ffff00009077b334();
      *param_1 = uVar2;
      FUN_ffff00009077ee50(param_1[1],0,0x1000);
      uVar3 = 0;
    }
  }
  else {
    param_1[1] = &DAT_ffff00009083a000;
    uVar2 = FUN_ffff00009077b334();
    uVar3 = 0;
    *param_1 = uVar2;
  }
  return uVar3;
}


