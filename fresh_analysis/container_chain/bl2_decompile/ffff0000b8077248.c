
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b8077248(undefined8 param_1,undefined1 param_2,undefined1 param_3)

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
  FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s__gate_ic_i2c_write_bytes_ffff0000b808c184);
  local_18 = param_2;
  local_17 = param_3;
  local_14 = param_2;
  local_13 = param_3;
  iVar1 = FUN_ffff0000b807733c(param_1,&local_34,&local_38);
  if ((-1 < iVar1) && (local_34 >> 0x10 == 0)) {
    _DAT_ffff0000b81b9ad8 = (undefined2)local_34;
    DAT_ffff0000b81b9ae0 = (undefined1)local_38;
    iVar1 = FUN_ffff0000b8019a84(local_34,local_38,&local_14,2,0);
    if (iVar1 < 0) {
      FUN_ffff0000b804c33c
                (s__LCM__ERROR___s___d_ffff0000b8095dcb,s__gate_ic_i2c_write_bytes_ffff0000b808c184,
                 iVar1);
    }
    FUN_ffff0000b8019bc8(DAT_ffff0000b81b9ad8,DAT_ffff0000b81b9ae0,&local_18,1,1,0);
    return iVar1;
  }
  return -1;
}


