
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be095ea4(undefined8 param_1,undefined1 param_2,undefined1 param_3)

{
  int iVar1;
  undefined4 local_38;
  uint local_34;
  undefined1 local_18;
  undefined1 local_17;
  undefined1 local_14;
  undefined1 local_13;
  
  local_34 = 0;
  local_38 = 0;
  FUN_ffff0000be05fd98(&DAT_ffff0000be0b835e,s__gate_ic_i2c_write_bytes_ffff0000be0ac1bb);
  local_18 = param_2;
  local_17 = param_3;
  local_14 = param_2;
  local_13 = param_3;
  iVar1 = FUN_ffff0000be095f98(param_1,&local_34,&local_38);
  if ((-1 < iVar1) && (local_34 >> 0x10 == 0)) {
    _DAT_ffff0000be1b7db0 = (undefined2)local_34;
    DAT_ffff0000be1b7db8 = (undefined1)local_38;
    iVar1 = FUN_ffff0000be022c7c(local_34,local_38,&local_14,2,0);
    if (iVar1 < 0) {
      FUN_ffff0000be05fd98
                (s__LCM__ERROR___s___d_ffff0000be0b8363,s__gate_ic_i2c_write_bytes_ffff0000be0ac1bb,
                 iVar1);
    }
    FUN_ffff0000be022dc0(DAT_ffff0000be1b7db0,DAT_ffff0000be1b7db8,&local_18,1,1,0);
    return iVar1;
  }
  return -1;
}


