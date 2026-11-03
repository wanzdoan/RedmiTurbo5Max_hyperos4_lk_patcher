
int FUN_ffff0000b80132c0(undefined8 param_1,undefined4 param_2,uint *param_3)

{
  int iVar1;
  byte local_14 [4];
  
  local_14[0] = (byte)param_2;
  iVar1 = FUN_ffff0000b8019bc8(3,0x53,local_14,1,1,0);
  if (iVar1 < 0) {
    FUN_ffff0000b804c33c(s_mt6379_2p_read_reg__d_fail___d_ffff0000b809a7b8,local_14[0],iVar1);
  }
  else {
    *param_3 = (uint)local_14[0];
    FUN_ffff0000b804c33c
              (s__s_I2CR_0x_04X____0x_04X_ffff0000b8088c40,s_mt6379_2p_reg_read_ffff0000b80899ef,
               param_2);
  }
  return iVar1;
}


