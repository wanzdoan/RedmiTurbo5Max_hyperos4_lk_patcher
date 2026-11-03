
int FUN_ffff000090720f18(undefined8 param_1,undefined4 param_2,uint *param_3)

{
  int iVar1;
  byte local_14 [4];
  
  local_14[0] = (byte)param_2;
  iVar1 = FUN_ffff000090728b24(3,0x53,local_14,1,1,0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s_mt6379_2p_read_reg__d_fail___d_ffff0000907f60e5,local_14[0],iVar1);
  }
  else {
    *param_3 = (uint)local_14[0];
    FUN_ffff00009077e11c
              (s__s_I2CR_0x_04X____0x_04X_ffff0000907d5ca4,s_mt6379_2p_reg_read_ffff0000907d7712,
               param_2);
  }
  return iVar1;
}


