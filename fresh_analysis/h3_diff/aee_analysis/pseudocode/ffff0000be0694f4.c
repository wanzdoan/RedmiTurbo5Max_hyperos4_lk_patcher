
undefined4 FUN_ffff0000be0694f4(undefined8 *param_1,undefined8 *param_2)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff0000be1348a5 & 1) == 0) {
    uVar3 = 0xfffffffa;
  }
  else {
    FUN_ffff0000be0194d8(0x277);
    uVar1 = *(uint *)(param_2 + 1);
    *param_1 = param_2;
    param_2[4] = param_1;
    *(undefined4 *)(param_2 + 6) = 0;
    if (uVar1 < 0xfffd) {
      iVar2 = FUN_ffff0000be06ac24(param_1);
      if (iVar2 == 0) {
        FUN_ffff0000be007328(*param_2,*(undefined4 *)(param_2 + 1));
        FUN_ffff0000be06ac54(param_1,param_2);
        FUN_ffff0000be06abe0(param_1);
        uVar3 = 0;
      }
      else {
        uVar3 = 0xfffffff5;
      }
    }
    else {
      FUN_ffff0000be05fd98(s__USB__req_length_>_supported_MAX_ffff0000be0a76db,0xfffc);
      uVar3 = 0xffffffa1;
    }
    FUN_ffff0000be019544(0x277);
  }
  return uVar3;
}


