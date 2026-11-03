// ffff00009071a14c  size=508  FUN_ffff00009071a14c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009071a14c(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  undefined1 *puVar4;
  undefined4 uVar5;
  ulong uVar6;
  
  if ((DAT_ffff000090848c84 & 1) != 0) {
    lVar1 = FUN_ffff00009071a348(param_2,0);
    FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
    if (lVar1 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__Error__cannot_find__s_nod_ffff0000907d75b7,
                 s_mblock_pre_alloc_phandle_ffff0000907ec0e1,0x2ae,param_2);
      uVar5 = 0xffffffff;
    }
    else {
      uVar6 = (ulong)_DAT_ffff000090848c88;
      if (_DAT_ffff000090848c88 < 0x80) {
        uVar2 = FUN_ffff00009077f250(param_1);
        if (uVar2 < 0x80) {
          FUN_ffff00009077eb20(uVar6 * 0x100 + -0xffff6f7b7374,param_1,uVar2);
          uVar6 = (ulong)_DAT_ffff000090848c88;
          lVar1 = FUN_ffff00009077f250(param_1);
          puVar4 = (undefined1 *)(uVar6 * 0x100 + -0xffff6f7b7374 + lVar1);
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d__Warning_device_name_reach_ffff0000907f0f40,
                     s_mblock_pre_alloc_phandle_ffff0000907ec0e1,0x2ba);
          FUN_ffff00009077eb20((ulong)_DAT_ffff000090848c88 * 0x100 + -0xffff6f7b7374,param_1,0x80);
          uVar6 = (ulong)_DAT_ffff000090848c88;
          puVar4 = (undefined1 *)(uVar6 * 0x100 + -0xffff6f7b72f5);
        }
        *puVar4 = 0;
        uVar2 = FUN_ffff00009077f250(param_2);
        if (uVar2 < 0x80) {
          FUN_ffff00009077eb20(uVar6 * 0x100 + -0xffff6f7b72f4,param_2,uVar2);
          uVar3 = _DAT_ffff000090848c88;
          uVar6 = (ulong)_DAT_ffff000090848c88;
          lVar1 = FUN_ffff00009077f250(param_2);
          puVar4 = (undefined1 *)(uVar6 * 0x100 + lVar1 + -0xffff6f7b72f4);
        }
        else {
          FUN_ffff00009077e11c
                    (s__s__d__Warning_mblock_name_reach_ffff0000907f43cf,
                     s_mblock_pre_alloc_phandle_ffff0000907ec0e1,0x2c3);
          FUN_ffff00009077eb20((ulong)_DAT_ffff000090848c88 * 0x100 + -0xffff6f7b72f4,param_2,0x80);
          puVar4 = (undefined1 *)((ulong)_DAT_ffff000090848c88 * 0x100 + -0xffff6f7b7275);
          uVar3 = _DAT_ffff000090848c88;
        }
        uVar5 = 0;
        _DAT_ffff000090848c88 = uVar3 + 1;
        *puVar4 = 0;
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__Error__device_dts_reach_t_ffff0000907dc2bc,
                   s_mblock_pre_alloc_phandle_ffff0000907ec0e1,0x2b4,0x80);
        uVar5 = 0xffffffff;
      }
    }
    FUN_ffff0000907177d0(&LAB_ffff000090818208);
    return uVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_lib_mblock_mblock_c_ffff0000907f7d89,0x2a8,s_mblock_init_done_ffff0000907f5edf);
}


