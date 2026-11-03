
int FUN_ffff0000be01f1b8(undefined8 param_1,undefined4 param_2,uint *param_3)

{
  int iVar1;
  byte local_14 [4];
  
  local_14[0] = (byte)param_2;
  iVar1 = FUN_ffff0000be022dc0(3,0x53,local_14,1,1,0);
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98(s_mt6379_2p_read_reg__d_fail___d_ffff0000be0be1be,local_14[0],iVar1);
  }
  else {
    *param_3 = (uint)local_14[0];
    FUN_ffff0000be05fd98
              (s__s_I2CR_0x_04X____0x_04X_ffff0000be0a7d39,s_mt6379_2p_reg_read_ffff0000be0a8f13,
               param_2);
  }
  return iVar1;
}


