
long FUN_ffff0000be00cbbc(ulong param_1,ulong param_2,long param_3)

{
  long lVar1;
  ulong uVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  long local_38;
  
  local_38 = 0;
  if (param_1 != 0) {
    if (DAT_ffff0000be0fd038 == 0 || DAT_ffff0000be0fd040 == 0) {
      FUN_ffff0000be063b40(s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5);
      FUN_ffff0000be05fd98
                (s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5,DAT_ffff0000be0fd038,
                 DAT_ffff0000be0fd040);
    }
    else if ((((DAT_ffff0000be0fd038 <= param_1) && (param_1 < DAT_ffff0000be0fd040)) &&
             (uVar2 = (param_1 + param_2) - 1, param_1 + param_2 != 1)) &&
            ((DAT_ffff0000be0fd038 <= uVar2 && (uVar2 < DAT_ffff0000be0fd040)))) {
      iVar3 = FUN_ffff0000be05d9a8
                        (PTR_DAT_ffff0000be0f5570,s_ke_mem_read_ffff0000be0ad2ab,0x200000,&local_38,
                         0x15,param_1 & 0xffffffffffe00000,0,0);
      if (iVar3 != 0) {
        FUN_ffff0000be063b40(s_kedump__map_error_ffff0000be0c28ae);
        FUN_ffff0000be05fd98(s_kedump__map_error_ffff0000be0c28ae);
        return (long)iVar3;
      }
      if (param_2 == 0) {
        lVar4 = 0;
      }
      else {
        lVar5 = 0;
        lVar4 = 0;
        lVar1 = local_38 + (param_1 & 0x1fffff) + param_2;
        do {
          uVar2 = param_2;
          if (0xfff < param_2) {
            uVar2 = 0x1000;
          }
          FUN_ffff0000be0607b0(param_3 + lVar5,lVar1 - param_2,uVar2);
          lVar4 = uVar2 + lVar4;
          lVar5 = lVar5 + uVar2;
          param_2 = param_2 - uVar2;
        } while (param_2 != 0);
      }
      FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_38);
      return lVar4;
    }
  }
  FUN_ffff0000be063b40(s_kedump__illegal_pa_0x_llx_sz_0x__ffff0000be0c3b8d,param_1,param_2);
  FUN_ffff0000be05fd98(s_kedump__illegal_pa_0x_llx_sz_0x__ffff0000be0c3b8d,param_1,param_2);
  return 0;
}


