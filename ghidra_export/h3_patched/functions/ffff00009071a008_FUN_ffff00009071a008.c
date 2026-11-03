// ffff00009071a008  size=508  FUN_ffff00009071a008

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009071a008(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  undefined1 *puVar4;
  undefined4 uVar5;
  ulong uVar6;
  
  if ((DAT_ffff000090848c44 & 1) != 0) {
    lVar1 = FUN_ffff00009071a204(param_2,0);
    FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
    if (lVar1 == 0) {
      FUN_ffff00009077de54
                (s__s__d__Error__cannot_find__s_nod_ffff0000907d7521,
                 s_mblock_pre_alloc_phandle_ffff0000907ebf47,0x2ae,param_2);
      uVar5 = 0xffffffff;
    }
    else {
      uVar6 = (ulong)_DAT_ffff000090848c48;
      if (_DAT_ffff000090848c48 < 0x80) {
        uVar2 = FUN_ffff00009077ef88(param_1);
        if (uVar2 < 0x80) {
          FUN_ffff00009077e858(uVar6 * 0x100 + -0xffff6f7b73b4,param_1,uVar2);
          uVar6 = (ulong)_DAT_ffff000090848c48;
          lVar1 = FUN_ffff00009077ef88(param_1);
          puVar4 = (undefined1 *)(uVar6 * 0x100 + -0xffff6f7b73b4 + lVar1);
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d__Warning_device_name_reach_ffff0000907f0db8,
                     s_mblock_pre_alloc_phandle_ffff0000907ebf47,0x2ba);
          FUN_ffff00009077e858((ulong)_DAT_ffff000090848c48 * 0x100 + -0xffff6f7b73b4,param_1,0x80);
          uVar6 = (ulong)_DAT_ffff000090848c48;
          puVar4 = (undefined1 *)(uVar6 * 0x100 + -0xffff6f7b7335);
        }
        *puVar4 = 0;
        uVar2 = FUN_ffff00009077ef88(param_2);
        if (uVar2 < 0x80) {
          FUN_ffff00009077e858(uVar6 * 0x100 + -0xffff6f7b7334,param_2,uVar2);
          uVar3 = _DAT_ffff000090848c48;
          uVar6 = (ulong)_DAT_ffff000090848c48;
          lVar1 = FUN_ffff00009077ef88(param_2);
          puVar4 = (undefined1 *)(uVar6 * 0x100 + lVar1 + -0xffff6f7b7334);
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d__Warning_mblock_name_reach_ffff0000907f424b,
                     s_mblock_pre_alloc_phandle_ffff0000907ebf47,0x2c3);
          FUN_ffff00009077e858((ulong)_DAT_ffff000090848c48 * 0x100 + -0xffff6f7b7334,param_2,0x80);
          puVar4 = (undefined1 *)((ulong)_DAT_ffff000090848c48 * 0x100 + -0xffff6f7b72b5);
          uVar3 = _DAT_ffff000090848c48;
        }
        uVar5 = 0;
        _DAT_ffff000090848c48 = uVar3 + 1;
        *puVar4 = 0;
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d__Error__device_dts_reach_t_ffff0000907dc1cf,
                   s_mblock_pre_alloc_phandle_ffff0000907ebf47,0x2b4,0x80);
        uVar5 = 0xffffffff;
      }
    }
    FUN_ffff00009071768c(&LAB_ffff0000908181f8);
    return uVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_lib_mblock_mblock_c_ffff0000907f7bed,0x2a8,s_mblock_init_done_ffff0000907f5d51);
}


