// ffff00009078b6a8  size=188  FUN_ffff00009078b6a8

undefined4 FUN_ffff00009078b6a8(undefined8 *param_1,undefined8 *param_2)

{
  uint uVar1;
  int iVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff00009088a185 & 1) == 0) {
    uVar3 = 0xfffffffa;
  }
  else {
    FUN_ffff00009071639c(0x277);
    uVar1 = *(uint *)(param_2 + 1);
    *param_1 = param_2;
    param_2[4] = param_1;
    *(undefined4 *)(param_2 + 6) = 0;
    if (uVar1 < 0xfffd) {
      iVar2 = FUN_ffff00009078cdd8(param_1);
      if (iVar2 == 0) {
        FUN_ffff000090707ce4(*param_2,*(undefined4 *)(param_2 + 1));
        FUN_ffff00009078ce08(param_1,param_2);
        FUN_ffff00009078cd94(param_1);
        uVar3 = 0;
      }
      else {
        uVar3 = 0xfffffff5;
      }
    }
    else {
      FUN_ffff00009077de54(s__USB__req_length_>_supported_MAX_ffff0000907d51c2,0xfffc);
      uVar3 = 0xffffffa1;
    }
    FUN_ffff000090716408(0x277);
  }
  return uVar3;
}


