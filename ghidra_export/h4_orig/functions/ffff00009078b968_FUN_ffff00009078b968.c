// ffff00009078b968  size=188  FUN_ffff00009078b968

undefined4 FUN_ffff00009078b968(undefined8 *param_1,undefined8 *param_2)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff00009088a21d & 1) == 0) {
    uVar3 = 0xfffffffa;
  }
  else {
    FUN_ffff0000907164e0(0x277);
    uVar1 = *(uint *)(param_2 + 1);
    *param_1 = param_2;
    param_2[4] = param_1;
    *(undefined4 *)(param_2 + 6) = 0;
    if (uVar1 < 0xfffd) {
      iVar2 = FUN_ffff00009078d098(param_1);
      if (iVar2 == 0) {
        FUN_ffff000090707c70(*param_2,*(undefined4 *)(param_2 + 1));
        FUN_ffff00009078d0c8(param_1,param_2);
        FUN_ffff00009078d054(param_1);
        uVar3 = 0;
      }
      else {
        uVar3 = 0xfffffff5;
      }
    }
    else {
      FUN_ffff00009077e11c(s__USB__req_length_>_supported_MAX_ffff0000907d51ff,0xfffc);
      uVar3 = 0xffffffa1;
    }
    FUN_ffff00009071654c(0x277);
  }
  return uVar3;
}


