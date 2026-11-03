
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b8077454(undefined8 param_1,byte param_2,undefined8 param_3,short param_4)

{
  int iVar1;
  undefined8 local_88;
  ulong local_80 [8];
  
  FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s__gate_ic_i2c_write_regs_ffff0000b8092f86);
  if ((ushort)(param_4 + 1U) < 0x41) {
    local_80[6] = 0;
    local_80[7] = 0;
    local_80[4] = 0;
    local_80[5] = 0;
    local_80[2] = 0;
    local_80[3] = 0;
    local_80[1] = 0;
    local_88 = 0;
    local_80[0] = (ulong)param_2;
    FUN_ffff0000b804cd40((ulong)local_80 | 1,param_3,param_4);
    iVar1 = FUN_ffff0000b807733c(param_1,(long)&local_88 + 4,&local_88);
    if ((-1 < iVar1) && ((local_88 & 0xffff000000000000) == 0)) {
      _DAT_ffff0000b81b9ad8 = (undefined2)(local_88 >> 0x20);
      DAT_ffff0000b81b9ae0 = (undefined1)local_88;
      iVar1 = FUN_ffff0000b8019a84(local_88._4_4_,local_88 & 0xffffffff,local_80,param_4 + 1U,0);
      if (iVar1 < 0) {
        FUN_ffff0000b804c33c
                  (s__LCM__ERROR___s___d_ffff0000b8095dcb,s__gate_ic_i2c_write_regs_ffff0000b8092f86
                   ,iVar1);
      }
      FUN_ffff0000b804c33c(&DAT_ffff0000b8086d00,s__gate_ic_i2c_write_regs_ffff0000b8092f86);
      return iVar1;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s__LCM__ERROR___s__i2c_wr__len__d_i_ffff0000b8092f9e,
               s__gate_ic_i2c_write_regs_ffff0000b8092f86,param_4);
  }
  return -1;
}


